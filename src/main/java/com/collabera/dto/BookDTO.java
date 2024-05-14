package com.collabera.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BookDTO {

    private String id;

    @NotNull
    @NotBlank
    private String isbn;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String author;
}
