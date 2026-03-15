package com.rajeswari.urlshortener.controller;

import com.rajeswari.urlshortener.model.Url;
import com.rajeswari.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;

    @GetMapping("/u/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        Url url = urlService.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(302)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }
}