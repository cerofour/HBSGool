package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/confirmaciones")
@RequiredArgsConstructor
public class RemotePaymentConfirmationController {
	private final RemotePaymentConfirmationService service;

	@GetMapping
	@Operation(summary = "Listar confirmaciones de pago remoto con filtros opcionales")
	@PreAuthorize("hasRole('ADMIN')")
	public List<RemotePaymentConfirmationDTO> getAll(
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
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
	) {
		return service.findAll(cashierId, date, startDate, endDate);
	}
}
