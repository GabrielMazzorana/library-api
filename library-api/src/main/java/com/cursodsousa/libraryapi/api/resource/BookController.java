package com.cursodsousa.libraryapi.api.resource;

import com.cursodsousa.libraryapi.api.dto.BookDTO;
import com.cursodsousa.libraryapi.api.exception.ApiErros;
import com.cursodsousa.libraryapi.entity.Book;
import com.cursodsousa.libraryapi.exception.BusinessException;
import com.cursodsousa.libraryapi.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  //é importante receber e retornar um dto para nao expor as entidades
    public BookDTO create(@RequestBody @Valid BookDTO dto){  //@Valid, serve para validar os campos com o NotEmpy do BookDTO
        Book entity = modelMapper.map(dto, Book.class);  //pega o dto dos parenteses cria uma instancia de Book
        entity = service.save(entity); //e transfere todas as propriedades de mesmo nome do dto para o Book
        return modelMapper.map(entity, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex){  //essa exception MethodArgumentNotValidException valida com o @Valid toda vez que não vier os campos do DTO
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErros(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleBusinessException(BusinessException ex){
        return new ApiErros(ex);
    }
}
