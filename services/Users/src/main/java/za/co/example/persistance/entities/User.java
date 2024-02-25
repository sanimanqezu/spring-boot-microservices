package za.co.example.persistance.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.threeten.bp.LocalDate;
import za.co.example.basePK.EntitiesPK;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User extends EntitiesPK {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "rsa_id")
    private String rsaId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

}
