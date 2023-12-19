package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.Flavor;
import com.ark.security.service.product.FlavorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/flavor")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class AdminFlavorController {

    private final FlavorService flavorService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFlavorList(){
        List<Flavor> flavors = flavorService.getAllFlavors();
        return ResponseEntity.ok(flavors);

    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        Flavor flavor = flavorService.getFlavorById(id);
        return ResponseEntity.ok(flavor);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ResponseEntity<?> create(@RequestBody Flavor flavor){
        flavorService.saveFlavor(flavor);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo hương vị thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Flavor flavor){
//        Flavor oldFlavor = flavorService.getFlavorById(id);
//        if(oldFlavor == null){
//            return ResponseEntity.badRequest().body("Hương vị không được trống");
//        }
//        if(flavor == null){
//            return ResponseEntity.badRequest().body("Không tìm thấy hương vị: "+ id);
//        }
        flavorService.updateFlavor(id, flavor);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật hương vị thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
//        Flavor flavor = flavorService.getFlavorById(id);
//        if(flavor == null){
//            return ResponseEntity.badRequest().body("Không tìm thấy hương vị: "+id);
//        }
        flavorService.deleteFlavor(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa hương vị thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }
}
