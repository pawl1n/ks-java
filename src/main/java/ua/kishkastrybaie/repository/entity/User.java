package ua.kishkastrybaie.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "main")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            schema = "main",
            allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String middleName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    @JsonIgnore
    private String password;
}
