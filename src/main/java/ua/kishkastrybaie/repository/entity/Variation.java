package ua.kishkastrybaie.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "variation", schema = "main")
@Getter
@Setter
public class Variation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "variation_seq")
    @SequenceGenerator(
            name = "variation_seq",
            schema = "main",
            sequenceName = "variation_seq",
            allocationSize = 1
    )
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "variation")
    private Set<VariationOption> variationOptions;
}