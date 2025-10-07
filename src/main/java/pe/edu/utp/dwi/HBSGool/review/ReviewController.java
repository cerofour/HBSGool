package pe.edu.utp.dwi.HBSGool.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @GetMapping
    public ResponseEntity<Page<ReviewDto>> list(
            @RequestParam(name = "usuarioId", required = false) Integer usuarioId,
            @PageableDefault(size = 10, sort = "creado", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReviewDto> page = service.listReviews(usuarioId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getById(@PathVariable Integer id) {
        ReviewDto dto = service.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
