package com.ark.security.service.user;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Employee;
import com.ark.security.models.user.User;
import com.ark.security.repository.user.EmployeeRepository;
import com.ark.security.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1);
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeService.getEmployeeById(1));
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(new Employee()));

        assertNotNull(employeeService.getAllEmployees());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEmployeesEmpty() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> employeeService.getAllEmployees());
    }

    @Test
    void testSaveEmployee() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");

        Employee employee = new Employee();
        employee.setUser(user);

        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.saveEmployee(employee, user);

        verify(userRepository, times(2)).save(user);
    }

    @Test
    void testUpdateEmployee() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");

        Employee employee = new Employee();
        employee.setId(1);
        employee.setUser(user);

        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.updateEmployee(1, employee, user);

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testDeleteEmployee() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");

        Employee employee = new Employee();
        employee.setId(1);
        employee.setUser(user);

        when(employeeRepository.existsById(anyInt())).thenReturn(true);
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1)).delete(employee);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        when(employeeRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> employeeService.deleteEmployee(1));
    }

}