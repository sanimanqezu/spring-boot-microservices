package za.co.example.persistance.entities;

import jakarta.persistence.*;
import za.co.example.basePK.EntitiesPK;

import java.util.List;

@Entity
@Table(name = "order")
public class Order extends EntitiesPK {

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "product_quantity")
    private Integer quantity;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product")
    private List<Product> products;
}
