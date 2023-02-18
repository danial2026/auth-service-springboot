package com.springboot.server.authenticationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    public final static RoleEntity USER = new RoleEntity("USER");

    public final static RoleEntity ADMIN = new RoleEntity("ADMIN");

    public final static RoleEntity SERVICE = new RoleEntity("SERVICE");

    private String name;
}

