package com.cariochi.recordo.books;

import com.cariochi.recordo.Given;
import com.cariochi.recordo.RecordoExtension;
import com.cariochi.recordo.books.dto.Author;
import com.cariochi.recordo.books.dto.Book;
import com.cariochi.recordo.given.Assertion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(RecordoExtension.class)
class BookServiceTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    @Test
    void should_get_book_by_id(
            @Given("/books/book.json") Assertion<Book> assertion
    ) {
        final Book actual = bookService.findById(1L);
        assertion.assertAsExpected(actual);
    }

    @Test
    void should_get_books_by_author(
            @Given("/books/author.json") Author author,
            @Given("/books/short_books.json") Assertion<Page<Book>> assertion
    ) {
        assertion
                .included("content.id", "content.title", "content.author.id")
                .assertAsExpected(bookService.findAllByAuthor(author));
    }

    @Test
    void should_create_book(
            @Given("/books/new_book.json") Book book,
            @Given("/books/author.json") Author author,
            @Given("/books/created_book.json") Assertion<Book> assertion
    ) {
        when(authorService.findById(book.getAuthor().getId())).thenReturn(author);
        assertion
                .excluded("id")
                .assertAsExpected(bookService.create(book));
    }

    @Test
    void should_add_book_to_shelf(
            @Given("/books/book.json") Book book,
            @Given("/books/books.json") List<Book> books,
            @Given("/books/expected_books.json") Assertion<Page<Book>> assertion
    ) {
        assertion.assertAsExpected(bookService.merge(books, book));
    }
}

