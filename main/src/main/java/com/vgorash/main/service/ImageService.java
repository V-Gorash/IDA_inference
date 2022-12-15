package com.vgorash.main.service;

import org.springframework.stereotype.Service;
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

    public String processImage(MultipartFile image){
        try{
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
            ImageIO.write(bufferedImage, "png", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean checkImage(MultipartFile image){
        return false;
    }
}
