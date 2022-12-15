package com.vgorash.segmentation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class InferenceService {
    public String doInference(String imageBase64){
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imageBase64)));

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
