package com.vgorash.check.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;

@Service
public class InferenceService {

    public boolean doInference(String imageBase64)
    {
        try{
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imageBase64)));
            File outputfile = new File("E:/saved.png");
            ImageIO.write(bufferedImage, "png", outputfile);
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
