package dev.dov.tasklist.web.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDto {

    @NotNull(message = "Image must be not null")
    private MultipartFile file;
}
