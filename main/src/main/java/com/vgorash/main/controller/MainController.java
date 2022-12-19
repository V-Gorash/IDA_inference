package com.vgorash.main.controller;

import com.vgorash.main.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class MainController {

    private final ImageService imageService;

    MainController(ImageService imageService){
        this.imageService = imageService;
    }

    @RequestMapping( "/")
    public String main(Map<String, Object> model){
        return "inference";
    }

    @RequestMapping( value = "/", method = RequestMethod.POST)
    public String inference(Map<String, Object> model, @RequestParam("image") MultipartFile image){
        model.put("base64Image", imageService.processImage(image));
        model.put("validType", imageService.checkImage(image));
        return "inference_result";
    }

    @RequestMapping("/healthcheck")
    @ResponseStatus(value = HttpStatus.OK)
    public void heathCheck(){}
}
