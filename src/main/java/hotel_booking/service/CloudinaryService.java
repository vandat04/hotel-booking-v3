package hotel_booking.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hotel_booking.dto.response.CloudinaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private String saveFileLocally(MultipartFile file) {
        try {
            java.io.File uploadDir = new java.io.File("uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = ".jpg";
            }
            String filename = java.util.UUID.randomUUID().toString() + extension;
            java.nio.file.Path filePath = java.nio.file.Paths.get("uploads").resolve(filename);
            java.nio.file.Files.write(filePath, file.getBytes());
            return "/api/uploads/" + filename;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lưu file cục bộ thất bại: " + e.getMessage());
        }
    }

    public String uploadFile1(MultipartFile file) {
        try {
            String apiKey = cloudinary.config.apiKey;
            if (apiKey == null || apiKey.contains("your-cloudinary-api-key") || apiKey.trim().isEmpty()) {
                System.out.println("[CloudinaryService] Detected placeholder/empty Cloudinary API key. Falling back to local upload.");
                return saveFileLocally(file);
            }

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "avatars"
                    )
            );

            return uploadResult.get("url").toString();

        } catch (Exception e) {
            System.err.println("[CloudinaryService] Cloudinary upload failed: " + e.getMessage() + ". Falling back to local upload.");
            e.printStackTrace();
            return saveFileLocally(file);
        }
    }

    public CloudinaryResponse uploadFile(MultipartFile file) {
        try {
            String apiKey = cloudinary.config.apiKey;
            if (apiKey == null || apiKey.contains("your-cloudinary-api-key") || apiKey.trim().isEmpty()) {
                System.out.println("[CloudinaryService] Detected placeholder/empty Cloudinary API key. Falling back to local upload.");
                String localUrl = saveFileLocally(file);
                String publicId = localUrl.substring(localUrl.lastIndexOf("/") + 1);
                return new CloudinaryResponse(localUrl, publicId);
            }

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "hotel/room-types"
                    )
            );

            String url = uploadResult.get("secure_url").toString(); // ✅ https
            String publicId = uploadResult.get("public_id").toString(); // 🔥 dùng để xoá

            return new CloudinaryResponse(url, publicId);

        } catch (Exception e) {
            System.err.println("[CloudinaryService] Cloudinary upload failed: " + e.getMessage() + ". Falling back to local upload.");
            try {
                String localUrl = saveFileLocally(file);
                String publicId = localUrl.substring(localUrl.lastIndexOf("/") + 1);
                return new CloudinaryResponse(localUrl, publicId);
            } catch (Exception ex) {
                throw new RuntimeException("Upload ảnh thất bại: " + ex.getMessage());
            }
        }
    }

    // 👉 xoá ảnh
    public void deleteFile(String publicId) {
        try {
            if (publicId != null && !publicId.contains("/")) {
                java.io.File localFile = new java.io.File("uploads/" + publicId);
                if (localFile.exists()) {
                    localFile.delete();
                    System.out.println("[CloudinaryService] Successfully deleted local file: " + publicId);
                    return;
                }
            } else if (publicId != null && publicId.contains("/uploads/")) {
                String filename = publicId.substring(publicId.lastIndexOf("/") + 1);
                java.io.File localFile = new java.io.File("uploads/" + filename);
                if (localFile.exists()) {
                    localFile.delete();
                    System.out.println("[CloudinaryService] Successfully deleted local file: " + filename);
                    return;
                }
            }

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );
        } catch (Exception e) {
            System.err.println("[CloudinaryService] Error deleting file: " + e.getMessage());
        }
    }

}
