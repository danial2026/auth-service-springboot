package com.springboot.server.authenticationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    private String fullName;

    private Date birthday;

    private Set<AddressEntity> addressEntities;

    @Size(max=70)
    private String bio;
}
