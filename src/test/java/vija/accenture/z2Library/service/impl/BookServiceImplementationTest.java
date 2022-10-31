package vija.accenture.z2Library.service.impl;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import vija.accenture.z2Library.mappers.BookMapStructMapper;
import vija.accenture.z2Library.model.Genre;
import vija.accenture.z2Library.repository.BookRepository;
import vija.accenture.z2Library.repository.model.BookDAO;
import vija.accenture.z2Library.model.Book;
import vija.accenture.z2Library.model.Cover;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BookServiceImplementationTest {

        @InjectMocks
        private BookServiceImplementation bookServiceImpl;
        @Mock
        private BookRepository bookRepository;
        @Mock
        private BookMapStructMapper bookMapper;
        @Rule
        public final ExpectedException exception = ExpectedException.none();


        private Book book;
        private BookDAO bookDAO;
        private List<Book> bookList;
        private List<BookDAO> bookDAOList;

        @BeforeEach
        public void init() {
            book = getBook(101L, "title", "author", Genre.valueOf("THRILLER"), 13, Cover.valueOf("SOFT"),"A1A");
            bookDAO = getBookDAO(101L, "title", "author", Genre.THRILLER, 13, Cover.SOFT,"A1A" );
            bookList = getBookList(book);
            bookDAOList = getBookDAOList(bookDAO);
        }

        @Test
         void getALlBooksSuccess() {
            when(bookRepository.findAll()).thenReturn(bookDAOList);
            when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
            List<Book> bookList = bookServiceImpl.getAllBooks();
            assertEquals(2, bookList.size());
            assertEquals("title", bookList.get(0).getTitle());
        }

        @Test
        void getAllBooksUnsuccessful() {
            when(bookRepository.findAll()).thenReturn(new ArrayList<>());
            List<Book> list = bookServiceImpl.getAllBooks();
            assertEquals(0, list.size());
        }

        @Test
        void getBookByIdSuccess() {
            when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookDAO));
            when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
            Optional<Book> optionalBookRole = bookServiceImpl.getBookById(anyLong());
            optionalBookRole.ifPresent(projectRole -> assertEquals("title", projectRole.getTitle()));
        }

        @Test
        void getBookByIdUnsuccessful() {
            when(bookRepository.findById(any())).thenReturn(Optional.empty());
            Optional<Book> book = bookServiceImpl.getBookById(any());
            Assertions.assertFalse(book.isPresent());
        }

        @Test
        void saveBookSuccess() {
            when(bookRepository.save(bookDAO)).thenReturn(bookDAO);
            when(bookMapper.bookDAOToBook(bookDAO)).thenReturn(book);
            when(bookMapper.bookToBookDAO(book)).thenReturn(bookDAO);
            Book savedBook = bookServiceImpl.saveBook(book);
//        assertEquals("name", savedProduct.getName());
            assertAll("product",
                    () -> assertEquals(bookDAO.getId(), savedBook.getId()),
                    () -> assertEquals(bookDAO.getTitle(), savedBook.getTitle()),
                    () -> assertEquals(bookDAO.getAuthor(), savedBook.getAuthor()),
                    () -> assertEquals(bookDAO.getGenre(), savedBook.getGenre()),
                    () -> assertEquals(bookDAO.getPages(), savedBook.getPages()),
                    () -> assertEquals(bookDAO.getCover(), savedBook.getCover()),
                    () -> assertEquals(bookDAO.getShelf(), savedBook.getShelf())
            );
            verify(bookRepository, times(1)).save(bookDAO);
        }

        @Test
        void saveBookUnsuccessful() {
            when(bookRepository.findAll()).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT));
            try {
                bookServiceImpl.saveBook(book);
            } catch (Exception e) {
                assertEquals("409 CONFLICT", e.getMessage());
            }
        }

        @Test
        void deleteBookSuccess() {
            bookServiceImpl.deleteBookById(anyLong());
            verify(bookRepository, times(1)).deleteById(anyLong());
        }

        @Test
        void deleteBookUnsuccessful() {
            bookServiceImpl.deleteBookById(null);
            exception.expect(IllegalArgumentException.class);
        }


        public Book getBook(Long id, String title, String author, Genre genre, int pages, Cover cover, String shelf) {
            Book book = new Book();
            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setPages(pages);
            book.setCover(cover);
            book.setShelf(shelf);
            return book;
        }

        public BookDAO getBookDAO(Long id, String title, String author, Genre genre, int pages, Cover cover, String shelf) {
            BookDAO book = new BookDAO();
            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setPages(pages);
            book.setCover(cover);
            book.setShelf(shelf);
            return book;
        }

        public List<Book> getBookList(Book book) {
            List<Book> bookList = new ArrayList<>();
            bookList.add(book);
            bookList.add(book);
            return bookList;
        }

        public List<BookDAO> getBookDAOList(BookDAO book) {
            List<BookDAO> bookList = new ArrayList<>();
            bookList.add(book);
            bookList.add(book);
            return bookList;
        }

}