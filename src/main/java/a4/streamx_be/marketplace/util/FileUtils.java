package a4.streamx_be.marketplace.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public static String saveFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            String filePath = UPLOAD_DIR + fileName;

            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(filePath));
            return filePath;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}