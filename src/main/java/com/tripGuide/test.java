package com.tripGuide;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping("/")
    public String home() {
        return "<h1>aws-v5</h1>";
    }
}
