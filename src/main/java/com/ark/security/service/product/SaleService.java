package com.ark.security.service.product;

import com.ark.security.dto.SaleDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.product.Sale;
import com.ark.security.repository.product.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final String NOT_FOUND = "Không tìm thấy sự kiện: ";
    private final String EMPTY = "Không có sự kiện nào";
    private final String BLANK = "Không được để trống";

    public void saveSale(Sale sale){
        saleRepository.save(sale);
    }

    public List<Sale> getAllSales(){
        List<Sale> sales = saleRepository.findAll();
        if(sales.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return sales;
    }

    public List<SaleDto> getAllSalesDto() {
        List<SaleDto> sales = saleRepository.findAll()
                .stream()
                .map(Sale::convertToDto)
                .toList();
        if(sales.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return sales;
    }

    public Sale getSaleById(int id){
        return saleRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }

    public void updateSale(int id, Sale sale){
        Sale oldSale = getSaleById(id);
        if(sale == null){
            throw new NullException(BLANK);
        }
        if(oldSale !=null){
            oldSale.setName(sale.getName());
            oldSale.setDiscount(sale.getDiscount());
            oldSale.setDescription(sale.getDescription());
            oldSale.setStart(sale.getStart());
            oldSale.setEnd(sale.getEnd());
            oldSale.setStatus(sale.getStatus());
            saleRepository.save(oldSale);
        }else{
            throw new NotFoundException(NOT_FOUND+ id);
        }
    }

    public void deleteSale(int id){
        Sale sale = getSaleById(id);
        if(sale == null){
            throw new NotFoundException(NOT_FOUND+ id);
        }
        saleRepository.deleteById(id);
    }
}
