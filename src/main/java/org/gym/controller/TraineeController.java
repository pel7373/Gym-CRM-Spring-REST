package org.gym.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeCreateRequest;
import org.gym.dto.TraineeCreateResponse;
import org.gym.dto.TraineeDto;
import org.gym.facade.TraineeFacade;

import org.gym.mapper.TraineeMapper;
import org.gym.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/trainees")
@Validated
//@Tag(name = "Trainees", description = "Operations related to managing trainees")
public class TraineeController {

    private final TraineeFacade traineeFacade;
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TransactionIdGenerator transactionIdGenerator;

    //    @SwaggerCreationInfo(
//            summary = "Create a trainee",
//            description = "Adds a new trainee",
//            schema = TraineeCreateResponse.class
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TraineeCreateResponse create(@RequestBody @Valid TraineeDto traineeDto){
        String id = transactionIdGenerator.generate();
        //TraineeDto traineeDto = traineeMapper.convertCreateRequestToDto(request);
        TraineeCreateResponse response = traineeFacade.create(traineeDto);
        LOGGER.info("create: id {}, request {}, response {}", id,  traineeDto, response);
        return response;
    }

//    public TraineeCreateResponse create(@RequestBody @Valid TraineeCreateRequest request){
//        String id = transactionIdGenerator.generate();
//        TraineeDto traineeDto = traineeMapper.convertCreateRequestToDto(request);
//        TraineeCreateResponse response = traineeFacade.create(traineeDto);
//        LOGGER.info("create: id {}, request {}, response {}", id,  request, response);
//        return response;
//    }

    @GetMapping("login/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> login(@PathVariable("username") String userName,
                                      @PathVariable("password") String password) {
        String id = transactionIdGenerator.generate();
        boolean response = traineeFacade.authenticate(userName, password);
        LOGGER.info("login: id {}, userName {}, response {}", id, userName, response);
        if(response) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable("username") String userName){
        String id = transactionIdGenerator.generate();
        LOGGER.info("delete: id {}, userName {}", id, userName);
        traineeFacade.delete(userName);
        return ResponseEntity.ok().build();
    }
//
//    @GetMapping("/{username}/{password}")
//    public ResponseEntity<TraineeDto> getTrainee(@PathVariable("username") String userName,
//                                                 @PathVariable("password") String password) {
//        return traineeFacade.select(userName, password).map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

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