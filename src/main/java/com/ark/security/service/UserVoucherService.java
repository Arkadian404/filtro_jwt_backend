package com.ark.security.service;

import com.ark.security.models.UserVoucher;
import com.ark.security.repository.UserVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserVoucherService {
    private final UserVoucherRepository userVoucherRepository;


    public UserVoucher getUserVoucherByUserIdAndVoucherId(int userId, int voucherId){
        return userVoucherRepository.findByUserIdAndVoucherId(userId, voucherId).orElse(null);
    }

    public void saveUserVoucher(UserVoucher userVoucher){
        userVoucherRepository.save(userVoucher);
    }

    public void deleteUserVoucher(int id){
        userVoucherRepository.deleteById(id);
    }

}
