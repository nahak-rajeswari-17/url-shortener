package com.rajeswari.urlshortener.service;

import com.rajeswari.urlshortener.model.Url;
import com.rajeswari.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void shouldReturnUrlAndIncrementCount() {

        Url url = Url.builder()
                .id(1L)
                .originalUrl("https://google.com")
                .shortCode("abc")
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        when(urlRepository.findByShortCode("abc"))
                .thenReturn(Optional.of(url));

        Url result = urlService.getOriginalUrl("abc");

        verify(urlRepository, times(1))
                .incrementClickCount("abc");

        assertThat(result.getOriginalUrl())
                .isEqualTo("https://google.com");
    }
}