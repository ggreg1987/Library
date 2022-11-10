package io.github.ggreg1987.Library.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;

    @Column(name = "customer_email",length = 50)
    private String customerEmail;


}
