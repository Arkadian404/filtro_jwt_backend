package com.ark.security.service;

import com.ark.security.models.Employee;
import com.ark.security.models.user.User;
import com.ark.security.repository.EmployeeRepository;
import com.ark.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public Employee getEmployeeById(Integer id){
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    private void editEmp(User user, User newUser) {
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRole(user.getRole());
        newUser.setDob(user.getDob());
        newUser.setAddress(user.getAddress());
        newUser.setPhone(user.getPhone());
        userRepository.save(newUser);
    }

    public void saveEmployee(Employee employee, User user ){
        if(user!=null && employee!=null){
            User newUser = new User();
            editEmp(user, newUser);

            Employee newEmployee = new Employee();
            newEmployee.setStartOn(employee.getStartOn());
            newEmployee.setUser(newUser);
            employeeRepository.save(newEmployee);
            userRepository.save(newUser);
        }
    }



    public void updateEmployee(int id, Employee employee, User user){
        Optional<Employee> empOpt = employeeRepository.findById(id);
        if (empOpt.isPresent()){
            Employee oldEmployee = empOpt.get();
            User oldUser = oldEmployee.getUser();
            if(oldUser!=null){
                editEmp(user, oldUser);

                oldEmployee.setStartOn(employee.getStartOn());
                oldEmployee.setUser(oldUser);
                employeeRepository.save(oldEmployee);
                userRepository.save(oldUser);
            }
        }
    }

    public void deleteEmployee(int id){
        Optional<Employee> empOpt = employeeRepository.findById(id);
        if(empOpt.isPresent()){
            Employee employee = empOpt.get();
            User user = employee.getUser();
            if(user!=null){
                employeeRepository.delete(employee);
                userRepository.delete(user);
            }
        } else{
            throw new RuntimeException("Không tìm thấy nhân viên");
        }
    }



}
