package com.example.demo.config;

import com.example.demo.models.Customer;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
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

    private final CustomerRepository customerRepository;

    public MyUserDetailsService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = Optional.of(customerRepository.findByLogin(username));

        return new User(customer.get().getLogin(),
                customer.get().getPassword(),
                mapRolesToAuthorities(customer.get().getRole()));

    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Role role) {
        List<String> permissionNames = role.getPermissions().stream()
                .map(Permission::getName)  // Extract the 'name' field from each Permission
                .collect(Collectors.toList());

        return permissionNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
