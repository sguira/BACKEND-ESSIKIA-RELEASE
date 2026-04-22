package com.formation.demo.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class R2Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    @Value("${r2.bucket}")
    private String bucket;

    // ✅ 1. Générer URL signée pour upload (équivalent Supabase)
    public Map<String, String> generateSignedUploadUrl(String filename) {

        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + filename;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            Map<String, String> result = new HashMap<>();
            result.put("signedUrl", presignedRequest.url().toString());
            result.put("filePath", key);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Erreur lors de la génération de l'URL signée: " + e.getMessage());
        }
    }

    // ✅ 2. Générer URL signée pour lecture vidéo
    public String generateSignedVideoUrl(String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }

    public void deleteFile(String key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression: " + e.getMessage());
        }
    }
}