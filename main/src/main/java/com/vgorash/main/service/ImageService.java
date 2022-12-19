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
import java.awt.*;
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
            BufferedImage originalImage = prepareImage(image);
            String processedBase64 = restTemplate.postForObject("http://IDA-Segmentation/segmentation", prepareRequest(originalImage), String.class);
            BufferedImage processedImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(processedBase64)));

            BufferedImage maskImage = new BufferedImage(processedImage.getWidth(), processedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for(int h = 0; h < processedImage.getHeight(); h++){
                for (int w = 0; w < processedImage.getWidth(); w++){
                    int value = processedImage.getRGB(h, w);
                    int alpha = value > 0 ? 10 : 0;
                    int pixel = (alpha << 24) | (value << 16);
                    maskImage.setRGB(h, w, pixel);
                }
            }

            BufferedImage resultImage = new BufferedImage(processedImage.getWidth(), processedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = resultImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
            graphics2D.drawImage(maskImage, 0, 0, maskImage.getWidth(), maskImage.getHeight(), null);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(resultImage, "png", output);

            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer checkImage(MultipartFile image){
        try {
            return restTemplate.postForObject("http://IDA-Check/check", prepareRequest(image), Integer.class);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private BufferedImage prepareImage(MultipartFile image) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        BufferedImage resizedImage = new BufferedImage(512, 512, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(bufferedImage, 0, 0, 512, 512, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private HttpEntity<MultiValueMap<String, Object>> prepareRequest(BufferedImage image) throws IOException{
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        String base64 = Base64.getEncoder().encodeToString(output.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("image", base64);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<MultiValueMap<String, Object>> prepareRequest(MultipartFile image) throws IOException {
        return prepareRequest(prepareImage(image));
    }
}
