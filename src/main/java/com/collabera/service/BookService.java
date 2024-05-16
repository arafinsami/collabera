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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        List<Book> existingBooks = bookRepository.findByIsbn(request.getIsbn());
        for (Book existingBook : existingBooks) {
            if (existingBook.getTitle().equals(request.getTitle()) && existingBook.getAuthor().equals(request.getAuthor())) {
                existingBook.setQuantity(existingBook.getQuantity() + 1);
                return bookRepository.save(existingBook);
            }
        }
        Book book = bookMapper.save(request);
        log.info("book saved : {} ", toJson(book));
        return bookRepository.save(book);
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

    public void borrowBook(String bookId, AppUser user) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<AppUser> borrowerOptional = appUserRepository.findById(user.getId());
        if (bookOptional.isPresent() && borrowerOptional.isPresent()) {
            Book book = bookOptional.get();
            AppUser borrower = borrowerOptional.get();
            if (book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);
                bookRepository.save(book);
                borrower.getBorrowedBooks().add(book);
                appUserRepository.save(borrower);
            } else {
                throw new RuntimeException("Book with id " + bookId + " is not available for borrowing.");
            }
        } else {
            throw new RuntimeException("Book or Borrower not found.");
        }
    }

    public void returnBook(String bookId, AppUser user) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<AppUser> borrowerOptional = appUserRepository.findById(user.getId());
        if (bookOptional.isPresent() && borrowerOptional.isPresent()) {
            Book book = bookOptional.get();
            AppUser borrower = borrowerOptional.get();
            borrower.getBorrowedBooks().remove(book);
            appUserRepository.save(borrower);
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Book or Borrower not found.");
        }
    }
}
