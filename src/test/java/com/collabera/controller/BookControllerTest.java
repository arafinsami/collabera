package com.collabera.controller;

import com.collabera.dto.BookDTO;
import com.collabera.entity.Book;
import com.collabera.mapper.BookMapper;
import com.collabera.service.BookService;
import com.collabera.validators.BookValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    BookMapper bookMapper;

    @Mock
    private BookService bookService;

    @Mock
    private BookValidator validator;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setValidator(validator)
                .build();
    }

    @Test
    public void testFindById() throws Exception {
        String bookId = "140c7291-5afc-49c1-a253-cb156737aa81";
        Book book = new Book();
        book.setId(bookId);
        lenient().when(bookService.findById(bookId)).thenReturn(book);
        BookDTO bookDTO = getBookDTO(bookId);
        lenient().when(bookMapper.from(book)).thenReturn(bookDTO);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        mockMvc.perform(get("/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSave_Success() throws Exception {
        when(validator.supports(BookDTO.class)).thenReturn(true);
        BindingResult bindingResult = mock(BindingResult.class);
        lenient().when(bindingResult.hasErrors()).thenReturn(false);
        when(bookService.save(any(BookDTO.class))).thenReturn(new Book());
        lenient().when(bookMapper.from(any(Book.class))).thenReturn(new BookDTO());
        mockMvc.perform(post("/book/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new BookDTO())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();
        verify(bookService, times(1)).save(any(BookDTO.class));
    }

    @Test
    public void testBorrowBook_ValidInput() throws Exception {
        String bookId = "bookId";
        String borrowerId = "borrowerId";
        doNothing().when(bookService).borrowBook(bookId, borrowerId);
        mockMvc.perform(post("/book/borrow/{bookId}/{borrowerId}", bookId, borrowerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data")
                        .value(
                                "Book borrowed successfully with bookId: "
                                        + bookId
                                        + " borrowerId: "
                                        + borrowerId
                        )
                );
    }

    @Test
    public void testReturnBook_ValidInput() throws Exception {
        String bookId = "bookId";
        String borrowerId = "borrowerId";
        doNothing().when(bookService).returnBook(bookId, borrowerId);
        mockMvc.perform(post("/book/return/{bookId}/{borrowerId}", bookId, borrowerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data")
                        .value(
                                "Book returned successfully with bookId: "
                                        + bookId
                                        + " and borrowerId: "
                                        + borrowerId
                        )
                );
    }

    private static BookDTO getBookDTO(String bookId) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(bookId);
        bookDTO.setIsbn("ISBN-123");
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setQuantity(5);
        return bookDTO;
    }
}
