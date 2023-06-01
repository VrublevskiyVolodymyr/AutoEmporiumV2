package com.autoemporium.autoemporium.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ClientDTO {
    private String userFirstname;
    private String userLastname;
    private String username;
    private String password;
}
