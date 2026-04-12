package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "role")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private BigDecimal id;
    @Column(name = "name", nullable = false, length = 200)
    @NonNull
    private String name;
}
