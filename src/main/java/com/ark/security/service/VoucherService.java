package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Voucher;
import com.ark.security.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;


    public void saveVoucher(Voucher voucher){
        voucher.setCode(RandomStringUtils.random(10, true, true));
        voucher.setCreatedAt(LocalDateTime.now());
        voucherRepository.save(voucher);
    }


    public Voucher getVoucherById(Integer id){
        return voucherRepository.findById(id).orElseThrow(()-> new NotFoundException("Voucher not found"));
    }

    public List<Voucher> getAllVoucher(){
        return voucherRepository.findAll();
    }

    public void updateVoucher(int id, Voucher voucher){
        Voucher oldVoucher = getVoucherById(id);
        if(voucher == null){
            throw new NotFoundException("Voucher is null");
        }
        if (oldVoucher != null) {
            oldVoucher.setName(voucher.getName());
            oldVoucher.setDiscount(voucher.getDiscount());
            oldVoucher.setCode(oldVoucher.getCode());
            oldVoucher.setExpirationDate(voucher.getExpirationDate());
            oldVoucher.setDescription(voucher.getDescription());
            oldVoucher.setCategory(voucher.getCategory());
            voucherRepository.save(oldVoucher);
        }
    }

    public void deleteVoucher(int id){
        Voucher oldVoucher = getVoucherById(id);
        if(oldVoucher == null){
            throw new NotFoundException("Voucher is null");
        }
        voucherRepository.deleteById(id);
    }

}
