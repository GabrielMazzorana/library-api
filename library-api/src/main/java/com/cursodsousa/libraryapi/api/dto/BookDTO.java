package com.cursodsousa.libraryapi.api.dto;

import lombok.*;

@Getter
@Setter   //annotation lombok set e get
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    private String isbn;
}
