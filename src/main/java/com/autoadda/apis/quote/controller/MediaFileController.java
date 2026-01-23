package com.autoadda.apis.quote.controller;

import com.autoadda.apis.quote.exception.ResourceNotFoundException;
import com.autoadda.apis.quote.response.MediaFileResponse;
import com.autoadda.apis.quote.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/mediafiles")
@RequiredArgsConstructor
public class MediaFileController {

    private final MediaFileService mediaFileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MediaFileResponse>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files) {
        List<MediaFileResponse> responses = mediaFileService.uploadFiles(files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaFileResponse> getFileById(@PathVariable Long id) {
        return mediaFileService.getFileById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Media file not found: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        if (!mediaFileService.deleteFile(id)) {
            throw new ResourceNotFoundException("Media file not found: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
