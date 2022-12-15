package com.vgorash.segmentation.controller;

import com.vgorash.segmentation.service.InferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MainController {

    private final InferenceService inferenceService;

    public MainController(InferenceService inferenceService){
        this.inferenceService = inferenceService;
    }

    @GetMapping("/healthcheck")
    @ResponseStatus(value = HttpStatus.OK)
    public void heathCheck(){}

    @PostMapping("/segmentation")
    public String segmentation(@RequestParam("image") String imageBase64){
        System.out.println("request processing");
        return inferenceService.doInference(imageBase64);
    }
}
