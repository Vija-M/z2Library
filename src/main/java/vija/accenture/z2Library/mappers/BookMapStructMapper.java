package vija.accenture.z2Library.mappers;

import org.mapstruct.Mapper;
import vija.accenture.z2Library.model.Book;
import vija.accenture.z2Library.repository.model.BookDAO;

@Mapper(componentModel = "spring")
public interface BookMapStructMapper {
    BookDAO bookToBookDAO(Book book);

    Book bookDAOToBook(BookDAO bookDAO);
}
