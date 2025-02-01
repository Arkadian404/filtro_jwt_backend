package com.ark.security.service.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ark.security.exception.AppException;
import com.ark.security.mapper.UserMapper;
import com.ark.security.models.Employee;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.repository.user.UserRepository;
import com.ark.security.service.CacheService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private CacheService cacheService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Test {@link UserService#changePassword(int, String, String)}.
     * <p>
     * Method under test: {@link UserService#changePassword(int, String, String)}
     */
    @Test
    @DisplayName("Test changePassword(int, String, String)")
    void testChangePassword() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1);
        employee.setStartOn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        employee.setUser(new User());

        User user = new User();
        user.setAddress("42 Main St");
        user.setCarts(new ArrayList<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setDistrict("District");
        user.setDob(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEmployee(employee);
        user.setEnabled(true);
        user.setFirstname("Jane");
        user.setId(1);
        user.setLastname("Doe");
        user.setOrders(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setProvince("Province");
        user.setReviews(new ArrayList<>());
        user.setRole(Role.USER);
        user.setTokens(new ArrayList<>());
        user.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setUserVoucherList(new ArrayList<>());
        user.setUsername("janedoe");
        user.setWard("Ward");
        user.setWishlists(new ArrayList<>());

        Employee employee2 = new Employee();
        employee2.setId(1);
        employee2.setStartOn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        employee2.setUser(user);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setCarts(new ArrayList<>());
        user2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setDistrict("District");
        user2.setDob(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("jane.doe@example.org");
        user2.setEmployee(employee2);
        user2.setEnabled(true);
        user2.setFirstname("Jane");
        user2.setId(1);
        user2.setLastname("Doe");
        user2.setOrders(new ArrayList<>());
        user2.setPassword("iloveyou");
        user2.setPhone("6625550144");
        user2.setProvince("Province");
        user2.setReviews(new ArrayList<>());
        user2.setRole(Role.USER);
        user2.setTokens(new ArrayList<>());
        user2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setUserVoucherList(new ArrayList<>());
        user2.setUsername("janedoe");
        user2.setWard("Ward");
        user2.setWishlists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(AppException.class, () -> userService.changePassword(1, "iloveyou", "iloveyou"));
        verify(userRepository).findById(eq(1));
    }
}
