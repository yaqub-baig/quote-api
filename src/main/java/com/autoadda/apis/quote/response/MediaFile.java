package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MediaFile {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String mediaType;
    private Long size;
    private LocalDateTime createdAt;
}
