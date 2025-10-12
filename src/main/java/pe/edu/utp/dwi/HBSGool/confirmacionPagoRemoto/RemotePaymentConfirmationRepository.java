package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RemotePaymentConfirmationRepository extends JpaRepository<RemotePaymentConfirmationEntity, Integer> {
	List<RemotePaymentConfirmationEntity> findByCashierId(Integer cashierId);

	// Filtrar por fecha exacta
	List<RemotePaymentConfirmationEntity> findByDate(LocalDateTime date);

	// Filtrar por rango de fechas
	List<RemotePaymentConfirmationEntity> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<RemotePaymentConfirmationEntity> findByCashierIdAndDateBetween(Integer cashierId, LocalDateTime startDate, LocalDateTime endDate);
}
