package com.vgorash.main.service;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    private final RestTemplate restTemplate;

    public ImageService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String processImage(MultipartFile image){
        try {
            return restTemplate.postForObject("http://IDA-Segmentation/segmentation", prepareRequest(image), String.class);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean checkImage(MultipartFile image){
        try {
            return restTemplate.postForObject("http://IDA-Check/check", prepareRequest(image), Boolean.class);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private HttpEntity<MultiValueMap<String, Object>> prepareRequest(MultipartFile image) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        ImageIO.write(bufferedImage, "png", output);
        String base64 = Base64.getEncoder().encodeToString(output.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("image", base64);
        return new HttpEntity<>(body, headers);
    }
}