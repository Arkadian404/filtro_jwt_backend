package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.FlavorRequest;
import com.ark.security.dto.response.FlavorResponse;
import com.ark.security.service.product.FlavorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/flavor")
@RequiredArgsConstructor
public class FlavorController {
    private final FlavorService flavorTestService;


    @GetMapping
    public ApiResponse<List<FlavorResponse>> getAllFlavors(){
        return ApiResponse.<List<FlavorResponse>>builder()
                .message("Get all flavors successfully")
                .result(flavorTestService.getAllFlavors())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<FlavorResponse> getFlavorById(@PathVariable int id){
        return ApiResponse.<FlavorResponse>builder()
                .message("Get flavor by id successfully")
                .result(flavorTestService.getFlavorById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<FlavorResponse> saveFlavor(@RequestBody @Valid FlavorRequest request){
        return ApiResponse.<FlavorResponse>builder()
                .message("Save flavor successfully")
                .result(flavorTestService.saveFlavor(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<FlavorResponse> updateFlavor(@PathVariable int id, @RequestBody @Valid FlavorRequest request){
        return ApiResponse.<FlavorResponse>builder()
                .message("Update flavor successfully")
                .result(flavorTestService.updateFlavor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteFlavor(@PathVariable int id){
        flavorTestService.deleteFlavor(id);
        return ApiResponse.<String>builder()
                .message("Delete flavor successfully")
                .result("Delete flavor successfully")
                .build();
    }
}
