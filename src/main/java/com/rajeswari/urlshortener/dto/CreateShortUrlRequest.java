package com.rajeswari.urlshortener.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CreateShortUrlRequest {

    @NotBlank(message = "Original URL cannot be empty")
    @Size(max = 2048, message = "Original URL is too long")
    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "Invalid URL format"
    )
    private String originalUrl;
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiresAt;

}