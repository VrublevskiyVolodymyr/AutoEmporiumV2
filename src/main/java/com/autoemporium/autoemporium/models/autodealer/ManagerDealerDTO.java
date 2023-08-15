package com.autoemporium.autoemporium.models.autodealer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDealerDTO {
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
