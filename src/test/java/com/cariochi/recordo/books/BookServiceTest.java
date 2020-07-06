package com.cariochi.recordo.books;

import com.cariochi.recordo.RecordoExtension;
import com.cariochi.recordo.books.dto.Author;
import com.cariochi.recordo.books.dto.Book;
import com.cariochi.recordo.given.Given;
import com.cariochi.recordo.verify.Expected;
import com.cariochi.recordo.verify.Verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
            @Verify("/books/book.json") Expected<Book> expected
    ) {
        expected.assertEquals(bookService.findById(1L));
    }

    @Test
    void should_get_books_by_author(
            @Given("/books/author.json") Author author,
            @Verify(value = "/books/short_books.json", included = {"id", "title", "author.id"}) Expected<List<Book>> expected
    ) {
        expected.assertEquals(bookService.findAllByAuthor(author));
    }

    @Test
    void should_create_book(
            @Given("/books/new_book.json") Book book,
            @Given("/books/author.json") Author author,
            @Verify(value = "/books/created_book.json", excluded = "id") Expected<Book> expected
    ) {
        when(authorService.findById(book.getAuthor().getId())).thenReturn(author);
        expected.assertEquals(bookService.create(book));
    }

    @Test
    void should_add_book_to_shelf(
            @Given("/books/book.json") Book book,
            @Given("/books/books.json") List<Book> books,
            @Verify("/books/expected_books.json") Expected<List<Book>> expected
    ) {
        expected.assertEquals(bookService.merge(books, book));
    }
}

