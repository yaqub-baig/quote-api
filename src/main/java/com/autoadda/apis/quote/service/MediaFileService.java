package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.response.MediaFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MediaFileService {

    MediaFileResponse uploadFile(MultipartFile file);

    List<MediaFileResponse> uploadFiles(List<MultipartFile> files);

    boolean deleteFile(Long id);

    Optional<MediaFileResponse> getFileById(Long id);
}
