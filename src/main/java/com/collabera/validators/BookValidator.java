package com.collabera.validators;

import com.collabera.dto.BookDTO;
import com.collabera.entity.Book;
import com.collabera.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookValidator implements Validator {

    private final BookService bookService;

    @Override
    public boolean supports(Class<?> clazz) {
        return BookDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDTO dto = (BookDTO) target;
        List<Book> existingBooks = bookService.findByIsbn(dto.getIsbn());
        for (Book existingBook : existingBooks) {
            if (!existingBook.getTitle().equals(dto.getTitle()) || !existingBook.getAuthor().equals(dto.getAuthor())) {
                errors.rejectValue("isbn", "", "Book with this ISBN already exists with different title or author");
                break;
            }
        }
    }
}
