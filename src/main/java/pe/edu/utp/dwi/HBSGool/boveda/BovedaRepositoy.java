package pe.edu.utp.dwi.HBSGool.boveda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BovedaRepositoy extends JpaRepository<BovedaEntity, Integer> {



}
