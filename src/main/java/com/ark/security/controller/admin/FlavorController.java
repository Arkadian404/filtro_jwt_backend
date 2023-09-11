package com.ark.security.controller.admin;

import com.ark.security.models.Flavor;
import com.ark.security.service.FlavorService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin/flavor")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class FlavorController {

    private final FlavorService flavorService;

    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getFlavorList(){
        return ResponseEntity.ok(flavorService.getAllFlavors());
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        Flavor flavor = flavorService.getFlavorById(id);
        if(flavor == null){
            return ResponseEntity.badRequest().body("Không tìm thấy hương vị: "+ id);
        }
        return ResponseEntity.ok(flavor);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> create(@RequestBody Flavor flavor){
        if(flavor == null){
            return ResponseEntity.badRequest().body("Hương vị không được trống");
        }
        flavorService.saveFlavor(flavor);
        return ResponseEntity.ok("Tạo hương vị thành công");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Flavor flavor){
        Flavor oldFlavor = flavorService.getFlavorById(id);
        if(oldFlavor == null){
            return ResponseEntity.badRequest().body("Hương vị không được trống");
        }
        if(flavor == null){
            return ResponseEntity.badRequest().body("Không tìm thấy hương vị: "+ id);
        }
        flavorService.updateFlavor(id, flavor);
        return ResponseEntity.ok("Cập nhật hương vị thành công");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        Flavor flavor = flavorService.getFlavorById(id);
        if(flavor == null){
            return ResponseEntity.badRequest().body("Không tìm thấy hương vị: "+id);
        }
        flavorService.deleteFlavor(id);
        return ResponseEntity.ok("Xóa hương vị thành công");
    }
}
