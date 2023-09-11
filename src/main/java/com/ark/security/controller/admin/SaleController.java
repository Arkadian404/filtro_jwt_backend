package com.ark.security.controller.admin;

import com.ark.security.models.Sale;
import com.ark.security.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/sale")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SaleController {
    private final SaleService saleService;


    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getSaleList(){
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getSaleById(@PathVariable int id){
        Sale sale = saleService.getSaleById(id);
        if(sale == null){
            return ResponseEntity.badRequest().body("Không tìm thấy sự kiện");
        }
        return ResponseEntity.ok(sale);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> create(@RequestBody Sale sale){
        if(sale == null){
            return ResponseEntity.badRequest().body("Sự kiện không được để trống");
        }
        saleService.saveSale(sale);
        return ResponseEntity.ok("Tạo sự kiện thành công");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Sale sale){
        Sale newSale = saleService.getSaleById(id);
        if(newSale == null){
            return ResponseEntity.badRequest().body("Không tìm thấy sự kiện");
        }
        if(sale == null){
            return ResponseEntity.badRequest().body("Sự kiện không được để trống");
        }
        saleService.updateSale(id, sale);
        return ResponseEntity.ok("Cập nhật sự kiện thành công");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        Sale sale = saleService.getSaleById(id);
        if(sale == null){
            return ResponseEntity.badRequest().body("Không tìm thấy sự kiện");
        }
        saleService.deleteSale(id);
        return ResponseEntity.ok("Xóa sự kiện thành công");
    }

}
