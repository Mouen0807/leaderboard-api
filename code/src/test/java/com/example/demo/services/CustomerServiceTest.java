package com.example.demo.services;

import com.example.demo.dtos.CustomerDetailsDto;
import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.CustomerLoginDto;
import com.example.demo.mappers.CustomerDetailsMapper;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.models.Customer;
import com.example.demo.models.CustomerDetails;
import com.example.demo.models.Permission;
import com.example.demo.models.Role;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerDetailsMapper customerDetailsMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldReturnCustomerWhenCustomerLoginExists(){
        // Entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();


        UUID customerDetailsId = UUID.randomUUID();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        // Dto
        CustomerDetailsDto customerDetailsDto = CustomerDetailsDto.builder()
                .id(customerDetailsId.toString())
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-20")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetailsDto)
                .role("ADMIN-TEST")
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Mockito.when(customerRepository.findByLogin(customer.getLogin())).thenReturn(customer);

        Mockito.when(customerMapper.convertToDto(customer)).thenReturn(customerDto);

        Optional<CustomerDto> result = customerService.findCustomerByLogin(customer.getLogin());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getRole());
        Assertions.assertEquals(customerId.toString(), result.get().getId());
        Assertions.assertEquals("jean.dupont", result.get().getLogin());

    }

    @Test
    public void shouldNotReturnCustomerWhenCustomerLoginDoesntExist(){
        String login = "123ac";

        Mockito.when(customerRepository.findByLogin(login)).thenReturn(null);

        Optional<CustomerDto> result = customerService.findCustomerByLogin(login);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void shouldReturnCustomerWhenCustomerGuidExists(){
        // Entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();


        UUID customerDetailsId = UUID.randomUUID();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        // Dto
        CustomerDetailsDto customerDetailsDto = CustomerDetailsDto.builder()
                .id(customerDetailsId.toString())
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-20")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetailsDto)
                .role("ADMIN-TEST")
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Optional<Customer> optCustomer = Optional.of(customer);

        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(optCustomer);

        Mockito.when(customerMapper.convertToDto(optCustomer.get())).thenReturn(customerDto);

        Optional<CustomerDto> result = customerService.findCustomerByGuid(customerId.toString());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getRole());
        Assertions.assertEquals(customerId.toString(), result.get().getId());
        Assertions.assertEquals("jean.dupont", result.get().getLogin());

    }

    @Test
    public void shouldNotReturnCustomerWhenCustomerGuidDoesntExist(){
        UUID customerId = UUID.randomUUID();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.findCustomerByGuid(customerId.toString());

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotReturnCustomerWhenCustomerGuidIsNotValid(){
        String customerId = "123ac";

        Optional<CustomerDto> result = customerService.findCustomerByGuid(customerId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotUpdateCustomerLoginWhenCustomerGuidIsNotValid(){
        String customerId = "123ac";
        CustomerLoginDto customerLoginDto = null;

        Optional<CustomerDto> result = customerService.updateCustomerLogin(customerId, customerLoginDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotUpdateCustomerLoginWhenCustomerGuidDoesntExists(){
        UUID customerId = UUID.randomUUID();
        CustomerLoginDto customerLoginDto = null;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.updateCustomerLogin(customerId.toString(), customerLoginDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdateCustomerLoginWhenCustomerGuidExists(){
        // Entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        UUID customerDetailsId = UUID.randomUUID();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Customer customerUpdated = Customer.builder()
                .id(customerId)
                .login("jean.dupont2")
                .password("encodedNewSecuredPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Optional<Customer> optCustomer = Optional.of(customer);

        // dtos
        CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
                .login("jean.dupont2")
                .password("newSecuredPassword123")
                .build();

        CustomerDetailsDto customerDetailsDto = CustomerDetailsDto.builder()
                .id(customerDetailsId.toString())
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        CustomerDto customerUpdatedDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont2")
                .password("encodedNewSecuredPassword123")
                .customerDetails(customerDetailsDto)
                .role("ADMIN-TEST")
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(optCustomer);

        Mockito.when(passwordEncoder.encode(customerLoginDto.getPassword())).thenReturn(customerUpdated.getPassword());

        Mockito.when(customerRepository.save(customer)).thenReturn(customerUpdated);

        Mockito.when(customerMapper.convertToDto(customerUpdated)).thenReturn(customerUpdatedDto);

        Optional<CustomerDto> result = customerService.updateCustomerLogin(customerId.toString(), customerLoginDto);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getRole());
        Assertions.assertEquals(customerId.toString(), result.get().getId());
        Assertions.assertEquals("jean.dupont2", result.get().getLogin());
        Assertions.assertEquals("encodedNewSecuredPassword123", result.get().getPassword());
    }


    @Test
    public void shouldNotUpdateCustomerRoleWhenCustomerGuidIsNotValid(){
        String customerId = "123ac";
        Role role = null;

        Optional<CustomerDto> result = customerService.updateRole(customerId, role);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotUpdateCustomerRoleWhenCustomerGuidDoesntExists(){
        UUID customerId = UUID.randomUUID();
        Role role = null;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.updateRole(customerId.toString(), role);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdateCustomerRoleWhenCustomerGuidExists() {
        // Entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        UUID customerDetailsId = UUID.randomUUID();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Role roleUpdated = Role.builder()
                .id(2L)
                .name("ADMIN-TEST2")
                .permissions(permissionSet)
                .build();

        Customer customerUpdated = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetails)
                .role(roleUpdated)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Optional<Customer> optCustomer = Optional.of(customer);

        // dtos
        CustomerLoginDto customerLoginDto = CustomerLoginDto.builder()
                .login("jean.dupont2")
                .password("newSecuredPassword123")
                .build();

        CustomerDetailsDto customerDetailsDto = CustomerDetailsDto.builder()
                .id(customerDetailsId.toString())
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        CustomerDto customerUpdatedDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetailsDto)
                .role(roleUpdated.getName())
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(optCustomer);

        Mockito.when(customerRepository.save(customer)).thenReturn(customerUpdated);

        Mockito.when(customerMapper.convertToDto(customerUpdated)).thenReturn(customerUpdatedDto);

        Optional<CustomerDto> result = customerService.updateRole(customerId.toString(), roleUpdated);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST2", result.get().getRole());
        Assertions.assertEquals(customerId.toString(), result.get().getId());
        Assertions.assertEquals("jean.dupont", result.get().getLogin());
        Assertions.assertEquals("securedPassword123", result.get().getPassword());
    }

    @Test
    public void shouldNotUpdateCustomerDetailsWhenCustomerGuidIsNotValid(){
        String customerId = "123ac";
        CustomerDetailsDto custDetailsDto = null;

        Optional<CustomerDto> result = customerService.updateCustomerDetails(customerId, custDetailsDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotUpdateCustomerDetailsWhenCustomerGuidDoesntExists(){
        UUID customerId = UUID.randomUUID();
        CustomerDetailsDto custDetailsDto = null;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.updateCustomerDetails(customerId.toString(), custDetailsDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdateCustomerDetailsWhenCustomerGuidExists() {
        // Entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        UUID customerDetailsId = UUID.randomUUID();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        CustomerDetails customerDetailsUpdated = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean2")
                .secondName("Dupont2")
                .dateOfBirth("1990-05-11")
                .phoneNumber("+33612345679")
                .email("jean2.dupont2@example.com")
                .originCountry("France")
                .build();

        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Customer customerUpdated = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetailsUpdated)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Optional<Customer> optCustomer = Optional.of(customer);

        // dtos
        CustomerDetailsDto customerDetailsUpdatedDto = CustomerDetailsDto.builder()
                .id(customerDetailsId.toString())
                .firstName("Jean2")
                .secondName("Dupont2")
                .dateOfBirth("1990-05-11")
                .phoneNumber("+33612345679")
                .email("jean2.dupont2@example.com")
                .originCountry("France")
                .build();

        CustomerDto customerUpdatedDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetailsUpdatedDto)
                .role(role.getName())
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(optCustomer);

        Mockito.when(customerRepository.save(customer)).thenReturn(customerUpdated);

        Mockito.when(customerDetailsMapper.convertToEntity(customerDetailsUpdatedDto)).thenReturn(customerDetailsUpdated);

        Mockito.when(customerMapper.convertToDto(customerUpdated)).thenReturn(customerUpdatedDto);

        Optional<CustomerDto> result = customerService.updateCustomerDetails(customerId.toString(), customerDetailsUpdatedDto);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getRole());
        Assertions.assertEquals(customerId.toString(), result.get().getId());
        Assertions.assertEquals("Jean2", result.get().getCustomerDetails().getFirstName());
        Assertions.assertEquals("Dupont2", result.get().getCustomerDetails().getSecondName());
    }

    @Test
    public void shouldNotSaveCustomerWhenCustomerLoginAlreadyExists(){

        UUID customerId = UUID.randomUUID();

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(null)
                .role(null)
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(null)
                .role(null)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Mockito.when(customerRepository.findByLogin(customerDto.getLogin())).thenReturn(customer);

        Optional<Customer> result = customerService.saveCustomer(customerDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotSaveCustomerWhenRoleDoesntExist(){
        //dtos
        UUID customerId = UUID.randomUUID();

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(null)
                .role(null)
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        //entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(null)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Mockito.when(customerRepository.findByLogin(customerDto.getLogin())).thenReturn(null);

        Mockito.when(roleRepository.findByName(customerDto.getRole())).thenReturn(null);

        Optional<Customer> result = customerService.saveCustomer(customerDto);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldSaveCustomer(){
        // Entity
        Permission permission1 = Permission.builder()
                .id(1L)
                .name("permission1")
                .description("permission1")
                .build();

        Permission permission2 = Permission.builder()
                .id(2L)
                .name("permission2")
                .description("permission2")
                .build();

        Set<Permission> permissionSet = Set.of(permission1, permission2);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN-TEST")
                .permissions(permissionSet)
                .build();

        UUID customerDetailsId = UUID.randomUUID();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .id(customerDetailsId)
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        UUID customerId = UUID.randomUUID();

        Customer customerToSave = Customer.builder()
                .login("jean.dupont")
                .password("encryptedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("encryptedPassword123")
                .customerDetails(customerDetails)
                .role(role)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Optional<Customer> optCustomer = Optional.of(customer);

        // dtos
        CustomerDetailsDto customerDetailsDto = CustomerDetailsDto.builder()
                .id(customerDetailsId.toString())
                .firstName("Jean")
                .secondName("Dupont")
                .dateOfBirth("1990-05-10")
                .phoneNumber("+33612345678")
                .email("jean.dupont@example.com")
                .originCountry("France")
                .build();

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("securedPassword123")
                .customerDetails(customerDetailsDto)
                .role(role.getName())
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Mockito.when(customerRepository.findByLogin(customerDto.getLogin())).thenReturn(null);

        Mockito.when(roleRepository.findByName(customerDto.getRole())).thenReturn(role);

        Mockito.when(passwordEncoder.encode(customerDto.getPassword())).thenReturn(customer.getPassword());

        Mockito.when(customerMapper.convertToEntity(customerDto)).thenReturn(customerToSave);

        Mockito.when(customerRepository.save(customerToSave)).thenReturn(customer);

        Optional<Customer> result = customerService.saveCustomer(customerDto);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("ADMIN-TEST", result.get().getRole().getName());
        Assertions.assertEquals(customerId.toString(), result.get().getId().toString());
        Assertions.assertEquals("Jean", result.get().getCustomerDetails().getFirstName());
        Assertions.assertEquals("Dupont", result.get().getCustomerDetails().getSecondName());
    }


    @Test
    public void shouldNotLoginCustomerWhenCustomerLoginDoesntExist(){

        UUID customerId = UUID.randomUUID();

        String login = "jean.dupont";
        String rawPassword = "securedPassword";

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("encryptedPassword123")
                .customerDetails(null)
                .role(null)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        Mockito.when(customerRepository.findByLogin(login)).thenReturn(null);

        Optional<CustomerDto> result = customerService.loginCustomer(login,rawPassword);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldLoginCustomerWhenCustomerLoginExist(){

        UUID customerId = UUID.randomUUID();

        String login = "jean.dupont";
        String rawPassword = "securedPassword";

        Customer customer = Customer.builder()
                .id(customerId)
                .login("jean.dupont")
                .password("encryptedPassword123")
                .customerDetails(null)
                .role(null)
                .createdAt(LocalDateTime.parse("2024-01-01T12:00:00"))
                .updatedAt(LocalDateTime.parse("2024-02-01T08:00:00"))
                .build();

        CustomerDto customerDto = CustomerDto.builder()
                .id(customerId.toString())
                .login("jean.dupont")
                .password("encryptedPassword123")
                .customerDetails(null)
                .role(null)
                .createdAt("2024-01-01T12:00:00")
                .updatedAt("2024-02-01T08:00:00")
                .build();

        Mockito.when(customerRepository.findByLogin(login)).thenReturn(customer);

        Mockito.when(passwordEncoder.matches(rawPassword, customer.getPassword())).thenReturn(true);

        Mockito.when(customerMapper.convertToDto(customer)).thenReturn(customerDto);

        Optional<CustomerDto> result = customerService.loginCustomer(login,rawPassword);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(customerId.toString(), result.get().getId());
        Assertions.assertEquals("jean.dupont", result.get().getLogin());
        Assertions.assertEquals("encryptedPassword123", result.get().getPassword());
    }
}
