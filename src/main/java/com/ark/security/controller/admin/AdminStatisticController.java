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

    @GetMapping("/get/revenue/date")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getRevenueByDate(@RequestParam Integer day, @RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getRevenueByDate(day, month, year));
    }

    @GetMapping("/get/revenue/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getRevenueCurrentMonth(){
        return ResponseEntity.ok(statisticService.getRevenueByCurrentMonth());
    }

    @GetMapping("/get/revenue/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getRevenueLastMonth(){
        return ResponseEntity.ok(statisticService.getRevenueByLastMonth());
    }

    @GetMapping("/get/order")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatistic(@RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getOrderStatistic(month, year));
    }

    @GetMapping("/get/order/date")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatisticByDate(@RequestParam Integer day, @RequestParam Integer month, @RequestParam Integer year){
        return ResponseEntity.ok(statisticService.getOrderStatisticByDate(day, month, year));
    }

    @GetMapping("/get/order/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getOrderStatisticByCurrentMonth());
    }

    @GetMapping("/get/order/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getOrderStatisticByLastMonth());
    }

    @GetMapping("/get/order/failed/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFailedOrderStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getFailedOrderStatisticByCurrentMonth());
    }

    @GetMapping("/get/order/failed/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFailedOrderStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getFailedOrderStatisticByLastMonth());
    }

    @GetMapping("/get/order/location/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderLocationStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getOrderLocationStatisticByCurrentMonth());
    }

    @GetMapping("/get/order/location/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderLocationStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getOrderLocationStatisticByLastMonth());
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

    @GetMapping("/get/category/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getCategoryStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getCategoryStatisticByCurrentMonth());
    }


    @GetMapping("/get/category/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getCategoryStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getCategoryStatisticByLastMonth());
    }

    @GetMapping("/get/flavor/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFlavorStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getFlavorStatisticByCurrentMonth());
    }

    @GetMapping("/get/flavor/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFlavorStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getFlavorStatisticByLastMonth());
    }

    @GetMapping("/get/brand/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getBrandStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getBrandStatisticByCurrentMonth());
    }

    @GetMapping("/get/brand/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getBrandStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getBrandStatisticByLastMonth());
    }

    @GetMapping("/get/origin/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOriginStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getOriginStatisticByCurrentMonth());
    }

    @GetMapping("/get/origin/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOriginStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getOriginStatisticByLastMonth());
    }


}
