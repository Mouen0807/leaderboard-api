package com.example.demo.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customerLogin")
public class CustomerLogin{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique=true,nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private CustomerDetails customerDetails;

    @OneToOne(cascade = CascadeType.ALL)
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "CustomerLogin{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", customerDetails=" + customerDetails +
                ", role=" + role +
                '}';
    }
    /*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> permissionNames = this.role.getPermissions().stream()
                .map(Permission::getName)  // Extract the 'name' field from each Permission
                .collect(Collectors.toList());

        return permissionNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    */
}
