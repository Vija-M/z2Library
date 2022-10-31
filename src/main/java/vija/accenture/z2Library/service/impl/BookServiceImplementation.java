package vija.accenture.z2Library.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vija.accenture.z2Library.mappers.BookMapStructMapper;
import vija.accenture.z2Library.service.BookService;
import vija.accenture.z2Library.model.Book;
import vija.accenture.z2Library.repository.BookRepository;
import vija.accenture.z2Library.repository.model.BookDAO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BookServiceImplementation implements BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookMapStructMapper bookMapper;

    @Override
    public Optional<Book> getBookById(Long id) {
        Optional<Book> bookById = bookRepository.findById(id)
                .flatMap(book -> Optional.ofNullable(bookMapper.bookDAOToBook(book)));
        log.info("Book with id {} is {}", id, bookById);
        return bookById;
    }

    @Override
    public List<Book> getAllBooks() {
        List<BookDAO> bookDAOList = bookRepository.findAll();
        log.info("Get book list. Size is: {}", bookDAOList::size);
        return bookDAOList.stream().map(bookMapper::bookDAOToBook).collect(Collectors.toList());
    }


    @Override
    public Book saveBook(Book book) {
        BookDAO bookSaved = bookRepository.save(bookMapper.bookToBookDAO(book));
        log.info("My new book saved: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
        log.info("Book with id {} is deleted", id);
    }

}
