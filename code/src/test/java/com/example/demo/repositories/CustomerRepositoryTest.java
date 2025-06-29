package com.example.demo.repositories;

import com.example.demo.models.Customer;
import com.example.demo.models.CustomerDetails;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    private Role role1;
    private Role role2;

    @BeforeEach
    void setUp() {
        Role role1ToCreate = new Role();
        role1ToCreate.setName("role1");

        Role role2ToCreate = new Role();
        role2ToCreate.setName("role2");

        Set<Permission> setPermissions1 = new HashSet<>();
        Set<Permission> setPermissions2 = new HashSet<>();

        Permission permission1 = new Permission();
        permission1.setName("permission1");
        permission1.setDescription("permission1");

        Permission permission2 = new Permission();
        permission2.setName("permission2");
        permission2.setDescription("permission2");

        Permission permission3 = new Permission();
        permission3.setName("permission3");
        permission3.setDescription("permission3");

        Permission permission1Created = permissionRepository.save(permission1);
        Permission permission2Created = permissionRepository.save(permission2);
        Permission permission3Created = permissionRepository.save(permission3);

        setPermissions1.add(permission1Created);
        setPermissions1.add(permission2Created);
        setPermissions2.add(permission3Created);

        role1ToCreate.setPermissions(setPermissions1);
        this.role1 = roleRepository.save(role1ToCreate);

        role2ToCreate.setPermissions(setPermissions2);
        this.role2 = roleRepository.save(role2ToCreate);
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("John");
        customerDetails.setSecondName("Doe");
        customerDetails.setDateOfBirth("1990-05-15");
        customerDetails.setPhoneNumber("+123456789");
        customerDetails.setEmail("john.doe@example.com");
        customerDetails.setOriginCountry("USA");

        customer.setLogin("login1");
        customer.setPassword("password1");
        customer.setCustomerDetails(customerDetails);
        customer.setRole(this.role1);

        Customer customerCreated = customerRepository.save(customer);

        Customer retrievedCustomer = customerRepository.findById(customerCreated.getId()).orElse(null);

        assertNotNull(retrievedCustomer);

        assertEquals("login1", retrievedCustomer.getLogin());
        assertEquals("password1", retrievedCustomer.getPassword());
        assertEquals("role1", retrievedCustomer.getRole().getName());

        assertEquals("John", retrievedCustomer.getCustomerDetails().getFirstName());
        assertEquals("Doe", retrievedCustomer.getCustomerDetails().getSecondName());
        assertEquals("1990-05-15", retrievedCustomer.getCustomerDetails().getDateOfBirth());
        assertEquals("+123456789", retrievedCustomer.getCustomerDetails().getPhoneNumber());
        assertEquals("john.doe@example.com", retrievedCustomer.getCustomerDetails().getEmail());
        assertEquals("USA", retrievedCustomer.getCustomerDetails().getOriginCountry());

    }


    @Test
    public void testGetCustomer() {
        Customer customer = new Customer();

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("John");
        customerDetails.setSecondName("Doe");
        customerDetails.setDateOfBirth("1990-05-15");
        customerDetails.setPhoneNumber("+123456789");
        customerDetails.setEmail("john.doe@example.com");
        customerDetails.setOriginCountry("USA");

        customer.setLogin("login1");
        customer.setPassword("password1");
        customer.setCustomerDetails(customerDetails);
        customer.setRole(this.role1);

        Customer customerCreated = customerRepository.save(customer);

        Customer retrievedCustomer = customerRepository.findByLogin(customerCreated.getLogin());

        assertNotNull(retrievedCustomer);

        assertEquals("login1", retrievedCustomer.getLogin());
        assertEquals("password1", retrievedCustomer.getPassword());
        assertEquals("role1", retrievedCustomer.getRole().getName());

        assertEquals("John", retrievedCustomer.getCustomerDetails().getFirstName());
        assertEquals("Doe", retrievedCustomer.getCustomerDetails().getSecondName());
        assertEquals("1990-05-15", retrievedCustomer.getCustomerDetails().getDateOfBirth());
        assertEquals("+123456789", retrievedCustomer.getCustomerDetails().getPhoneNumber());
        assertEquals("john.doe@example.com", retrievedCustomer.getCustomerDetails().getEmail());
        assertEquals("USA", retrievedCustomer.getCustomerDetails().getOriginCountry());
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = new Customer();

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("John");
        customerDetails.setSecondName("Doe");
        customerDetails.setDateOfBirth("1990-05-15");
        customerDetails.setPhoneNumber("+123456789");
        customerDetails.setEmail("john.doe@example.com");
        customerDetails.setOriginCountry("USA");

        customer.setLogin("login1");
        customer.setPassword("password1");
        customer.setCustomerDetails(customerDetails);
        customer.setRole(this.role2);

        Customer customerCreated = customerRepository.save(customer);

        CustomerDetails customerDetails2 = new CustomerDetails();
        customerDetails2.setFirstName("John2");
        customerDetails2.setSecondName("Doe2");
        customerDetails2.setDateOfBirth("1990-05-16");
        customerDetails2.setPhoneNumber("+123456780");
        customerDetails2.setEmail("john2.doe@example.com");
        customerDetails2.setOriginCountry("USA2");

        customerCreated.setLogin("login2");
        customerCreated.setPassword("password2");
        customerCreated.setCustomerDetails(customerDetails2);
        customerCreated.setRole(this.role2);

        Customer customerUpdated = customerRepository.save(customerCreated);

        Customer retrievedCustomer = customerRepository.findById(customerUpdated.getId()).orElse(null);

        assertNotNull(retrievedCustomer);

        assertEquals("login2", retrievedCustomer.getLogin());
        assertEquals("password2", retrievedCustomer.getPassword());
        assertEquals("role2", retrievedCustomer.getRole().getName());

        assertEquals("John2", retrievedCustomer.getCustomerDetails().getFirstName());
        assertEquals("Doe2", retrievedCustomer.getCustomerDetails().getSecondName());
        assertEquals("1990-05-16", retrievedCustomer.getCustomerDetails().getDateOfBirth());
        assertEquals("+123456780", retrievedCustomer.getCustomerDetails().getPhoneNumber());
        assertEquals("john2.doe@example.com", retrievedCustomer.getCustomerDetails().getEmail());
        assertEquals("USA2", retrievedCustomer.getCustomerDetails().getOriginCountry());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("John");
        customerDetails.setSecondName("Doe");
        customerDetails.setDateOfBirth("1990-05-15");
        customerDetails.setPhoneNumber("+123456789");
        customerDetails.setEmail("john.doe@example.com");
        customerDetails.setOriginCountry("USA");

        customer.setLogin("login1");
        customer.setPassword("password1");
        customer.setCustomerDetails(customerDetails);
        customer.setRole(this.role1);

        Customer customerCreated = customerRepository.save(customer);
        UUID customerDetailsId = customerCreated.getCustomerDetails().getId();
        customerRepository.deleteById(customerCreated.getId());

        Customer retrievedCustomer = customerRepository.findById(customerCreated.getId()).orElse(null);
        Role retrievedRole = roleRepository.findById(this.role1.getId()).orElse(null);
        CustomerDetails retrievedcustomerDetails= customerDetailsRepository.findById(customerDetailsId).orElse(null);

        assertNull(retrievedCustomer);
        assertNull(retrievedcustomerDetails);
        assertNotNull(retrievedRole);
    }

}

