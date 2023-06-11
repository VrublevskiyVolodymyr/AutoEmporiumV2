package com.autoemporium.autoemporium.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ManagerDTO {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
}
