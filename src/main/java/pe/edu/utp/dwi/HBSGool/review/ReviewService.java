package pe.edu.utp.dwi.HBSGool.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;
    private final AuthService authService;

    public Page<ReviewDto> listReviews(Integer usuarioId, Pageable pageable) {
        Page<ReviewEntity> page;
        if (usuarioId != null) {
            page = repository.findByUsuarioId(usuarioId, pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return page.map(this::toDto);
    }

    public ReviewDto getById(Integer id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    public ReviewDto createReview(CreateReviewDto createDto) {

        UsuarioEntity currentUser = authService.getCurrentUser()
                .orElseThrow();

        ReviewEntity entity = new ReviewEntity();
        entity.setUsuarioId(currentUser.getUserId());
        entity.setRating(createDto.getRating());
        entity.setComentario(createDto.getComentario());
        entity.setCreado(LocalDateTime.now());
        
        ReviewEntity saved = repository.save(entity);
        return toDto(saved);
    }

    private ReviewDto toDto(ReviewEntity e) {
        return new ReviewDto(e.getIdReview(), e.getUsuarioId(), e.getRating(), e.getComentario(), e.getCreado());
    }
}
