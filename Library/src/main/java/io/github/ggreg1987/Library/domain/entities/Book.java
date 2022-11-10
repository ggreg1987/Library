package io.github.ggreg1987.Library.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,nullable = false)
    private String author;
    @Column(length = 100,nullable = false)
    private String title;
    @Column(length = 10,nullable = false)
    private String isbn;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;
}
