package com.ark.security.controller.user;

import com.ark.security.models.product.Flavor;
import com.ark.security.service.FlavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/flavor")
@RequiredArgsConstructor
public class FlavorController {
    private final FlavorService flavorService;

    @GetMapping("/getList")
    public ResponseEntity<?> getFlavorList(){
        List<Flavor> flavors = flavorService.getAllFlavors();
        return ResponseEntity.ok(flavors);

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        Flavor flavor = flavorService.getFlavorById(id);
        return ResponseEntity.ok(flavor);
    }
}
