package com.rajeswari.urlshortener.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class CreateShortUrlRequest {

    @NotBlank(message = "Original URL cannot be empty")
    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "Invalid URL format"
    )
    private String originalUrl;

}