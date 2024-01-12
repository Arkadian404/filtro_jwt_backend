package com.ark.security.repository;

import com.ark.security.models.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {

    Optional<UserVoucher> findByUserIdAndVoucherId(int userId, int voucherId);
    Optional<List<UserVoucher>> findAllByUserId(int userId);
    Optional<List<UserVoucher>> findAllByVoucherId(int voucherId);
}
