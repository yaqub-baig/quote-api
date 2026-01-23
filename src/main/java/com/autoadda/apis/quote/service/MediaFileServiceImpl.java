package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.entity.MediaFile;
import com.autoadda.apis.quote.exception.FileStorageException;
import com.autoadda.apis.quote.mapper.MediaFileMapper;
import com.autoadda.apis.quote.repository.MediaFileRepository;
import com.autoadda.apis.quote.response.MediaFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaFileServiceImpl implements MediaFileService {

    private final MediaFileRepository mediaFileRepository;
    private final MediaFileMapper mediaFileMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String BANNERS_SUBFOLDER = "banners";

    @Override
    @Transactional
    public MediaFileResponse uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot store empty file");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFileName.contains("..")) {
            throw new FileStorageException("Invalid file path: " + originalFileName);
        }

        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            String filePath = BANNERS_SUBFOLDER + "/" + uniqueFileName;

            Path targetLocation = Paths.get(uploadDir).resolve(filePath).normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(uniqueFileName);
            mediaFile.setOriginalFileName(originalFileName);
            mediaFile.setFilePath(filePath);
            mediaFile.setMediaType(file.getContentType());
            mediaFile.setSize(file.getSize());
            mediaFile.setCreatedAt(LocalDateTime.now());

            MediaFile savedFile = mediaFileRepository.save(mediaFile);
            log.info("Stored file: {} at {}", originalFileName, filePath);

            return mediaFileMapper.toResponse(savedFile);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file: " + originalFileName, ex);
        }
    }

    @Override
    @Transactional
    public List<MediaFileResponse> uploadFiles(List<MultipartFile> files) {
        List<MediaFileResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            responses.add(uploadFile(file));
        }
        return responses;
    }

    @Override
    @Transactional
    public boolean deleteFile(Long id) {
        Optional<MediaFile> mediaFileOpt = mediaFileRepository.findById(id);
        if (mediaFileOpt.isEmpty()) {
            return false;
        }

        MediaFile mediaFile = mediaFileOpt.get();
        try {
            Path filePath = Paths.get(uploadDir).resolve(mediaFile.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
            mediaFileRepository.delete(mediaFile);
            log.info("Deleted file: {}", mediaFile.getFilePath());
            return true;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete file: " + mediaFile.getFilePath(), ex);
        }
    }

    @Override
    public Optional<MediaFileResponse> getFileById(Long id) {
        return mediaFileRepository.findById(id)
                .map(mediaFileMapper::toResponse);
    }
}
