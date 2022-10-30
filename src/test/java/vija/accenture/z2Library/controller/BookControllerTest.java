package vija.accenture.z2Library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import vija.accenture.z2Library.model.Book;
import vija.accenture.z2Library.model.Cover;
import vija.accenture.z2Library.model.Genre;
import vija.accenture.z2Library.service.impl.BookServiceImplementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    public static String URL = "/api/book";

    private Book book;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController controller;

    @MockBean
    private BookServiceImplementation serviceImpl;
    public List<Book> bookList = new ArrayList<>();

    @Test
    void getAllBooks() throws Exception {
        bookList.add(testBook());
        when(serviceImpl.getAllBooks()).thenReturn(bookList);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBooksNoData() throws Exception {
        when(serviceImpl.getAllBooks()).thenReturn(Collections.EMPTY_LIST);
        mockMvc.perform(get(URL + "/all"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBookById() throws Exception {
        when(serviceImpl.getBookById(anyLong())).thenReturn(Optional.of(testBook()));
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookByIdNotFound() throws Exception {
        when(serviceImpl.getBookById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBookSuccess() throws Exception {
        when(serviceImpl.saveBook(any(Book.class))).thenReturn(testBook());
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(getContent(testBook()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    void saveBookEmptyTitle() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(testEmptyTitle())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveBookEmptyAuthor() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(testEmptyAuthor())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveBookEmptyShelf() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(testEmptyShelf())))
                .andExpect(status().isBadRequest());
    }

  /*  @Test
    void updateBookByIdSuccess() throws Exception {
        Book book = testBook();
        when(serviceImpl.getBookById(book.getId())).thenReturn(Optional.of(book));
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/put/1")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated());
        verify(serviceImpl, times(1)).saveBook(book);
    }
*/
    @Test
    void UpdateBookByIdUnsuccessful() throws Exception {
        Book book = testBook();
        book.setId(null);
        when(serviceImpl.getBookById(null)).thenReturn(Optional.empty());
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/put/1")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(serviceImpl, times(0)).saveBook(book);
    }

    @Test
    void deleteBook() throws Exception {
        Optional<Book> book = Optional.of(testBook());
        when(serviceImpl.getBookById(anyLong())).thenReturn(book);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(serviceImpl, times(1)).deleteBookById(anyLong());
    }

    @Test
    void deleteBookUnsuccessful() throws Exception {
        Optional<Book> book = Optional.of(testBook());
        book.get().setId(null);
        when(serviceImpl.getBookById(null)).thenReturn(book);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + null)
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(serviceImpl, times(0)).deleteBookById(null);
    }


    private Book testBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("War in Red");
        book.setAuthor("O.Mao");
        book.setCover(Cover.HARD);
                //(Cover.valueOf("HARD"));
        book.setGenre(Genre.THRILLER);
                //(Genre.valueOf("HISTORICAL_FICTION"));
        book.setPages(235);
        book.setShelf("C2F");
        return book;
    }

    private Book testEmptyTitle() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("");
        book.setAuthor("O.Mao");
        book.setCover(Cover.HARD);
        book.setGenre(Genre.HISTORICAL_FICTION);
        book.setPages(235);
        book.setShelf("C2F");
        return book;
    }

    private Book testEmptyAuthor() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("War in Green");
        book.setAuthor("");
        book.setCover(Cover.HARD);
        book.setGenre(Genre.HISTORICAL_FICTION);
        book.setPages(225);
        book.setShelf("C2F");
        return book;
    }

    private Book testEmptyShelf() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("War in Brown");
        book.setAuthor("O.Mao");
        book.setCover(Cover.SOFT);
        book.setGenre(Genre.HISTORICAL_FICTION);
        book.setPages(225);
        book.setShelf("");
        return book;
    }


    private List<Book> testBookList() {
        List<Book> list = new ArrayList<>();
        return list;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getContent(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}


