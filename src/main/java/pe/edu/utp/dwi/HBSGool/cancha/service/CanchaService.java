package pe.edu.utp.dwi.HBSGool.cancha.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.cancha.model.CanchaEntity;
import pe.edu.utp.dwi.HBSGool.cancha.repository.CanchaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanchaService {
	private final CanchaRepository repository;

	public List<CanchaEntity> findAll() {
		return repository.findAll();
	}

	public CanchaEntity patchCancha(Integer id, pe.edu.utp.dwi.HBSGool.cancha.model.CanchaPatchDTO dto) {
	System.out.println("PATCH DTO recibido: " + dto);
	CanchaEntity cancha = repository.findById(id)
		.orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

	System.out.println("Cancha antes de modificar: " + cancha);
	if (dto.getNombre() != null) cancha.setName(dto.getNombre());
	if (dto.getDescripcion() != null) cancha.setDescription(dto.getDescripcion());
	if (dto.getPrecioHora() != null) cancha.setHourlyPrice(dto.getPrecioHora());
	if (dto.getEstadoCancha() != null) cancha.setCanchaState(dto.getEstadoCancha());
	System.out.println("Cancha después de modificar: " + cancha);

	return repository.save(cancha);
	}
}
