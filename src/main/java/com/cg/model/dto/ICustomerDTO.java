package com.cg.model.dto;

import com.cg.model.LocationRegion;

public interface ICustomerDTO {

    long getId();
    String getFullName();
    String getEmail();
    String getPhone();
    String getBalance();
    LocationRegion getLocationRegion();
//    LocationRegionDTO getLocationRegion();
//    String getProvinceName();
//    String getDistrictName();
//    String getWardName();
//    String getAddress();
}
