package pe.edu.utp.dwi.HBSGool.cancha.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataCanchaRepository extends JpaRepository<CanchaEntity, UUID> {
}
