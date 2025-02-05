package org.gym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeDto;
import org.gym.dto.UserDto;
import org.gym.facade.TraineeFacade;

import org.gym.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainees",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
//@Tag(name = "Trainees", description = "Operations related to managing trainees")
public class TraineeController {

    private final TraineeFacade traineeFacade;
    private final TraineeService traineeService;
    private final TransactionIdGenerator transactionIdGenerator;

    @GetMapping(value ="/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeDto> getSimpleData(){
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        TraineeDto traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        return new ResponseEntity<>(traineeDto, HttpStatus.OK);
    }

    @PostMapping(value ="/data2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeDto> getSimpleData2(){
        UserDto userDto = new UserDto("Maria", "Petrenko", "Maria.Petrenko", true);
        TraineeDto traineeDto = TraineeDto.builder()
                .user(userDto)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();

        return new ResponseEntity<>(traineeDto, HttpStatus.OK);
    }

    @GetMapping("login/{username}/{password}")
    public ResponseEntity<Void> login(@PathVariable("username") String userName,
                                      @PathVariable("password") String password) {
        //traineeFacade.authenticate(userName, password);
        System.out.println(userName + " : " + password);
        return ResponseEntity.ok().build();
    }
//
//    @GetMapping("/{username}/{password}")
//    public ResponseEntity<TraineeDto> getTrainee(@PathVariable("username") String userName,
//                                                 @PathVariable("password") String password) {
//        return traineeFacade.select(userName, password).map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

//    @SwaggerCreationInfo(
//            summary = "Create a trainee",
//            description = "Adds a new trainee",
//            schema = TraineeCreateResponse.class
//    )
//    @PostMapping
//    public ResponseEntity<UserRegisteredResponse> registerTrainee(
//            @RequestBody @Valid RegisterTraineeRequest registerTraineeRequest,
//            HttpServletRequest servletRequest) {
//        UserRegisteredResponse response = traineeFacade.create(registerTraineeRequest);
//        return ResponseEntity.created(Utilities. .getResourceURI)
//    }


    //@CreationResponse
//    @PostMapping
//    //@ResponseStatus(Http.CREATED)
//    public TraineeCreateResponse createTrainee(@RequestBody @Valid TraineeCreateRequest request) {
//        String transactionId = transactionIdGenerator.generate();
//        LOGGER.info("POST /api/trainees called with request: {} with transaction id {}", request, transactionId);
//
//
//    }
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