package com.collabera.service;

import com.collabera.dto.BookDTO;
import com.collabera.entity.AppUser;
import com.collabera.entity.Book;
import com.collabera.exception.ResourceNotFoundException;
import com.collabera.mapper.BookMapper;
import com.collabera.repository.AppUserRepository;
import com.collabera.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.collabera.utils.StringUtils.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final AppUserRepository appUserRepository;

    @Transactional
    public Book save(BookDTO request) {
        return bookRepository.findByIsbn(request.getIsbn()).stream()
                .filter(book -> book.getTitle().equals(request.getTitle()) && book.getAuthor().equals(request.getAuthor()))
                .findFirst()
                .map(book -> {
                    book.setQuantity(book.getQuantity() + request.getQuantity());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    Book newBook = bookMapper.save(request);
                    log.info("book saved : {} ", toJson(newBook));
                    return bookRepository.save(newBook);
                });
    }

    @Transactional(readOnly = true)
    public Book findById(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> {
            log.error("book not found with id: {}", id);
            return new ResourceNotFoundException("book not found with id: " + id);
        });
        log.info("get book by id: {}, book: {} ", id, toJson(book));
        return book;
    }

    @Transactional(readOnly = true)
    public List<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Transactional
    public void borrowBook(String bookId, String borrowerId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new RuntimeException("Book with id " + bookId + " not found.")
        );
        AppUser borrower = appUserRepository.findById(borrowerId).orElseThrow(
                () -> new RuntimeException("Borrower not found.")
        );
        if (book.getQuantity() > 0) {
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
            borrower.getBorrowedBooks().add(book);
            appUserRepository.save(borrower);
        } else {
            throw new RuntimeException("Book with id " + bookId + " is not available for borrowing.");
        }
    }

    @Transactional
    public void returnBook(String bookId, String borrowerId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new RuntimeException("Book with id " + bookId + " not found.")
        );
        AppUser borrower = appUserRepository.findById(borrowerId).orElseThrow(
                () -> new RuntimeException("Borrower not found.")
        );
        borrower.getBorrowedBooks().remove(book);
        appUserRepository.save(borrower);
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Page<Book> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }
}
