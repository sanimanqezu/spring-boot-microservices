package za.co.example.persistance.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "product_quantity")
    private Integer quantity;

    @ElementCollection
    @CollectionTable(name = "products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_name")
    @Column(name = "products")
    private Map<String, String> products = new HashMap<>();

    @Column(name = "orderer_full_name")
    private String ordererFullName;

    @Column(name = "orderer_id_no")
    private String ordererIdNo;
}
