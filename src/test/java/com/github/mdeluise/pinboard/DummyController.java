package com.github.mdeluise.pinboard;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// See DummyPageScraper
@Profile("integration")
@Controller
@RequestMapping("/dummy")
public class DummyController {

    @GetMapping("/")
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("<html><title>This is a dummy page<title/></html>", HttpStatus.OK);
    }
}
