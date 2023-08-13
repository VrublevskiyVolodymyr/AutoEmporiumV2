package com.autoemporium.autoemporium.services.financialServices;

import com.autoemporium.autoemporium.dao.PremiumPurchaseDAO;
import com.autoemporium.autoemporium.dao.SellerDAO;
import com.autoemporium.autoemporium.models.PremiumPurchase;
import com.autoemporium.autoemporium.models.users.AccountType;
import com.autoemporium.autoemporium.models.users.Seller;
import com.autoemporium.autoemporium.services.financialServices.LiqPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.*;


@Service
public class LiqPayServiceImpl1 implements LiqPayService {
    @Value(value = "${liqpay.publicKey}")
    private String publicKey;

    @Value(value = "${liqpay.privateKey}")
    private String privateKey;

    private PremiumPurchaseDAO premiumPurchaseDAO;

    private SellerDAO sellerDAO;


    public LiqPayServiceImpl1(PremiumPurchaseDAO premiumPurchaseDAO, SellerDAO sellerDAO) {
        this.premiumPurchaseDAO = premiumPurchaseDAO;
        this.sellerDAO = sellerDAO;
    }

    @Override
    public ResponseEntity<String> createPayment(Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);

        if (premiumPurchaseDAO.findBySeller(seller) != null) {
            return new ResponseEntity<>("You already have a premium account", HttpStatus.OK);
        }
        String orderId = generateOrderId();
        PremiumPurchase premiumPurchase = new PremiumPurchase(orderId, seller);
        premiumPurchaseDAO.save(premiumPurchase);

        Map<String, Object> params = new HashMap<>();
        params.put("action", "pay");
        params.put("version", "3");
        params.put("public_key", publicKey);
        params.put("amount", 100);
        params.put("currency", "UAH");
        params.put("description", "Buying premium account");
        params.put("order_id", orderId);

        // Encoding the json string with the base64_encode function
        String json_string = "{\"public_key\":\"" + publicKey + "\",\"version\":\"3\",\"action\":\"pay\",\"amount\":\"100\",\"currency\":\"UAH\",\"description\":\"Купівля преміум акаунту\",\"order_id\":\"" + orderId + "\"}";
        String data = base64_encode(json_string.getBytes(StandardCharsets.UTF_8));

        // Forming a signature
        String sign_string = privateKey + data + privateKey;
        String signature = base64_encode(sha1(sign_string));

        // Sending a request to the LiqPay API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("data", data);
        body.add("signature", signature);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange("https://www.liqpay.ua/api/3/checkout", HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.FOUND) {
            HttpHeaders responseHeaders = response.getHeaders();
            String redirectLocation = responseHeaders.getFirst("Location");

            String paymentStatus = getPaymentStatus(redirectLocation);

            if (paymentStatus != null) {
                return new ResponseEntity<>("Payment status: " + response.getStatusCode() + " You orderId: " + orderId + " Make payment using the following link: " + response.getHeaders().getFirst("Location") + " Use the following test data:  Cards for testing\n" +
                        "\n" +
                        "4000000000003055 Successful payment with CVV\n" +
                        "4000000000000002 Unsuccessful payment." + "\n" + "After that, confirm the payment using your \"orderId\" Post a request to" + "apiUrl/confirmation-of-payment/orderId/{orderId}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Payment status: unknown", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private String getPaymentStatus(String redirectLocation) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(redirectLocation, String.class);
        String responseBody = response.getBody();
        return responseBody;
    }


    @Override
    public ResponseEntity<String> getPayPaymentStatus(String orderId, Principal principal) {
        String username = principal.getName();
        Seller seller = sellerDAO.findSellerByUsername(username);
        try {
            // Constructing request parameters
            Map<String, Object> params = new HashMap<>();
            params.put("action", "status");
            params.put("version", "3");
            params.put("public_key", publicKey);
            params.put("order_id", orderId);

            // Encoding JSON string using base64_encode function
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(params);
            String data = base64_encode(jsonStr.getBytes(StandardCharsets.UTF_8));

            // Generating signature
            String signStr = privateKey + data + privateKey;
            String signature = base64_encode(sha1(signStr));

            // Sending request to LiqPay API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("data", data);
            body.add("signature", signature);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange("https://www.liqpay.ua/api/request", HttpMethod.POST, requestEntity, String.class);

            // Handling response from LiqPay API
            System.out.println(response);
            System.out.println(response.getHeaders());
            System.out.println(response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                try {
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                    String status = (String) responseMap.get("status");
                    if (Objects.equals(status, "success")) {
                        seller.setAccountType(AccountType.PREMIUM);
                        sellerDAO.save(seller);
                        return new ResponseEntity<>("Payment status: " + status + "." + " Congratulations your type account is Premium )", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Payment status: " + status, HttpStatus.OK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>("Error processing response", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Error requesting LiqPay API", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JSON processing error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 12);
    }

    private String base64_encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    private byte[] sha1(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

@Override
    public ResponseEntity<String> getOrderIdBySellerId(Integer sellerId) {
        PremiumPurchase premiumPurchase = premiumPurchaseDAO.findPremiumPurchaseBySellerId(sellerId);

        if (premiumPurchase != null) {
            String orderId = premiumPurchase.getOrderId();
            if (orderId != null) {
                return new ResponseEntity<>(orderId, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("orderId NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
