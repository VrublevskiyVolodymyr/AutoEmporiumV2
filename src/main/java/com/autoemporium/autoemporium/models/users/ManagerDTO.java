package com.autoemporium.autoemporium.models.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {
    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "password cannot be empty")
    private String password;

    @NotBlank(message = "managerFirstname cannot be empty")
    private String userFirstname;

    @NotBlank(message = "managerLastname cannot be empty")
    private String userLastname;

    @NotBlank(message = "phoneNumber cannot be empty")
    private String phoneNumber;
}
