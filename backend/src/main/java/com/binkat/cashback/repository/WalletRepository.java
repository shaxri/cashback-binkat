package com.binkat.cashback.repository;

import com.binkat.cashback.model.User;
import com.binkat.cashback.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Optional<Wallet> findByUser(User user);
    
    Optional<Wallet> findByUserId(Long userId);
}