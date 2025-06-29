package com.example.demo.repositories;

import com.example.demo.models.Customer;
import com.example.demo.models.Contest;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ContestRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    private ContestRepository contestRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
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

        Role role1ToCreate = new Role();
        role1ToCreate.setName("role1");
        role1ToCreate.setPermissions(setPermissions1);

        Role role2ToCreate = new Role();
        role2ToCreate.setName("role2");
        role2ToCreate.setPermissions(setPermissions2);

        Role role1 = roleRepository.save(role1ToCreate);
        Role role2 = roleRepository.save(role1ToCreate);

        CustomerDetails customerDetails1 = new CustomerDetails();
        customerDetails1.setFirstName("John");
        customerDetails1.setSecondName("Doe");
        customerDetails1.setDateOfBirth("1990-05-15");
        customerDetails1.setPhoneNumber("+123456789");
        customerDetails1.setEmail("john.doe@example.com");
        customerDetails1.setOriginCountry("USA");

        CustomerDetails customerDetails2 = new CustomerDetails();
        customerDetails2.setFirstName("John2");
        customerDetails2.setSecondName("Doe2");
        customerDetails2.setDateOfBirth("1990-05-16");
        customerDetails2.setPhoneNumber("+123456780");
        customerDetails2.setEmail("john2.doe@example.com");
        customerDetails2.setOriginCountry("USA2");

        Customer customer1ToCreate = new Customer();
        customer1ToCreate.setLogin("login1");
        customer1ToCreate.setPassword("password1");
        customer1ToCreate.setRole(role1);
        customer1ToCreate.setCustomerDetails(customerDetails1);

        Customer customer2ToCreate = new Customer();
        customer2ToCreate.setLogin("login2");
        customer2ToCreate.setPassword("password2");
        customer2ToCreate.setRole(role2);
        customer2ToCreate.setCustomerDetails(customerDetails2);

        customer1 = customerRepository.save(customer1ToCreate);
        customer2 = customerRepository.save(customer2ToCreate);
    }

    @Test
    public void testCreateContest() {
        Contest contest = new Contest();

        contest.setName("Contest1");
        contest.setStartMessage("The contest has started!");
        contest.setEndMessage("The contest has ended.");
        contest.setIsStarted(true);
        contest.setIsStopped(false);
        contest.setDurationInSeconds(3600L);
        contest.setOwner(customer1);

        Contest contestCreated = contestRepository.save(contest);
        Contest retrievedContest = contestRepository.findById(contestCreated.getId()).orElse(null);

        assertNotNull(retrievedContest);

        assertEquals(3600L, retrievedContest.getDurationInSeconds());
        assertEquals("login1", retrievedContest.getOwner().getLogin());

        assertEquals("Contest1", retrievedContest.getName());
        assertEquals("The contest has started!", retrievedContest.getStartMessage());
        assertEquals("The contest has ended.", retrievedContest.getEndMessage());
        assertEquals(true, retrievedContest.getIsStarted());
        assertEquals(false, retrievedContest.getIsStopped());

    }

    @Test
    public void testGetContest() {
        Contest contest = new Contest();

        contest.setName("Contest1");
        contest.setStartMessage("The contest has started!");
        contest.setEndMessage("The contest has ended.");
        contest.setIsStarted(true);
        contest.setIsStopped(false);
        contest.setDurationInSeconds(3600L);
        contest.setOwner(customer1);

        Contest contestCreated = contestRepository.save(contest);
        Contest retrievedContest = contestRepository.findByName(contestCreated.getName());

        assertNotNull(retrievedContest);

        assertEquals(3600L, retrievedContest.getDurationInSeconds());
        assertEquals("login1", retrievedContest.getOwner().getLogin());

        assertEquals("Contest1", retrievedContest.getName());
        assertEquals("The contest has started!", retrievedContest.getStartMessage());
        assertEquals("The contest has ended.", retrievedContest.getEndMessage());
        assertEquals(true, retrievedContest.getIsStarted());
        assertEquals(false, retrievedContest.getIsStopped());
    }

    @Test
    public void testUpdateContest() {
        Contest contest = new Contest();

        contest.setName("Contest1");
        contest.setStartMessage("The contest1 has started!");
        contest.setEndMessage("The contest1 has ended.");
        contest.setIsStarted(true);
        contest.setIsStopped(false);
        contest.setDurationInSeconds(3600L);
        contest.setOwner(customer1);

        Contest contestCreated = contestRepository.save(contest);

        contestCreated.setName("Contest2");
        contestCreated.setStartMessage("The contest2 has started!");
        contestCreated.setEndMessage("The contest2 has ended.");
        contestCreated.setIsStarted(true);
        contestCreated.setIsStopped(true);
        contestCreated.setDurationInSeconds(3700L);
        contestCreated.setOwner(customer2);

        Contest contestUpdated = contestRepository.save(contestCreated);

        Contest retrievedContest = contestRepository.findById(contestUpdated.getId()).orElse(null);

        assertNotNull(retrievedContest);

        assertEquals(3700L, retrievedContest.getDurationInSeconds());
        assertEquals("login2", retrievedContest.getOwner().getLogin());

        assertEquals("Contest2", retrievedContest.getName());
        assertEquals("The contest2 has started!", retrievedContest.getStartMessage());
        assertEquals("The contest2 has ended.", retrievedContest.getEndMessage());
        assertEquals(true, retrievedContest.getIsStarted());
        assertEquals(true, retrievedContest.getIsStopped());
    }

    @Test
    public void testDeleteContest() {
        Contest contest = new Contest();

        contest.setName("Contest1");
        contest.setStartMessage("The contest has started!");
        contest.setEndMessage("The contest has ended.");
        contest.setIsStarted(true);
        contest.setIsStopped(false);
        contest.setDurationInSeconds(3600L);
        contest.setOwner(customer1);

        Contest contestCreated = contestRepository.save(contest);
        contestRepository.deleteById(contestCreated.getId());

        Contest retrievedContest = contestRepository.findById(contestCreated.getId()).orElse(null);
        Customer retrievedCustomer = customerRepository.findById(contestCreated.getOwner().getId()).orElse(null);

        assertNull(retrievedContest);

        assertNotNull(retrievedCustomer);
        assertEquals("login1", retrievedCustomer.getLogin());
    }

}

