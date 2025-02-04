package org.gym.controller;

import lombok.RequiredArgsConstructor;
import org.gym.dto.TraineeDto;
import org.gym.entity.Trainee;
import org.gym.facade.TraineeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
        //value = "/books", consumes = {"application/JSON"},
        //produces = {"application/JSON", "application/XML"})
public class TraineeController {

    private final TraineeFacade traineeFacade;
    private final TransactionIdGenerator transactionIdGenerator;

//    @Autowired
//    private Convertor<TraineeDto, Trainee> convertor;
//
//    @GetMapping
//    ResponseEntity<List<BookDTO>> getAll(
//            @RequestParam(defaultValue = "10", required = false) int limit,
//            @RequestParam(defaultValue = "5", required = false) int offset) {
//        List<Book> bookList = traineeFacade.getAllBooks(limit, offest);
//        List<BookDTO> bookDTOList = convertor.convertModelListToDtoList(bookList);
//        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/{id:\\d+}")
//    ResponseEntity<List<BookDTO>> getById(@PathVariable long id) {
//        Book book = traineeFacade.getBookById(id);
//        BookDTO bookDTO = convertor.convertModelToDto(book);
//        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
//    }
//
//    @PostMapping
//    ResponseEntity<BookDTO> create(@RequestBody BookCreateDTO bookCreateDTO) {
//        Book book = convertor.convertDtoToModel(bookCreateDTO);
//        Book createdBook = traineeFacade.createBook(book);
//        return new ResponseEntity<>(convertor.convertModelToDto(createdBook), HttpStatus.CREATE);
//    }
//
//    @PutMapping(value = "/{id:\\d+}")
//    ResponseEntity<BookDTO> update(@PathVariable long id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
//        Book book = convertor.convertDtoToModel(bookUpdateDTO);
//        Book updatedBook = traineeFacade.updateBook(book);
//        return new ResponseEntity<>(convertor.convertModelToDto(updatedBook), HttpStatus.OK);
//    }
//
//    @PatchMapping(value = "/{id:\\d+}")
//    ResponseEntity<BookDTO> patch(@PathVariable long id, @Valid @RequestBody BookPatchDTO bookPatchDTO) {
//        Book book = convertor.convertDtoToModel(bookPatchDTO);
//        Book patchedBook = traineeFacade.patchBook(id, book);
//        return new ResponseEntity<>(convertor.convertModelToDto(patchedBook), HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/{id:\\d+}")
//    ResponseEntity<V> delete(@PathVariable long id) {
//        traineeFacade.deleteBok(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}