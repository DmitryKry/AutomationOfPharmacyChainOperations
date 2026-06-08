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
@Table(name = "order_medicine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "id_of_medicine", nullable = false)
    private Long idOfMedicine;

    private BigDecimal count;

    @Column(name = "price_at_time")
    private BigDecimal priceAtTime;

    @Transient
    private BigDecimal priceMed;

    @Transient
    private String nameMed;
}