package com.cg.model.dto;

import com.cg.model.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class  CustomerDTO {

    private long id;
    private String fullName;
    private String email;
    private String phone;
    private BigDecimal balance;
    private LocationRegionDTO locationRegion;

    public CustomerDTO(long id, String fullName, String email, String phone, BigDecimal balance, LocationRegion locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }
}
