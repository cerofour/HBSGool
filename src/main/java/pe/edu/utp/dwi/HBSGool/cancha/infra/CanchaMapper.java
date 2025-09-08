package pe.edu.utp.dwi.HBSGool.cancha.infra;

import pe.edu.utp.dwi.HBSGool.cancha.domain.Cancha;
import pe.edu.utp.dwi.HBSGool.cancha.infra.persistence.CanchaEntity;

public class CanchaMapper {

	public static Cancha toDomain(CanchaEntity entity) {
		if (entity == null) return null;
		return new Cancha(
				entity.getId(),
				entity.getName(),
				entity.getDescription(),
				entity.getWidth(),
				entity.getLength(),
				entity.getSynthetic(),
				entity.getHourPrice(),
				entity.getCanchaStateId()
		);
	}

	public static CanchaEntity toEntity(Cancha domain) {
		if (domain == null) return null;
		return new CanchaEntity(
				domain.getId(),
				domain.getName(),
				domain.getDescription(),
				domain.getWidth(),
				domain.getLength(),
				domain.getSynthetic(),
				domain.getHourPrice(),
				domain.getCanchaStateId()
		);
	}
}
