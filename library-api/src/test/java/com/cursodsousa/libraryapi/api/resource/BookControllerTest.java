package com.cursodsousa.libraryapi.api.resource;

import com.cursodsousa.libraryapi.api.dto.BookDTO;
import com.cursodsousa.libraryapi.entity.Book;
import com.cursodsousa.libraryapi.exception.BusinessException;
import com.cursodsousa.libraryapi.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.regex.Matcher;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)  //essa annotation diz que o spring cria um contexto para rodar os testes
@ActiveProfiles("test")   //diz que é para rodar com perfil de teste, ou seja, ambiente de teste
@WebMvcTest
@AutoConfigureMockMvc   //o spring configura um objeto para gente estar fazendo as variaveis
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired   //serve para injetar uma dependencia
    MockMvc mvc;   //esse objeto simula como se fosse uma requisicao para a api

    @MockBean  //MockBean é um método para criar uma instancia de service
    BookService service;  //Mocks: são implementações fakes que podem alterar o seu comportamento

    @Test   //para criar uma variavel copiar o q quer botao direito e introduce variable
    @DisplayName("Deve criar um livro com sucesso.")  //define o que o metodo esta testando
    public void createBookTest() throws Exception{  //MockMvcRequestBuilders serve para definir uma requisição

        BookDTO dto = createNewBook();
        Book savedBook = Book.builder().id(10L).author("Artur").title("As aventuras").isbn("001").build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);    //esse writeValueAsString recebe um objeto e transforma em json

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(10L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para a criação do livro.")  //define o que o metodo esta testando
    public void createInvalidTest() throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao cadastrar um livro com isbn que já existe")
    public void createBookWithDuplicatedIsbn() throws JsonProcessingException {

        BookDTO dto = createNewBook();

        String json = new ObjectMapper().writeValueAsString(new BookDTO());
        String mensagemErro = "Isbn já cadastrado";
        BDDMockito.given(service.save(Mockito.any(Book.class))).  //BusinessException é igual a erro da regra de negócio
                willThrow(new BusinessException(mensagemErro));   //given(quando) o service for salvar qualquer Book vai lançar(willThrow)

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(mensagemErro));
    }

    private BookDTO createNewBook() {
        return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
    }
}
