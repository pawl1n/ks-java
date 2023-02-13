package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "product_category", schema = "main")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(generator = "category_seq")
    @SequenceGenerator(
            name = "category_seq",
            sequenceName = "category_seq",
            schema = "main",
            allocationSize = 1
    )
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private Set<Category> children;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Variation> variations;
}
