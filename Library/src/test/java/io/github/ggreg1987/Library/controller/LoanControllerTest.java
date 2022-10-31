package io.github.ggreg1987.Library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ggreg1987.Library.businessRule.BusinessException;
import io.github.ggreg1987.Library.domain.entities.Book;
import io.github.ggreg1987.Library.domain.entities.Loan;
import io.github.ggreg1987.Library.domain.rest.controller.LoanController;
import io.github.ggreg1987.Library.domain.rest.dto.LoanDTO;
import io.github.ggreg1987.Library.domain.rest.dto.LoanFilterDTO;
import io.github.ggreg1987.Library.domain.rest.dto.ReturnedLoanDTO;
import io.github.ggreg1987.Library.domain.rest.service.BookService;
import io.github.ggreg1987.Library.domain.rest.service.LoanService;
import io.github.ggreg1987.Library.service.LoanServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    @Test
    @DisplayName("Should accomplish a Loan")
    public void createLoanTest() throws Exception {

        LoanDTO dto = LoanDTO.builder()
                .isbn("12345")
                .customer("Gregorio")
                .build();
        String json = new ObjectMapper().writeValueAsString(dto);
        var book = Book.builder().id(1L).isbn("12345").build();
        BDDMockito.given(bookService.getBookByIsbn("12345"))
                .willReturn(Optional.of(book));

        Loan loan = Loan.builder()
                .id(1L).customer("Gregorio")
                .book(book)
                .loanDate(LocalDate.now()).build();
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Should return an error when try loan book with wrong isbn.")
    public void invalidIsbnLoanTest() throws Exception {

        LoanDTO dto = LoanDTO.builder()
                .isbn("12345")
                .customer("Gregorio")
                .build();
        String json = new ObjectMapper().writeValueAsString(dto);
        var book = Book.builder().id(1L).isbn("12345").build();
        BDDMockito.given(bookService.getBookByIsbn("12345"))
                .willReturn(Optional.of(book));

        BDDMockito.given(bookService.getBookByIsbn("12345"))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]")
                        .value("Book nor found for passed isbn."));
    }

    @Test
    @DisplayName("Should return an error when try loan a book that is already on loaned.")
    public void loanedBookErrorOnCreateLoanTest() throws Exception {

        LoanDTO dto = LoanDTO.builder()
                .isbn("12345")
                .customer("Gregorio")
                .build();
        String json = new ObjectMapper().writeValueAsString(dto);
        var book = Book.builder().id(1L).isbn("12345").build();
        BDDMockito.given(bookService.getBookByIsbn("12345"))
                .willReturn(Optional.of(book));

        BDDMockito.given(loanService.save(Mockito.any(Loan.class)))
                .willThrow(new BusinessException("Book already loaned."));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]")
                        .value("Book already loaned."));
    }

    @Test
    @DisplayName("Should return a book.")
    public void returnBookTest() throws Exception {
        ReturnedLoanDTO dto = ReturnedLoanDTO
                .builder().returned(true).build();
        var loan = Loan.builder().id(1L).build();

        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.of(loan));

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc
                .perform(
                        patch(LOAN_API.concat("/1"))
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isOk());

        Mockito.verify(loanService, Mockito.times(1)).update(loan);

    }

    @Test
    @DisplayName("Should return a 404 error when try return a non-existent book")
    public void returnNonExistentBookTest() throws Exception {
        ReturnedLoanDTO dto = ReturnedLoanDTO
                .builder().returned(true).build();

        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc
                .perform(
                        patch(LOAN_API.concat("/1"))
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should filter a loan.")
    public void findBookTest() throws Exception {
        Long id = 1L;

        Loan loan = LoanServiceTest.createNewLoan();



        BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class),Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0,100),1));

        String queryString = String.format("?title=%s&author=%s&page=0&size=100",
                book.getTitle(),
                book.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryString))
                .accept(APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content",hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));

    }
}
