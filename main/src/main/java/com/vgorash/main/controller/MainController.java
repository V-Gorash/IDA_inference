package com.vgorash.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MainController {

    @RequestMapping( "/")
    public String main(Map<String, Object> model){
        return "inference";
    }

    @RequestMapping("/healthcheck")
    @ResponseStatus(value = HttpStatus.OK)
    public void heathCheck(){

    }
}
