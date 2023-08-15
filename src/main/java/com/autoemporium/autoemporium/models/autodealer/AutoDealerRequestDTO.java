package com.autoemporium.autoemporium.models.autodealer;

import com.autoemporium.autoemporium.models.users.BuyerDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AutoDealerRequestDTO {
    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "location cannot be empty")
    private String location;

    private List<AdministratorDealerDTO> admins;

    private List<ManagerDealerDTO> managers;

    private List<SellerDealerDTO> sellers;

    private List<MechanicDealerDTO> mechanics;

    private List<BuyerDTO> buyers;
}
