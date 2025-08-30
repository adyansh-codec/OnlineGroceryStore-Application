package org.softuni.onlinegrocery.repository;

import org.softuni.onlinegrocery.domain.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {

    @Query("SELECT o FROM Otp o WHERE o.phoneNumber = :phoneNumber AND o.verified = false AND o.expiresAt > :now ORDER BY o.createdAt DESC")
    Optional<Otp> findLatestValidOtpByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("now") LocalDateTime now);

    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.verified = false AND o.expiresAt > :now ORDER BY o.createdAt DESC")
    Optional<Otp> findLatestValidOtpByEmail(@Param("email") String email, @Param("now") LocalDateTime now);

    @Query("SELECT o FROM Otp o WHERE o.otpCode = :otpCode AND o.verified = false AND o.expiresAt > :now")
    Optional<Otp> findValidOtpByCode(@Param("otpCode") String otpCode, @Param("now") LocalDateTime now);

    // Separate methods for phone and email validation - simpler and more reliable
    @Query("SELECT o FROM Otp o WHERE o.phoneNumber = :phoneNumber AND o.otpCode = :otpCode AND o.verified = false AND o.expiresAt > :now")
    Optional<Otp> findValidOtpByPhoneAndCode(@Param("phoneNumber") String phoneNumber, 
                                           @Param("otpCode") String otpCode, 
                                           @Param("now") LocalDateTime now);

    @Query("SELECT o FROM Otp o WHERE o.email = :email AND o.otpCode = :otpCode AND o.verified = false AND o.expiresAt > :now")
    Optional<Otp> findValidOtpByEmailAndCode(@Param("email") String email, 
                                           @Param("otpCode") String otpCode, 
                                           @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("DELETE FROM Otp o WHERE o.expiresAt < :now")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Otp o SET o.verified = true WHERE o.id = :id")
    void markAsVerified(@Param("id") String id);

    @Query("SELECT COUNT(o) FROM Otp o WHERE o.phoneNumber = :phoneNumber AND o.createdAt > :since")
    int countOtpsByPhoneNumberSince(@Param("phoneNumber") String phoneNumber, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(o) FROM Otp o WHERE o.email = :email AND o.createdAt > :since")
    int countOtpsByEmailSince(@Param("email") String email, @Param("since") LocalDateTime since);
}