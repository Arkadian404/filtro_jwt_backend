package com.ark.security.controller.admin;

import com.ark.security.service.StatisticService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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
    public ResponseEntity<?> getRevenueByDate(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy", iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy", iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return ResponseEntity.ok(statisticService.getRevenueByDate(startDate, endDate));
    }

    @GetMapping("/get/revenue/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getRevenueCurrentMonth(){
        return ResponseEntity.ok(statisticService.getRevenueByCurrentMonth());
    }

    @GetMapping ("/get/revenue/month")
    public ResponseEntity<?> getRevenueByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getRevenueByChosenMonth(month));
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
    public ResponseEntity<?> getOrderStatisticByDate(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy", iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy", iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return ResponseEntity.ok(statisticService.getOrderStatisticByDate(startDate, endDate));
    }

    @GetMapping("/get/order/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getOrderStatisticByCurrentMonth());
    }

    @GetMapping("/get/order/month")
    public ResponseEntity<?> getOrderStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getOrderStatisticByChosenMonth(month));
    }

    @GetMapping("/get/order/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOrderStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getOrderStatisticByLastMonth());
    }

    @GetMapping("/get/order/failed/date")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFailedOrderStatisticByDate(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy", iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy", iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return ResponseEntity.ok(statisticService.getFailedOrderStatisticByDate(startDate, endDate));
    }

    @GetMapping("/get/order/failed/currentMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getFailedOrderStatisticCurrentMonth(){
        return ResponseEntity.ok(statisticService.getFailedOrderStatisticByCurrentMonth());
    }

    @GetMapping("/get/order/failed/month")
    public ResponseEntity<?> getFailedOrderStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getFailedOrderStatisticByChosenMonth(month));
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

    @GetMapping("/get/order/location/month")
    public ResponseEntity<?> getOrderLocationStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getOrderLocationStatisticByChosenMonth(month));
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

    @GetMapping("/get/category/month")
    public ResponseEntity<?> getCategoryStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getCategoryStatisticByChosenMonth(month));
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

    @GetMapping("/get/flavor/month")
    public ResponseEntity<?> getFlavorStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getFlavorStatisticByChosenMonth(month));
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

    @GetMapping("/get/brand/month")
    public ResponseEntity<?> getBrandStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getBrandStatisticByChosenMonth(month));
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

    @GetMapping("/get/origin/month")
    public ResponseEntity<?> getOriginStatisticByChosenMonth(@RequestParam Integer month){
        return ResponseEntity.ok(statisticService.getOriginStatisticByChosenMonth(month));
    }

    @GetMapping("/get/origin/lastMonth")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getOriginStatisticLastMonth(){
        return ResponseEntity.ok(statisticService.getOriginStatisticByLastMonth());
    }


}
