package com.collabera.mapper;

import com.collabera.dto.BookDTO;
import com.collabera.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book save(BookDTO request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setQuantity(request.getQuantity());
        return book;
    }

    public BookDTO from(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setQuantity(book.getQuantity());
        return dto;
    }
}
