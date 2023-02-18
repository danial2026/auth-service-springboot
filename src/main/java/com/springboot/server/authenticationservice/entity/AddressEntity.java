package com.springboot.server.authenticationservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AddressEntity {

    private String id;

    private String country;

    private String city;

    private String zipCode;

    private String streetName;

    private int buildingNumber;
}

