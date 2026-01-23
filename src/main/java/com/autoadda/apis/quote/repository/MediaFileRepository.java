package com.autoadda.apis.quote.repository;

import com.autoadda.apis.quote.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

    Optional<MediaFile> findByFileName(String fileName);

    Optional<MediaFile> findByFilePath(String filePath);
}
