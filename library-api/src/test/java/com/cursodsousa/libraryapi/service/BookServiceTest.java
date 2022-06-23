package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.entity.Book;
import com.cursodsousa.libraryapi.exception.BusinessException;
import com.cursodsousa.libraryapi.model.repository.BookRepository;
import com.cursodsousa.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach  //essa annotation faz com que o método seja executado antes de cada teste
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);  //quando o repository executar o existsByIsbn passando qualquer string retorne verdadeiro
        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1l).isbn("123").title("As meninas").author("Fulano").build());
        //O mockito a cima é assim: quando(when) chamar o save então(thenReturn) tem retornar todas essas informações

        //execução
        Book savedBook = service.save(book);

        //verificação
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("As meninas");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As meninas").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplo")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);  //quando o repository executar o existsByIsbn passando qualquer string retorne verdadeiro

        //execucao
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificacoes
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class)   //assertThat(verifique que) hasMassage(possua a mesma mensagem que foi criada)
        .hasMessage("Isbn já cadastrado");

        Mockito.verify(repository, Mockito.never()).save(book);  //verifique que o meu repository nunca chame o método save
    }
}
