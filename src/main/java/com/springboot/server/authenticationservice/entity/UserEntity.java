package com.springboot.server.authenticationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class UserEntity {

    @Id
    @Indexed(unique = true)
    private String id;

    @NotBlank
    @Size(min = 4, max = 15)
    @Field("username")
    @Indexed(unique = true)
    private String username;

    @NotBlank
    @Size(max = 32)
    @JsonIgnore
    @Field("password")
    private String password;

    @NotBlank
    @Size(min = 11, max = 13)
    @Field("phone_number")
    @Indexed(unique = true)
    private String phoneNumber;

    @Field("verification_code")
    private String verificationCode;

    @Field("verification_code_expiration_date")
    private LocalDateTime verificationCodeExpirationDate;

    @Field("recovery_code")
    private String recoveryCode;

    @Field("recovery_code_expiration_date")
    private LocalDateTime recoveryCodeExpirationDate;

    @Field("enabled")
    private boolean activated;

    @Field("profile")
    private ProfileEntity userProfileEntity;

    @Field("roles")
    private Set<RoleEntity> roleEntities;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;

    @LastModifiedBy
    @Field("updated_by")
    private String updatedBy;

    public UserEntity(UserEntity user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.phoneNumber = user.phoneNumber;
        this.verificationCode = user.verificationCode;
        this.verificationCodeExpirationDate = user.verificationCodeExpirationDate;
        this.recoveryCode = user.recoveryCode;
        this.recoveryCodeExpirationDate = user.recoveryCodeExpirationDate;
        this.activated = user.activated;
        this.userProfileEntity = user.userProfileEntity;
        this.roleEntities = user.roleEntities;
        this.createdBy = user.getCreatedBy();
        this.createdAt = user.getCreatedAt();
        this.updatedBy = user.getUpdatedBy();
        this.updatedAt = user.getUpdatedAt();
    }
}
