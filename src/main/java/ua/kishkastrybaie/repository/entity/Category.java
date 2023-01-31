package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_category", schema = "main")
@Data
public class Category {

    @Id
    @GeneratedValue(generator = "category_seq_generator")
    @SequenceGenerator(
            name = "category_seq_generator",
            sequenceName = "category_seq",
            schema = "main",
            allocationSize = 1
    )
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @Column(name = "name")
    private String name;
}
