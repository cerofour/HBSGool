package pe.edu.utp.dwi.HBSGool.cierrecajero;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaDto;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaEntity;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaRepositoy;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaService;
import pe.edu.utp.dwi.HBSGool.cierrecajero.dto.LogoutCashierRequest;
import pe.edu.utp.dwi.HBSGool.pago.PagoRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CierreCajeroService {

    private final CierreCajeroRepository cierreCajeroRepository;

    private final BovedaService bovedaService;

    private final PagoRepository pagoRepository;

    public Optional<CierreCajeroEntity> getBySesionCashierId(int sesionCashierId) {
        return cierreCajeroRepository.findBySesionCajeroId(sesionCashierId);
    }

    public double getTheoricMoney(Integer cashierSessionId) {
        return pagoRepository.getTotalCashPaymentsConfirmedByCashierSessionId(cashierSessionId);
    }

    public CierreCajeroDto logoutCashier(LogoutCashierRequest logoutCashierRequest) {

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(logoutCashierRequest.getSesionCajeroId());

        if(cierreCajero.isPresent()) throw new RuntimeException("Ya existe un cierre cajero para dicha sesion");

        CierreCajeroEntity entity = cierreCajeroRepository.save(
                CierreCajeroEntity.builder()
                        .sesionCajeroId(logoutCashierRequest.getSesionCajeroId())
                        .fecha(LocalDateTime.now())
                        .montoTeorico(getTheoricMoney(logoutCashierRequest.getSesionCajeroId()))
                        .montoReal(logoutCashierRequest.getMontoReal())
                        .build()
        );

        bovedaService.processCashMovement(
                BovedaDto.builder()
                        .tipoMovimientoBoveda("INGRESO")
                        .motivo("Cierre de caja")
                        .monto(logoutCashierRequest.getMontoReal())
                        .build()
        );

        return toDto(entity);

    }

    private CierreCajeroDto toDto(CierreCajeroEntity e) {
        return CierreCajeroDto.builder()
                .idCierreCajero(e.getIdCierreCajero())
                .sesionCajeroId(e.getSesionCajeroId())
                .fecha(e.getFecha())
                .montoTeorico(e.getMontoTeorico())
                .montoReal(e.getMontoReal())
                .build();
    }

}
