package com.tripGuide.server.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("hello")
    public String home() {
        return "<h1>hihihihi</h1>";
    }
}
