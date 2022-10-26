package vija.accenture.z2Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vija.accenture.z2Library.repository.model.BookDAO;

@Repository
public interface BookRepository extends JpaRepository<BookDAO, Long> {
}
