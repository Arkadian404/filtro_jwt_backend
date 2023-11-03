package com.ark.security.controller.user;

import com.ark.security.dto.SaleDto;
import com.ark.security.models.product.Sale;
import com.ark.security.service.product.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping("/getList")
    public ResponseEntity<?> getSaleList(){
        List<SaleDto> sales = saleService.getAllSalesDto();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable int id){
        Sale sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }
}
