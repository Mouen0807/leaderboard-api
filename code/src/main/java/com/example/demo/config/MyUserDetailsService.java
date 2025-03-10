package com.example.demo.config;

import com.example.demo.models.Customer;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> optCustomer = customerRepository.findById(UUID.fromString(username));

        if(optCustomer.isPresent()){
            return new User(optCustomer.get().getId().toString(),
                    optCustomer.get().getPassword(),
                    roleService.mapRolesToAuthorities(optCustomer.get().getRole()));
        }

        return null;
    }

}
