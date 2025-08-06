package com.nicat.authverifymicroservice.dao.entity;

import com.nicat.authverifymicroservice.model.enums.Roles;
import com.nicat.authverifymicroservice.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;

    String password;

    @Enumerated(EnumType.STRING)
    Roles roles;

    String phoneNumber;
    String email;

    @Enumerated(EnumType.STRING)
    Status status;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities;



    @OneToMany(mappedBy = "user")
    List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getRoles()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}