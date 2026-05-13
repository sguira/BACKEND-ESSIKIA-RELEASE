package com.formation.demo.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class SupaBaseService {

        @Value("${supabase.url}")
        private String supabaseUrl;

        @Value("${supabase.key}")
        private String apiKey;

        @Value("${supabase.bucket}")
        private String bucketName;

        // ✅ Client configuré correctement avec timeouts
        private final OkHttpClient httpClient = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();

        public Map<String, Object> uploadFile(MultipartFile file) throws IOException {

                Map<String, Object> result = new HashMap<>();

                // Génération nom unique
                String filePath = "uploads/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // URL d’upload Supabase
                String uploadUrl = String.format(
                                "%s/storage/v1/object/%s/%s",
                                supabaseUrl,
                                bucketName,
                                filePath);

                // ✅ Body en raw binary (PAS multipart)
                RequestBody requestBody = RequestBody.create(
                                file.getBytes(),
                                MediaType.parse(
                                                file.getContentType() != null
                                                                ? file.getContentType()
                                                                : "application/octet-stream"));

                Request request = new Request.Builder()
                                .url(uploadUrl)
                                .header("Authorization", "Bearer " + apiKey)
                                .header("apikey", apiKey)
                                .header("Content-Type", "application/octet-stream")
                                .put(requestBody)
                                .build();

                try (Response response = httpClient.newCall(request).execute()) {

                        if (!response.isSuccessful()) {
                                String errorBody = response.body() != null ? response.body().string() : "";
                                throw new IOException("Erreur upload Supabase: " + errorBody);
                        }

                        // URL publique (si bucket public)
                        String publicUrl = String.format(
                                        "%s/storage/v1/object/public/%s/%s",
                                        supabaseUrl,
                                        bucketName,
                                        filePath);

                        result.put("url", publicUrl);
                        result.put("type", file.getContentType());
                        result.put("size", file.getSize());
                        result.put("name", file.getOriginalFilename());

                        return result;
                }
        }

        public String generateSignedUploadUrl(String filePath) throws IOException {

                String url = String.format(
                                "%s/storage/v1/object/upload/sign/%s/%s",
                                supabaseUrl,
                                bucketName,
                                filePath);

                Request request = new Request.Builder()
                                .url(url)
                                .header("Authorization", "Bearer " + apiKey)
                                .header("apikey", apiKey)
                                .post(RequestBody.create(new byte[0]))
                                .build();

                try (Response response = httpClient.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                                throw new IOException("Erreur génération URL signée");
                        }

                        return response.body().string();
                }
        }

}
