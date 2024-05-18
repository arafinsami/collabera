package com.collabera.controller;

import com.collabera.dto.BookDTO;
import com.collabera.entity.AppUser;
import com.collabera.entity.Book;
import com.collabera.helper.BookHelper;
import com.collabera.mapper.BookMapper;
import com.collabera.service.BookService;
import com.collabera.validators.BookValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.collabera.exception.ApiError.fieldError;
import static com.collabera.utils.ResponseBuilder.error;
import static com.collabera.utils.ResponseBuilder.success;
import static com.collabera.utils.StringUtils.toJson;
import static org.springframework.http.ResponseEntity.badRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Bool API")
@RequestMapping(path = "book")
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    private final BookValidator validator;

    private final BookHelper helper;

    @GetMapping("/{id}")
    @Operation(summary = "get book by id")
    public ResponseEntity<JSONObject> findById(@PathVariable("id") String id) {
        Book book = bookService.findById(id);
        log.info("get book by id: {}, response: {} ", id, toJson(book));
        BookDTO dto = bookMapper.from(book);
        return new ResponseEntity<>(success(dto).getJson(), HttpStatus.OK);
    }

    @PostMapping("/save")
    @Operation(summary = "save book")
    public ResponseEntity<JSONObject> save(@Valid @RequestBody BookDTO request, BindingResult bindingResult) {
        ValidationUtils.invokeValidator(validator, request, bindingResult);

        if (bindingResult.hasErrors()) {
            return badRequest().body(error(fieldError(bindingResult)).getJson());
        }
        Book book = bookService.save(request);
        log.info("book save response: {} ", toJson(book));
        BookDTO dto = bookMapper.from(book);
        return new ResponseEntity<>(success(dto).getJson(), HttpStatus.CREATED);
    }

    @PostMapping("/borrow/{bookId}/{borrowerId}")
    @Operation(summary = "borrow book by book id and borrower id")
    public ResponseEntity<JSONObject> borrowBook(@PathVariable String bookId, @PathVariable String borrowerId) {
        bookService.borrowBook(bookId, borrowerId);
        return ResponseEntity.ok(success("Book borrowed successfully with bookId: " + bookId).getJson());
    }

    @PostMapping("/return/{bookId}/{borrowerId}")
    @Operation(summary = "return book by book id and borrower id")
    public ResponseEntity<JSONObject> returnBook(@PathVariable String bookId, @PathVariable String borrowerId) {
        bookService.returnBook(bookId, borrowerId);
        return ResponseEntity.ok(success("Book returned successfully with bookId: " + bookId).getJson());
    }

    @GetMapping("/books")
    @Operation(summary = "get all paginated books by page and size")
    public ResponseEntity<JSONObject> findById(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Map<Object, Object> responseMap = new HashMap<>();
        Page<Book> books = bookService.findAll(page, size);
        helper.pagination(page, books, responseMap);
        return new ResponseEntity<>(success(responseMap).getJson(), HttpStatus.OK);
    }
}
