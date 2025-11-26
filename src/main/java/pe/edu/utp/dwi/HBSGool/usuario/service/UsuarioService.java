package pe.edu.utp.dwi.HBSGool.usuario.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.exception.notfound.UsernameNotFoundException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioRepository;
import pe.edu.utp.dwi.HBSGool.usuario.dto.UsuarioResult;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Page<UsuarioResult> findAllUsers(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::toUsuarioResult);
    }

    public UsuarioResult findById(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::toUsuarioResult)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con esta ID"));
    }

    public UsuarioResult toUsuarioResult(UsuarioEntity e) {
        return UsuarioResult.builder()
                .idUsuario(e.getUserId())
                .nombreCompleto(
                        String.format("%s %s, %s", e.getFatherLastname(), e.getMotherLastname(), e.getName())
                )
                .documento(e.getDni())
                .celular(e.getCellphone())
                .correo(e.getEmail())
                .activo(e.getActive())
                .rol(e.getRol())
                .build();
    }
}
