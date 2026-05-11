package ru.IS_122.AutomationOfPharmacyChainOperations.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    private BigDecimal id;

    // --- Основная информация ---
    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "international_name", length = 200)
    private String internationalName;

    @Column(name = "dosage_strength", length = 50)
    private String dosageStrength;

    @Column(name = "package_quantity")
    private Integer packageQuantity;

    @Column(name = "prescription_required")
    private Boolean prescriptionRequired = true;

    // --- Регистрационные данные ---
    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    // --- Условия хранения ---
    @Column(name = "shelf_life_months")
    private Integer shelfLifeMonths;

    @Column(name = "storage_temperature_min")
    private Integer storageTemperatureMin;

    @Column(name = "storage_temperature_max")
    private Integer storageTemperatureMax;

    @Column(name = "requires_refrigeration")
    private Boolean requiresRefrigeration = false;

    @Column(name = "light_sensitive")           // ← ДОБАВИТЬ
    private Boolean lightSensitive = false;

    @Column(name = "requires_dark_storage")     // ← ДОБАВИТЬ
    private Boolean requiresDarkStorage = false;

    // --- Статусные флаги ---
    @Column(name = "is_active")                 // ← ДОБАВИТЬ
    private Boolean isActive = true;

    @Column(name = "is_vital")
    private Boolean isVital = false;

    @Column(name = "is_available")              // ← ДОБАВИТЬ
    private Boolean isAvailable = true;

    // --- Системный аудит ---
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")                // ← ДОБАВИТЬ
    private LocalDateTime updatedAt;

    @Column(name = "created_by")                // ← ДОБАВИТЬ
    private BigDecimal createdBy;

    @Column(name = "updated_by")                // ← ДОБАВИТЬ
    private BigDecimal updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // --- СВЯЗИ СО СПРАВОЧНИКАМИ ---

    @Column(name = "pharmacological_group_id")
    private Long pharmacologicalGroupId;

    @Column(name = "therapeutic_group_id")
    private Long therapeuticGroupId;

    @Column(name = "manufacturer_id")
    private Long manufacturerId;

    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "prescription_form_id")
    private Long prescriptionFormId;

    @Column(name = "dosage_form_id")
    private Long dosageFormId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "atc_id")
    private Long atcId;

    @Column(name = "package_type_id")
    private Long packageTypeId;

}