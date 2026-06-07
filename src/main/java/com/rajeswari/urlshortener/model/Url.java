package com.rajeswari.urlshortener.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "short_url", schema = "urlshortener")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url implements Persistable<Long> {

    @Id
    private Long id;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PostLoad
    @PostPersist
    public void markNotNew() {
        this.isNew = false;
    }

    @Column(name = "short_code", unique = true)
    private String shortCode;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "click_count", nullable = false)
    private long clickCount;

    @Column(name = "active", nullable = false)
    private boolean active;
}