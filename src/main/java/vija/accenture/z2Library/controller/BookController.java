package vija.accenture.z2Library.controller;

import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vija.accenture.z2Library.model.Book;
import vija.accenture.z2Library.service.BookService;
import vija.accenture.z2Library.swagger.DescriptionVariables;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Api(tags = {DescriptionVariables.BOOK})
@Log4j2
@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/all")
    @ApiOperation(value = "Get all books",
            notes = "Returns the entire list of books",
            response = Book.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Book.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("Retrieve list of Books");
        List<Book> bookList = bookService.getAllBooks();
        if (bookList.isEmpty()) {
            log.warn("Book list is empty! {}", bookList);
            return ResponseEntity.notFound().build();
        }
        log.debug("Books list is found. Size: {}", bookList::size);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get books by ID",
            notes = "Enter ID to search specific books",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<Book> getProductById(@ApiParam(value = "book ID", required = true)
                                                         @NonNull @PathVariable Long id) {
        log.info("Get book by passing ID, where book ID is :{} ", id);
        Optional<Book> book = (bookService.getBookById(id));
        if (!book.isPresent()) {
            log.warn("Book with ID {} is not found.", id);
        } else {
            log.debug("Book with ID {} is found: {}", id, book);
        }
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves book in database",
            notes = "If provided valid book, saves it",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The book is successfully saved"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book, BindingResult bindingResult) {
        log.info("Create new book by passing : {}", book);
        if (bindingResult.hasErrors()) {
            log.error("New book is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Book bookSaved = bookService.saveBook(book);
        log.debug("New book is created: {}", book);
        return new ResponseEntity<>(bookSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the book by id",
            notes = "Deletes the book if provided id exists",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The book is successfully deleted"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBookById(@ApiParam(value = "The id of the book", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Delete book by passing ID, where ID is:{}", id);
        Optional<Book> product = bookService.getBookById(id);
        if (!(product.isPresent())) {
            log.warn("Book for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBookById(id);
        log.debug("Book with id {} is deleted: {}", id, product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the book by id",
            notes = "Updates the book if provided id exists",
            response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The book is successfully updated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Book> updateBookById(@ApiParam(value = "id of the book", required = true)
                                                       @NonNull @PathVariable Long id,
                                                       @Valid @RequestBody Book book, BindingResult bindingResult)  {
        log.info("Update existing book with ID: {} and new body: {}", id, book);
        if (bindingResult.hasErrors() || !id.equals(book.getId())) {
            log.warn("Book for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        bookService.saveBook(book);
        log.debug("Book with id {} is updated: {}", id, book);
        return new ResponseEntity<>(book, HttpStatus.ACCEPTED);
    }

}
