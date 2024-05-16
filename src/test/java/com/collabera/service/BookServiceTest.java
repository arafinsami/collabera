package com.collabera.service;

import com.collabera.dto.BookDTO;
import com.collabera.entity.Book;
import com.collabera.mapper.BookMapper;
import com.collabera.repository.AppUserRepository;
import com.collabera.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void save_NewBookSavedSuccessfully() {
        BookDTO request = getBookDTO();
        Book book = getBook(request);
        when(bookMapper.save(request)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book savedBook = bookService.save(request);
        assertEquals(book, savedBook);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void findById_ExistingBookFound() {
        String bookId = "140c7291-5afc-49c1-a253-cb156737aa81";
        Book book = new Book();
        book.setId(bookId);
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setQuantity(1);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Book foundBook = bookService.findById(bookId);
        assertEquals(book, foundBook);
    }

    @Test
    void findByIsbn_ExistingBooksFound() {
        String isbn = "1234567890";
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setId("140c7291-5afc-49c1-a253-cb156737aa81");
        book.setIsbn(isbn);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setQuantity(1);
        books.add(book);
        when(bookRepository.findByIsbn(isbn)).thenReturn(books);
        List<Book> foundBooks = bookService.findByIsbn(isbn);
        assertEquals(books, foundBooks);
    }

    private static Book getBook(BookDTO request) {
        Book book = new Book();
        book.setId("1");
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setQuantity(request.getQuantity());
        return book;
    }

    private static BookDTO getBookDTO() {
        BookDTO request = new BookDTO();
        request.setIsbn("1234567890");
        request.setTitle("Test Book");
        request.setAuthor("Test Author");
        request.setQuantity(1);
        return request;
    }
}

