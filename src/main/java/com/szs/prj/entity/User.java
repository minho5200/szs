package com.szs.prj.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
@Getter
@ToString
public class User {

    @Id
    private String userId;

    private String password;

    private String name;

    @Column(nullable = false, unique = true)
    private String regNo;
}

