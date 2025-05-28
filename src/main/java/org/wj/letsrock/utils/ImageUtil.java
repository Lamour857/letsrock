package org.wj.letsrock.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.enums.StatusEnum;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-21:53
 **/
public class ImageUtil {
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final Tika tika = new Tika();
    // 支持的图片类型及对应特征
    private enum ImageType {
        JPEG(new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF}, 0),
        PNG(new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47}, 0),
        GIF(new byte[]{0x47, 0x49, 0x46, 0x38}, 0),
        WEBP(new byte[]{0x52, 0x49, 0x46, 0x46, 0x57, 0x45, 0x42, 0x50}, 0),
        BMP(new byte[]{0x42, 0x4D}, 0),
        TIFF_II(new byte[]{0x49, 0x49, 0x2A, 0x00}, 0),
        TIFF_MM(new byte[]{0x4D, 0x4D, 0x00, 0x2A}, 0);

        private final byte[] magicNumber;
        private final int offset;

        ImageType(byte[] magicNumber, int offset) {
            this.magicNumber = magicNumber;
            this.offset = offset;
        }
    }
    private static final Set<String> allowedTypes = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("image/jpeg", "image/png", "image/webp")
    ));
    public static void validateImageFile(MultipartFile file) {
        // 校验MIME类型
        if (!allowedTypes.contains(file.getContentType())) {
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED, "不支持的图片格式");
        }

        // 校验文件大小（限制5MB）
        if (file.getSize() > MAX_FILE_SIZE) {
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED, "图片大小不能超过5MB");
        }
        try {
            if (!ImageUtil.isValidImage(file)) {
               throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED, "不支持的图片格式");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidImage(MultipartFile file) throws IOException {
        String mimeType = tika.detect(file.getInputStream());
        return mimeType.startsWith("image/");
    }

    public static String getExt(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        return ext != null ? ext.toLowerCase() : "";
    }

    private static boolean checkMagicNumber(byte[] header, byte[] magicNumber, int offset) {
        if (header.length < offset + magicNumber.length) {
            return false;
        }

        for (int i = 0; i < magicNumber.length; i++) {
            if (header[offset + i] != magicNumber[i]) {
                return false;
            }
        }
        return true;
    }
}
