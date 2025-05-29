package org.wj.letsrock.domain.image.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.domain.image.service.ImageStorage;
import org.wj.letsrock.utils.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service("localImageStorage")
public class LocalImageStorage implements ImageStorage {
    private final Path storagePath;
    private final String accessUrl;

    public LocalImageStorage(
            Path imageStoragePath,
            @Value("${server.address}") String address,
            @Value("${server.port}") String port) {
        this.storagePath = imageStoragePath;
        this.accessUrl = String.format("http://%s:%s/image/", address, port);
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        String extension = ImageUtil.getExt(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;
        Path targetPath = storagePath.resolve(filename).normalize();
        
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return accessUrl + filename;
    }

    @Override
    public boolean exists(String url) {
        if(!url.startsWith(accessUrl)) {
            return false;
        }
        String filename = url.substring(accessUrl.length());
        Path path = storagePath.resolve(filename);
        return Files.exists(path);
    }

    @Override
    public void delete(String url) throws IOException {
        if(!url.startsWith(accessUrl)) {
            return;
        }
        String filename = url.substring(accessUrl.length());
        Path path = storagePath.resolve(filename);
        Files.deleteIfExists(path);
    }
}
