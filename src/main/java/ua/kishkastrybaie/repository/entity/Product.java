package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product", schema = "main")
@Data
public class Product {
    @Id
    @GeneratedValue(generator = "product_seq_generator")
    @SequenceGenerator(
            name = "product_seq_generator",
            sequenceName = "product_seq",
            schema = "main",
            allocationSize = 1
    )
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String mainImage;

    @ManyToOne
    private Category category;
}
