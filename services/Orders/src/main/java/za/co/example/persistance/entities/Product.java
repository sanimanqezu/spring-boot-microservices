package za.co.example.persistance.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import za.co.example.basePK.EntitiesPK;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product extends EntitiesPK {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_number")
    private String productNumber;

    @Column(name = "product_quantity")
    private Integer quantity;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

}
