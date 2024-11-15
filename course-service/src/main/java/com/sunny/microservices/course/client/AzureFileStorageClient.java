package com.sunny.microservices.course.client;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobStorageException;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AzureFileStorageClient implements FileStorageClient {

    BlobServiceClient blobServiceClient;
    @Override
    public String uploadFile(String containerName, String originalFileName, InputStream data, long lengh) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
            String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
            String contentType = getContentType(fileExtension);

            BlobClient blobClient = blobContainerClient.getBlobClient(newFileName);
            BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders()
                    .setContentType(contentType);

            blobClient.upload(data, lengh, true);
            blobClient.setHttpHeaders(blobHttpHeaders);

            return blobClient.getBlobUrl();
        }catch (BlobStorageException e) {
            throw new AppException(ErrorCode.FILE_CANNOT_UPLOAD);
        }
    }

    private String getContentType(String fileExtension) {
        return switch (fileExtension) {
            case "mp4" -> "video/mp4";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }
}
