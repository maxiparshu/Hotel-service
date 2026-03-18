package by.beltam.practice.repository;

import by.beltam.practice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}