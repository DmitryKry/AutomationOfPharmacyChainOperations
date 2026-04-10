package ru.IS_122.AutomationOfPharmacyChainOperations.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "userofpharmacy")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOfPharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allobjects_seq")
    @SequenceGenerator(name = "allobjects_seq", sequenceName = "allobjects", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = 50)
    @NonNull
    private String login;

    @Column(name = "password", nullable = false, length = 100)
    @NonNull
    private String password;

    @Column(name = "fio", nullable = false, length = 100)
    @NonNull
    private String fio;

    @Column(name = "passport", nullable = false)
    private String passport;

    @Column(name = "data_of_birt", nullable = false)
    @NonNull
    private LocalDate dataOfBirt;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "role", nullable = false, length = 0)
    @NonNull
    private Long role;

}