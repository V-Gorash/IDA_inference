package com.vgorash.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MainController {

    @RequestMapping( "/")
    public String main(Map<String, Object> model){
        return "inference";
    }
}
