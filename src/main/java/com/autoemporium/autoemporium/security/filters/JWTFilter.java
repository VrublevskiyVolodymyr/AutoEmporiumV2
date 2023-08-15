package com.autoemporium.autoemporium.security.filters;

import com.autoemporium.autoemporium.models.users.ResponseError;
import com.autoemporium.autoemporium.services.advertisementService.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;


@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");// Bearer tokenEMAILtoken

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.replace("Bearer ", "");
            String userEmail = jwtService.extractUsername(token);

            if (userEmail != null && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authenticationToken);
                }
            }

        } catch (IOException | UsernameNotFoundException | ServletException e) {
            throw new RuntimeException(e);
        } catch (ExpiredJwtException e) {
            response.setHeader("TokenError", "token dead");
            response.resetBuffer();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ResponseError responseError = ResponseError
                    .builder()
                    .error("token is dead")
                    .code(403)
                    .build();

            response
                    .getOutputStream()
                    .write(new ObjectMapper().writeValueAsBytes(responseError));

            return;

        }
        filterChain.doFilter(request, response);
    }
}


//@Component
//@AllArgsConstructor
//
//public class JWTFilter extends OncePerRequestFilter {
////    private ClientDAO clientDAO;
//    private UserDAO userDAO;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorization = request.getHeader("Authorization");
//        if (authorization != null && authorization.startsWith("Bearer ")) {
//            String token = authorization.replace("Bearer ", "");
//            System.out.println("token-" + token);
//
//            String decodedData = Jwts.parser()
//                    .setSigningKey("okten".getBytes(StandardCharsets.UTF_8))
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
//            System.out.println("decodedData-" + decodedData);
//            User user = userDAO.findByUsername(decodedData);
//            System.out.println("user-" + user);
//            if (user != null) {
//                SecurityContextHolder
//                        .getContext()
//                        .setAuthentication(new UsernamePasswordAuthenticationToken(
//                                user.getUsername(),
//                                user.getPassword(),
//                                user.getAuthorities()
//                        ));
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
