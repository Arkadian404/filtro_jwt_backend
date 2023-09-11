package com.ark.security.controller.admin;

import com.ark.security.models.Employee;
import com.ark.security.models.user.User;
import com.ark.security.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/admin/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('employee:read', 'admin:read')")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('employee:read', 'admin:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        Employee emp = employeeService.getEmployeeById(id);
        if(emp == null){
            return ResponseEntity.badRequest().body("Không tìm thấy nhân viên: "+ id);
        }
        return ResponseEntity.ok(emp);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('employee:create', 'admin:create')")
    public ResponseEntity<?> create(@RequestBody Employee employee){
        if(employee == null){
            return ResponseEntity.badRequest().body("Nhân viên không được trống");
        }
        if(employee.getUser() == null){
            return ResponseEntity.badRequest().body("Tài khoản không được trống");
        }
        employeeService.saveEmployee(employee, employee.getUser());
        return ResponseEntity.ok("Tạo nhân viên thành công");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('employee:update', 'admin:update')")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody Employee employee){
        Employee oldEmployee = employeeService.getEmployeeById(id);
        if(oldEmployee == null){
            return ResponseEntity.badRequest().body("Không tìm thấy nhân viên: "+ id);
        }
        if(employee == null){
            return ResponseEntity.badRequest().body("Nhân viên không được trống");
        }
        if(employee.getUser() == null){
            return ResponseEntity.badRequest().body("Tài khoản không được trống");
        }
        employeeService.updateEmployee(id, employee, employee.getUser());
        return ResponseEntity.ok("Cập nhật nhân viên thành công");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('employee:delete', 'admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        Employee employee = employeeService.getEmployeeById(id);
        if(employee == null){
            return ResponseEntity.badRequest().body("Không tìm thấy nhân viên: "+id);
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Xóa nhân viên thành công");
    }
}
