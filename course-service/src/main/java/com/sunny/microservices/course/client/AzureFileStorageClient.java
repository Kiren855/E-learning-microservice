package com.sunny.microservices.course.client;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobStorageException;
import com.sunny.microservices.course.exception.AppException;
import com.sunny.microservices.course.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
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

    @Override
    public String uploadDirectory(String containerName, String directoryPath, String blobFolder) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            try (InputStream data = new FileInputStream(file)) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                String contentType = getContentType(fileExtension);

                String blobPath = blobFolder + "/" + fileName;

                BlobClient blobClient = blobContainerClient.getBlobClient(blobPath);
                BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);

                blobClient.upload(data, file.length(), true);
                blobClient.setHttpHeaders(headers);

            } catch (Exception e) {
                throw new AppException(ErrorCode.FILE_CANNOT_UPLOAD);
            }
        }

        return blobContainerClient.getBlobClient(blobFolder + "/master.m3u8").getBlobUrl();
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
            case "m3u8" -> "application/x-mpegURL";
            case "ts" -> "video/mp2t";
            case "m4s" -> "video/iso.segment";
            default -> "application/octet-stream";
        };
    }

    @Override
    public void deleteFile(String containerName, String fileName) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

            BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

            if (blobClient.exists()) {
                blobClient.delete();
            } else {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (BlobStorageException e) {
            throw new AppException(ErrorCode.FILE_CANNOT_DELETE);
        }
    }

    @Override
    public void deleteDirectory(String containerName, String blobFolder) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            containerClient.listBlobsByHierarchy(blobFolder + "/").forEach(blobItem -> {
                BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());
                blobClient.delete();
            });

        } catch (BlobStorageException e) {
            throw new AppException(ErrorCode.FILE_CANNOT_DELETE);
        }
    }
}
