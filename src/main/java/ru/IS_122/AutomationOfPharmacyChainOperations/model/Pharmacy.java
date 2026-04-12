package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pharmacy")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @NonNull
    private BigDecimal id;
    @Column(name = "name", nullable = false, length = 200)
    @NonNull
    private String name;
    @Column(name = "legal_name", nullable = false, length = 200)
    @NonNull
    private String legal_name;
    @Column(name = "inn", nullable = false, length = 12)
    @NonNull
    private String inn;
    @Column(name = "kpp", nullable = false, length = 9)
    @NonNull
    private String kpp;
    @Column(name = "address", nullable = false, length = 255)
    @NonNull
    private String address;
    @Column(name = "city", nullable = false, length = 100)
    @NonNull
    private String city;
    @Column(name = "region", nullable = false, length = 100)
    @NonNull
    private String region;
    @Column(name = "postal_code", nullable = false, length = 10)
    @NonNull
    private String postal_code;
}
