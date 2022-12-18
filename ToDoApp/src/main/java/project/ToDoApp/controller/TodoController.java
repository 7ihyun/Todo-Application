package project.ToDoApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.ToDoApp.dto.TodoDto;
import project.ToDoApp.entity.Todo;
import project.ToDoApp.mapper.TodoMapper;
import project.ToDoApp.service.TodoService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@CrossOrigin(origins = "https://todobackend.com")
@RestController
@RequestMapping("/todos")
@Validated
//@Slf4j
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

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 특정 id 조회
    @GetMapping("/{todo_id}")
    public ResponseEntity getTodo(@PathVariable("todo_id") @Positive Long id) {
        Todo todo = todoService.findTodo(id);

        return new ResponseEntity<>(mapper.todoToResponse(todo), HttpStatus.OK);
    }

    // 전체 리스트 조회
    @GetMapping
    public ResponseEntity getTodos() { // @Positive @RequestParam int page, @Positive @RequestParam int size
//        Page<Todo> pageTodos = todoService.findTodoList(page - 1, size);
//        List<Todo> todos = pageTodos.getContent();

        List<Todo> todos = todoService.findTodos();
        return new ResponseEntity<>(mapper.todosToResponses(todos), HttpStatus.OK); // , pageTodos
    }

    // 내용 및 완료 여부 수정
    @PatchMapping("/{todo_id}")
    public ResponseEntity patchTodo(@PathVariable("todo_id") @Positive Long id, @Valid @RequestBody TodoDto.Patch requestBody) {
        Todo updateTodo = todoService.updateTodo(id, requestBody);

        return new ResponseEntity<>(mapper.todoToResponse(updateTodo), HttpStatus.OK);
    }

    // 특정 할 일 삭제
    @DeleteMapping("/{todo_id}")
    public ResponseEntity deleteTodo(@PathVariable("todo_id") @Positive Long id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 전체 리스트 삭제
    @DeleteMapping
    public ResponseEntity deleteTodos() {    // @Positive @RequestParam int page, @Positive @RequestParam int size
//        Page<Todo> pageTodos = todoService.deleteTodoList(page - 1, size);
//        List<Todo> todos = pageTodos.getContent();
        todoService.deleteTodos();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        // return new ResponseEntity<>(new MultiResponseDto<>(mapper.todoToResponses(todos), pageTodos), HttpStatus.NO_CONTENT);
    }

    // 예외 처리 로직
//    @ExceptionHandler
//    public ResponseEntity handleException(MethodArgumentNotValidException e) {
//        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
//    }
}
