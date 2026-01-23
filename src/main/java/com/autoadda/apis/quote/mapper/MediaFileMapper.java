package com.autoadda.apis.quote.mapper;

import com.autoadda.apis.quote.entity.MediaFile;
import com.autoadda.apis.quote.response.MediaFileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MediaFileMapper {

    @Value("${file.base-url}")
    private String baseUrl;

    public MediaFileResponse toResponse(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }
        MediaFileResponse response = new MediaFileResponse();
        response.setId(mediaFile.getId());
        response.setFileName(mediaFile.getFileName());
        response.setOriginalFileName(mediaFile.getOriginalFileName());
        response.setFilePath(mediaFile.getFilePath());
        response.setMediaType(mediaFile.getMediaType());
        response.setSize(mediaFile.getSize());
        response.setFileUrl(baseUrl + "/uploads/" + mediaFile.getFilePath());
        response.setCreatedAt(mediaFile.getCreatedAt());
        return response;
    }
}
