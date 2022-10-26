package vija.accenture.z2Library.repository.model;

import lombok.*;
import vija.accenture.z2Library.model.Cover;
import vija.accenture.z2Library.model.Genre;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class BookDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;


    @Column(name = "author", length = 50, nullable = false)
    private String author;

   @Enumerated(EnumType.STRING)
    @Column(name = "genre", length = 25)
    private Genre genre;
    //  private String genre;

    @Column(name = "pages", length = 4)
    private int pages;

    @Enumerated(EnumType.STRING)
    @Column(name = "cover", length = 15)
   private Cover cover;
    //private String cover;

    @Column(name = "shelf", length = 5, nullable = false)
    private String shelf;
}

