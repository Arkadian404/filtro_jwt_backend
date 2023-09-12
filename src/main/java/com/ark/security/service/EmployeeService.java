package com.ark.security.service;

import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.Employee;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.repository.EmployeeRepository;
import com.ark.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    public Employee getEmployeeById(Integer id){
        return employeeRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy nhân viên: "+ id));
    }

    public List<Employee> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new NotFoundException("Không có nhân viên nào");
        }
        return employees;
    }

    private void addEmp(User newUser, User user) {
        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        if(userRepository.existsUserByUsername(newUser.getUsername())){
            throw new DuplicateException("Username đã tồn tại");
        }else{
            user.setUsername(newUser.getUsername());
        }
        if(userRepository.existsUserByEmail(newUser.getEmail())){
            throw new DuplicateException("Email đã tồn tại");
        }else{
            user.setEmail(newUser.getEmail());
        }
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(newUser.getRole());
        user.setDob(newUser.getDob());
        user.setAddress(newUser.getAddress());
        user.setPhone(newUser.getPhone());
        user.setEnabled(newUser.getEnabled());
        userRepository.save(user);
    }


    private void editEmp(User newUser, User user) {
        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        user.setDob(newUser.getDob());
        user.setAddress(newUser.getAddress());
        user.setPhone(newUser.getPhone());
        user.setEnabled(newUser.getEnabled());
        userRepository.save(user);
    }

    public void saveEmployee(Employee employee, User newUser ){
        if(employee!=null){
            if(newUser!=null){
                User user = new User();
                addEmp(newUser, user);

                Employee newEmployee = new Employee();
                newEmployee.setStartOn(employee.getStartOn());
                newEmployee.setUser(user);
                employeeRepository.save(newEmployee);
                userRepository.save(user);

            }else{
                throw new NullException("Thông tin tài khoản không được để trống");
            }
        }else {
            throw new NullException("Thông tin nhân viên không được để trống");
        }

    }



    public void updateEmployee(int id, Employee employee, User user){
        Optional<Employee> empOpt = employeeRepository.findById(id);
        if (empOpt.isPresent()){
            Employee oldEmployee = empOpt.get();
            User oldUser = oldEmployee.getUser();
            if(oldUser!=null){
                if(employee!=null){
                    editEmp(user, oldUser);

                    oldEmployee.setStartOn(employee.getStartOn());
                    oldEmployee.setUser(oldUser);
                    employeeRepository.save(oldEmployee);
                    userRepository.save(oldUser);
                }else {
                    throw new NullException("Thông tin nhân viên không được để trống");
                }
            }
            else{
                throw new NullException("Thông tin tài khoản không được để trống");
            }
        }else{
            throw new NotFoundException("Không tìm thấy nhân viên: "+ id);
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
            throw new NotFoundException("Không tìm thấy nhân viên: "+ id);
        }
    }



}
