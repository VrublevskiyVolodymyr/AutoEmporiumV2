package com.autoemporium.autoemporium.models.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SellerDTO {
    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "password cannot be empty")
    private String password;

    @NotBlank(message = "userFirstname cannot be empty")
    private String userFirstname;

    @NotBlank(message = "userLastname cannot be empty")
    private String userLastname;

    @NotBlank(message = "phoneNumber cannot be empty")
    private String phoneNumber;
}
