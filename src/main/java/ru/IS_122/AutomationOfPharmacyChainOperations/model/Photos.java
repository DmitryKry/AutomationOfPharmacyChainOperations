package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_gen")
    @SequenceGenerator(name = "allobjects_gen", sequenceName = "allobjects", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private BigDecimal id;

    @Column(name = "entity_id")
    private BigDecimal entityId;
    @Column(name = "entity_type", nullable = false, length = 1, columnDefinition = "bpchar")
    private String entityType;

    @Column(name = "photo_path", nullable = false, length = 500)
    private String photoPath;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "is_main")
    private Boolean isMain = false;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isMain == null) this.isMain = false;
        if (this.sortOrder == null) this.sortOrder = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String errorMessage;

    public void setPhotosResult(String errorMessage, BigDecimal id, String photoPath) {
        this.errorMessage = errorMessage;
        this.id = id;
        this.photoPath = photoPath;
    }
}
