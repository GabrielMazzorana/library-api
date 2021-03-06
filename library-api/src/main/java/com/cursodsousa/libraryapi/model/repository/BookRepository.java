package com.cursodsousa.libraryapi.model.repository;

import com.cursodsousa.libraryapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(Long id);
}
