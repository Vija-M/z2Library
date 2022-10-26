package vija.accenture.z2Library.mappers;

import org.mapstruct.Mapper;
import vija.accenture.z2Library.model.Book;
import vija.accenture.z2Library.repository.model.BookDAO;

@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {
    BookDAO productToProductDAO(Book product);

    Book productDAOToProduct(BookDAO productDAO);
}
