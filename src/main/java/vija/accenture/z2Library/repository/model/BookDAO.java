package vija.accenture.z2Library.repository.model;

import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.stereotype.Component;
import vija.accenture.z2Library.model.Cover;
import vija.accenture.z2Library.model.Genre;

import javax.persistence.*;

//@EqualsAndHashCode
@Data
@Component
@NoArgsConstructor
//@Getter
//@Setter
//@ToString
@Entity
@Table(name = "myBook")
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
    @ColumnTransformer(read = "UPPER(genre)", write = "LOWER(?)")
    private Genre genre;

    @Column(name = "pages", length = 4)
    private int pages;

    @Enumerated(EnumType.STRING)
    @Column(name = "cover", length = 15)
    @ColumnTransformer(read = "UPPER(cover)", write = "LOWER(?)")
    private Cover cover;

    @Column(name = "shelf", length = 5, nullable = false)
    private String shelf;

    public BookDAO(Long id, String title, String author, Genre genre, int pages, Cover cover, String shelf) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pages = pages;
        this.cover = cover;
        this.shelf = shelf;
    }

    public BookDAO(String title, String author, Genre genre, int pages, Cover cover, String shelf) {

        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pages = pages;
        this.cover = cover;
        this.shelf = shelf;
    }
}

