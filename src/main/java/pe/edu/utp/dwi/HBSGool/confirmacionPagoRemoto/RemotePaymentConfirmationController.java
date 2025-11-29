package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto.dto.RemotePaymentConfirmationDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/confirmaciones")
@RequiredArgsConstructor
public class RemotePaymentConfirmationController {
	private final RemotePaymentConfirmationService service;

	@GetMapping
	@Operation(summary = "Listar confirmaciones de pago remoto con filtros opcionales")
	@PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
	public Page<RemotePaymentConfirmationDTO> getAll(
			@Parameter(description = "ID del cajero (opcional)")
			@RequestParam(required = false) Integer cashierId,

			@Parameter(description = "Fecha exacta (opcional, formato ISO ej: 2025-10-11T10:00:00)")
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,

			@Parameter(description = "Fecha de inicio para rango (opcional, formato ISO ej: 2025-10-10T00:00:00)")
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

			@Parameter(description = "Fecha fin para rango (opcional, formato ISO ej: 2025-10-11T23:59:59)")
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return service.findAll(cashierId, date, startDate, endDate, pageable);
	}

	@GetMapping("/{confirmationId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
	public ResponseEntity<RemotePaymentConfirmationDTO> getById(@PathVariable Integer confirmationId) {
		return ResponseEntity.ok(service.getById(confirmationId));
	}


	@GetMapping("/pago/{paymentId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
	public ResponseEntity<RemotePaymentConfirmationDTO> getByPaymentId(@PathVariable Integer paymentId) {
		return ResponseEntity.ok(service.getByPaymentId(paymentId));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
	@PostMapping("/pago/{paymentId}/confirmar")
	public ResponseEntity<Void> confirmRemotePayment(@PathVariable Integer paymentId) {
		service.confirmPayment(paymentId);

		return ResponseEntity.ok(null);
	}
}
