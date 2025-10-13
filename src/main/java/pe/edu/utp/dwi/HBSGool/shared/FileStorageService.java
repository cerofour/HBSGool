package pe.edu.utp.dwi.HBSGool.shared;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.dwi.HBSGool.exception.InvalidImageException;
import pe.edu.utp.dwi.HBSGool.exception.InvalidImageFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
	public String saveEvidenceFile(MultipartFile file, Integer reservationId) {
		if (file == null || file.isEmpty()) return null;

		if (file.getOriginalFilename() == null)
			throw new InvalidImageException();

		if (file.getOriginalFilename() != null && !(
				file.getOriginalFilename().endsWith(".png") ||
				file.getOriginalFilename().endsWith(".jpg") ||
				file.getOriginalFilename().endsWith(".jpeg"))) {
			throw new InvalidImageFormatException();
		}

		try {
			String uploadDir = "uploads/evidencias/";
			Files.createDirectories(Paths.get(uploadDir));

			String filename = "reserva_" + reservationId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir, filename);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			return filePath.toString();
		} catch (IOException e) {
			throw new RuntimeException("Error al guardar la evidencia: " + e.getMessage());
		}
	}
}
