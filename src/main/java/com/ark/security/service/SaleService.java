package com.ark.security.service;

import com.ark.security.models.Sale;
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
        return saleRepository.findAll();
    }

    public Sale getSaleById(int id){
        return saleRepository.findById(id).orElse(null);
    }

    public void updateSale(int id, Sale sale){
        Sale oldSale = getSaleById(id);
        if(oldSale !=null){
            oldSale.setName(sale.getName());
            oldSale.setDiscount(sale.getDiscount());
            oldSale.setDescription(sale.getDescription());
            oldSale.setStart(sale.getStart());
            oldSale.setEnd(sale.getEnd());
            oldSale.setStatus(sale.getStatus());
            saleRepository.save(oldSale);
        }
    }

    public void deleteSale(int id){
        saleRepository.deleteById(id);
    }
}
