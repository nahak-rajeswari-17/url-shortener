package com.rajeswari.urlshortener.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateShortUrlResponse {

    private String originalUrl;
    private String shortUrl;
    private String shortCode;

}