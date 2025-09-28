package com.binkat.cashback.service;

import com.binkat.cashback.dto.refill.CreateRefillRequest;
import com.binkat.cashback.dto.refill.RefillTransactionResponse;
import com.binkat.cashback.enums.PointsReason;
import com.binkat.cashback.enums.TransactionStatus;
import com.binkat.cashback.model.*;
import com.binkat.cashback.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class RefillService {

    private final RefillTransactionRepository refillTransactionRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final PricePlanRepository pricePlanRepository;
    private final WalletRepository walletRepository;
    private final WalletLedgerRepository walletLedgerRepository;

    @Value("${app.points.redemption-rate-uzs-per-point:10}")
    private BigDecimal redemptionRateUzsPerPoint;

    public RefillService(RefillTransactionRepository refillTransactionRepository,
                        UserRepository userRepository,
                        StationRepository stationRepository,
                        PricePlanRepository pricePlanRepository,
                        WalletRepository walletRepository,
                        WalletLedgerRepository walletLedgerRepository) {
        this.refillTransactionRepository = refillTransactionRepository;
        this.userRepository = userRepository;
        this.stationRepository = stationRepository;
        this.pricePlanRepository = pricePlanRepository;
        this.walletRepository = walletRepository;
        this.walletLedgerRepository = walletLedgerRepository;
    }

    public RefillTransactionResponse createRefill(Long userId, CreateRefillRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Station station = stationRepository.findById(request.stationId())
            .orElseThrow(() -> new IllegalArgumentException("Station not found"));

        if (!station.getActive()) {
            throw new IllegalArgumentException("Station is not active");
        }

        // Get current pricing
        PricePlan pricePlan = pricePlanRepository.findCurrentPricing(
            request.stationId(), request.kind(), request.grade(), Instant.now()
        ).orElseThrow(() -> new IllegalArgumentException("No current pricing available"));

        // Create refill transaction
        RefillTransaction refill = new RefillTransaction(
            user, station, pricePlan, request.kind(), pricePlan.getUnit(),
            request.quantity(), pricePlan.getBasePriceUzs()
        );
        refill.setGrade(request.grade());
        refill.setNotes(request.notes());

        // Calculate points earned (1 point per 10 UZS spent, example rate)
        BigDecimal pointsEarned = refill.getTotalAmountUzs()
            .divide(redemptionRateUzsPerPoint, 2, RoundingMode.DOWN);
        refill.setPointsEarned(pointsEarned);
        refill.setStatus(TransactionStatus.COMPLETED);

        refill = refillTransactionRepository.save(refill);

        // Add points to wallet if transaction completed
        if (refill.getStatus() == TransactionStatus.COMPLETED && pointsEarned.compareTo(BigDecimal.ZERO) > 0) {
            addPointsToWallet(user, refill, pointsEarned);
        }

        return mapToRefillTransactionResponse(refill);
    }

    @Transactional(readOnly = true)
    public List<RefillTransactionResponse> getRefills(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<RefillTransaction> refills = refillTransactionRepository
            .findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return refills.getContent().stream()
            .map(this::mapToRefillTransactionResponse)
            .toList();
    }

    private void addPointsToWallet(User user, RefillTransaction refill, BigDecimal pointsEarned) {
        Wallet wallet = walletRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        // Calculate new balance
        BigDecimal currentBalance = walletLedgerRepository.calculateCurrentBalance(wallet.getId());
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        BigDecimal newBalance = currentBalance.add(pointsEarned);

        // Create ledger entry
        WalletLedger ledgerEntry = new WalletLedger(
            wallet, refill, PointsReason.REFILL_EARNED, pointsEarned, newBalance,
            "Points earned from refill at " + refill.getStation().getName()
        );

        walletLedgerRepository.save(ledgerEntry);

        // Update cached balance
        wallet.setBalancePointsCached(newBalance);
        walletRepository.save(wallet);
    }

    private RefillTransactionResponse mapToRefillTransactionResponse(RefillTransaction refill) {
        return new RefillTransactionResponse(
            refill.getId(),
            refill.getUser().getId(),
            refill.getStation().getId(),
            refill.getStation().getName(),
            refill.getKind(),
            refill.getGrade(),
            refill.getUnit(),
            refill.getQuantity(),
            refill.getUnitPriceUzs(),
            refill.getTotalAmountUzs(),
            refill.getPointsEarned(),
            refill.getStatus(),
            refill.getNotes(),
            refill.getCreatedAt()
        );
    }
}