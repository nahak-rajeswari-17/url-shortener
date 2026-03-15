package com.rajeswari.urlshortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShortUrlRequest {

    private String originalUrl;

}