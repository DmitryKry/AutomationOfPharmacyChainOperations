package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "administration_route")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrationRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;
}
