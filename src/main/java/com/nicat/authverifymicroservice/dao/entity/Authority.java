package com.nicat.authverifymicroservice.dao.entity;

import com.nicat.authverifymicroservice.model.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "authority")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    Roles roles;

    @ManyToMany(mappedBy = "authorities")
    List<User> users;
}