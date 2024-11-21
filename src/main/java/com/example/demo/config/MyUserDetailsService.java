package com.example.demo.config;

import com.example.demo.models.CustomerLogin;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerLoginRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final CustomerLoginRepository customerLoginRepository;

    public MyUserDetailsService(CustomerLoginRepository customerLoginRepository){
        this.customerLoginRepository = customerLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomerLogin> customerLogin = customerLoginRepository.findById(username);

        return new User(customerLogin.get().getLogin(),
                customerLogin.get().getPassword(),
                mapRolesToAuthorities(customerLogin.get().getRole()));

    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Role role) {
        List<String> permissionNames = role.getPermissions().stream()
                .map(Permission::getName)  // Extract the 'name' field from each Permission
                .collect(Collectors.toList());

        return permissionNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
