package com.binkat.cashback.service;

import com.binkat.cashback.dto.wallet.WalletBalanceResponse;
import com.binkat.cashback.dto.wallet.WalletLedgerResponse;
import com.binkat.cashback.model.User;
import com.binkat.cashback.model.Wallet;
import com.binkat.cashback.repository.UserRepository;
import com.binkat.cashback.repository.WalletLedgerRepository;
import com.binkat.cashback.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletLedgerRepository walletLedgerRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository,
                        WalletLedgerRepository walletLedgerRepository,
                        UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.walletLedgerRepository = walletLedgerRepository;
        this.userRepository = userRepository;
    }

    public WalletBalanceResponse getBalance(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        // Calculate current balance from ledger
        BigDecimal currentBalance = walletLedgerRepository.calculateCurrentBalance(wallet.getId());
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }

        // Update cached balance
        if (!currentBalance.equals(wallet.getBalancePointsCached())) {
            wallet.setBalancePointsCached(currentBalance);
            walletRepository.save(wallet);
        }

        return new WalletBalanceResponse(
            wallet.getId(),
            currentBalance,
            "POINTS",
            wallet.getUpdatedAt()
        );
    }

    public WalletLedgerResponse getLedger(Long userId, int limit, String cursor) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        Pageable pageable = PageRequest.of(0, limit);
        Page<com.binkat.cashback.model.WalletLedger> ledgerPage = 
            walletLedgerRepository.findByWalletOrderByCreatedAtDesc(wallet, pageable);

        var entries = ledgerPage.getContent().stream()
            .map(entry -> new WalletLedgerResponse.LedgerEntry(
                entry.getId(),
                entry.getReason(),
                entry.getPointsChange(),
                entry.getBalanceAfter(),
                entry.getDescription(),
                entry.getRefillTransaction() != null ? entry.getRefillTransaction().getId() : null,
                entry.getExpiresAt(),
                entry.getCreatedAt()
            ))
            .toList();

        return new WalletLedgerResponse(
            entries,
            ledgerPage.getTotalElements(),
            ledgerPage.hasNext(),
            ledgerPage.hasNext() ? String.valueOf(ledgerPage.getNumber() + 1) : null
        );
    }
}