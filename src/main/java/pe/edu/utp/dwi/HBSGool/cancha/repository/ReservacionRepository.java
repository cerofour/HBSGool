package pe.edu.utp.dwi.HBSGool.cancha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.dwi.HBSGool.cancha.model.ReservacionEntity;

@Repository
public interface ReservacionRepository extends JpaRepository<ReservacionEntity, Integer> {
}
