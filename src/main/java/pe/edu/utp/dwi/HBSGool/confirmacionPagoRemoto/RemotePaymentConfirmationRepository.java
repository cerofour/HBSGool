package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RemotePaymentConfirmationRepository extends JpaRepository<RemotePaymentConfirmationEntity, Integer> {
	Page<RemotePaymentConfirmationEntity> findByCashierId(Integer cashierId, Pageable pageable);

	Optional<RemotePaymentConfirmationEntity> findByPaymentId(Integer paymentId);

	// Filtrar por fecha exacta
	Page<RemotePaymentConfirmationEntity> findByDate(LocalDateTime date, Pageable pageable);

	// Filtrar por rango de fechas
	Page<RemotePaymentConfirmationEntity> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Page<RemotePaymentConfirmationEntity> findByCashierIdAndDateBetween(Integer cashierId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
