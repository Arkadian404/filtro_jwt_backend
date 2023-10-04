package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.product.Sale;
import com.ark.security.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;

    public void saveSale(Sale sale){
        saleRepository.save(sale);
    }

    public List<Sale> getAllSales(){
        List<Sale> sales = saleRepository.findAll();
        if(sales.isEmpty()){
            throw new NotFoundException("Không có khuyến mãi nào");
        }
        return sales;
    }

    public Sale getSaleById(int id){
        return saleRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy khuyến mãi: "+ id));
    }

    public void updateSale(int id, Sale sale){
        Sale oldSale = getSaleById(id);
        if(sale == null){
            throw new NullException("Không được để trống");
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
            throw new NotFoundException("Không tìm thấy khuyến mãi: "+ id);
        }
    }

    public void deleteSale(int id){
        Sale sale = getSaleById(id);
        if(sale == null){
            throw new NotFoundException("Không tìm thấy khuyến mãi: "+ id);
        }
        saleRepository.deleteById(id);
    }
}
