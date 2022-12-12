package project.ToDoApp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import project.ToDoApp.dto.MultiResponseDto;
import project.ToDoApp.dto.SingleResponseDto;
import project.ToDoApp.dto.TodoDto;
import project.ToDoApp.entity.Todo;
import project.ToDoApp.mapper.TodoMapper;
import project.ToDoApp.service.TodoService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@CrossOrigin(origins = "https://todobackend.com")
@RestController
@RequestMapping("/v1/todos")
@Validated
@Slf4j
public class TodoController {
    private final TodoService todoService;
    private final TodoMapper mapper;

    public TodoController(TodoService todoService, TodoMapper mapper) {
        this.todoService = todoService;
        this.mapper = mapper;
    }

    // Todo 등록
    @PostMapping
    public ResponseEntity postTodo(@Valid @RequestBody TodoDto.Post requestBody) {
        Todo todo = mapper.todoPostToTodo(requestBody);

        TodoDto.Response response = mapper.todoToResponse(todoService.createTodo(todo));

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    // 전체 리스트 조회
    @GetMapping
    public ResponseEntity getTodoList(@Positive @RequestParam int page, @Positive @RequestParam int size) {
        Page<Todo> pageTodos = todoService.findTodoList(page - 1, size);
        List<Todo> todos = pageTodos.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.todoToResponses(todos), pageTodos), HttpStatus.OK);
    }

    // 특정 id 조회
    @GetMapping("/{id}")
    public ResponseEntity getTodo(@PathVariable("id") @Positive int id) {
        Todo todo = todoService.findTodo(id);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.todoToResponse(todo)), HttpStatus.OK);
    }

    // 내용 및 완료 여부 수정
    @PatchMapping("/{id}")
    public ResponseEntity patchTodo(@PathVariable("id") @Positive int id, @Valid @RequestBody TodoDto.Patch requestBody) {
        requestBody.setId(id);
        Todo todo = todoService.updateTodo(mapper.todoPatchToTodo(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.todoToResponse(todo)), HttpStatus.OK);
    }

    // 전체 리스트 삭제
    @DeleteMapping
    public ResponseEntity deleteTodoList(@Positive @RequestParam int page, @Positive @RequestParam int size) {
        Page<Todo> pageTodos = todoService.deleteTodoList(page - 1, size);
        List<Todo> todos = pageTodos.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.todoToResponses(todos), pageTodos), HttpStatus.NO_CONTENT);
    }

    // 특정 할 일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodo(@PathVariable("id") @Positive int id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 예외 처리 로직
//    @ExceptionHandler
//    public ResponseEntity handleException(MethodArgumentNotValidException e) {
//        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
//    }
}
