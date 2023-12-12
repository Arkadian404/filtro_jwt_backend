package com.ark.security.controller.admin;

import com.ark.security.service.StatisticService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/statistic")
@RequiredArgsConstructor
//@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
//@SecurityRequirement(name = "BearerAuth")
public class AdminStatisticController {
    private final StatisticService statisticService;


    @GetMapping("/get/revenue")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getRevenue(@RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getRevenue(month, year));
    }

    @GetMapping("/get/order")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatistic(@RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getOrderStatistic(month, year));
    }

    @GetMapping("/get/user")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getUserStatistic(@RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getUserStatistic(month, year));
    }

    @GetMapping("/get/product")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getProductStatistic(@RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getProductStatistic(month, year));
    }

}
