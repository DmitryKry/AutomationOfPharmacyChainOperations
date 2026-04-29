package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "city")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @NonNull
    private BigDecimal id;
    @Column(name = "name", nullable = false, length = 100)
    @NonNull
    private String name;
    @Column(name = "region", nullable = false, length = 100)
    @NonNull
    private String region;
}
