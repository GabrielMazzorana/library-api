package com.cursodsousa.libraryapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data  //cria alem do getter e setter, toString e equals e hashcode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity  //essa annotation diz ao jpa que essa classe é uma entidade
@Table
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //o banco se encarrega de gerar o valor dessa chave
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;
}
