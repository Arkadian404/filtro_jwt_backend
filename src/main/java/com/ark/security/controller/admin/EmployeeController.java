package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Employee;
import com.ark.security.models.user.User;
import com.ark.security.service.EmployeeService;
import com.ark.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping ("/api/v1/admin/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('employee:read', 'admin:read')")
    public ResponseEntity<?> getList(){
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('employee:read', 'admin:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        Employee emp = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(emp);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('employee:create', 'admin:create')")
    public ResponseEntity<?> create(@RequestBody Employee employee){
        employeeService.saveEmployee(employee, employee.getUser());
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo nhân viên thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('employee:update', 'admin:update')")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody Employee employee){
        employeeService.updateEmployee(id, employee, employee.getUser());
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật nhân viên thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        employeeService.deleteEmployee(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa nhân viên thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }
}
