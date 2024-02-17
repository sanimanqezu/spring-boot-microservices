package za.co.example.persistance.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "order")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "products")
    @ElementCollection
    private List<String> products;
}
