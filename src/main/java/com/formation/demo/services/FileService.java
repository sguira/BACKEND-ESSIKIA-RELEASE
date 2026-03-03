package com.formation.demo.services;

import java.io.IOException;

// import java.net.http.HttpHeaders;

import org.apache.catalina.connector.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Multipart;

@Service
@Component
public class FileService {

    private final String SERVER_URL = "https://serveur-distant.com";

    public ResponseEntity<String> uploadFile(MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // convertir le fichier en ressource
        ByteArrayResource fileAResource = new ByteArrayResource(file.getBytes()) {

            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", fileAResource);
        HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(SERVER_URL, HttpMethod.POST, reqEntity, String.class);
    }

}
