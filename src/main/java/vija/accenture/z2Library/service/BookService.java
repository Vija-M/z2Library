package vija.accenture.z2Library.service;

import vija.accenture.z2Library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> getBookById(Long id);

    List<Book> getAllBooks();

    Book saveBook(Book book);

    void deleteBookById(Long id);
}
