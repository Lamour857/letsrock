package org.wj.letsrock.domain.image.factory.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.infrastructure.config.StorageConfig;
import org.wj.letsrock.utils.ImageUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service("localImageStorage")
public class LocalImageStorage implements ImageStorage {
    @Autowired
    private StorageConfig storageConfig;
    private final String accessUrl;

    public LocalImageStorage(
            @Value("${server.address}") String address,
            @Value("${server.port}") String port) {
        this.accessUrl = String.format("http://%s:%s/image/", address, port);
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        String extension = ImageUtil.getExt(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;
        Path targetPath = storageConfig.getLocation().resolve(filename).normalize();
        
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return accessUrl + filename;
    }

    @Override
    public boolean exists(String url) {
        if(!url.startsWith(accessUrl)) {
            return false;
        }
        String filename = url.substring(accessUrl.length());
        Path path =storageConfig.getLocation().resolve(filename);
        return Files.exists(path);
    }

    @Override
    public void delete(String url) throws IOException {
        if(!url.startsWith(accessUrl)) {
            return;
        }
        String filename = url.substring(accessUrl.length());
        Path path = storageConfig.getLocation().resolve(filename);
        Files.deleteIfExists(path);
    }
}
