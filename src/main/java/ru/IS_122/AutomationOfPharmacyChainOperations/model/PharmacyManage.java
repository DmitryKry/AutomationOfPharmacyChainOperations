package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pharmacy_manage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyManage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    private BigDecimal id;

    @Column(name = "id_of_pharmacy", nullable = false)
    private BigDecimal idOfPharmacy;

    @Column(name = "id_of_worker", nullable = false)
    private BigDecimal idOfWorker;
}
