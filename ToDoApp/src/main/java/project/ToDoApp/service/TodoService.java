package project.ToDoApp.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ToDoApp.dto.TodoDto;
import project.ToDoApp.entity.Todo;
import project.ToDoApp.exception.BusinessLogicException;
import project.ToDoApp.exception.ExceptionCode;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final ApplicationEventPublisher publisher;

    public TodoService(TodoRepository todoRepository, ApplicationEventPublisher publisher) {
        this.todoRepository = todoRepository;
        this.publisher = publisher;
    }

    // todo 등록
    public Todo createTodo(Todo todo) {
        // 완료 표시
        if (todo.getCompleted() == true)
            todo.setTodoStatus(Todo.TodoStatus.TODO_IS_COMPLETED);

        return todoRepository.save(todo);
    }

    // 내용 및 완료 여부 수정
    public Todo updateTodo(Todo todo) {
        Todo findTodo = findVerifiedTodo(todo.getId());

        Optional.ofNullable(todo.getTitle())
                .ifPresent(title -> findTodo.setTitle(title));
        Optional.ofNullable(todo.getTodoOrder())
                .ifPresent(todoOrder -> findTodo.setTodoOrder(todoOrder));
        Optional.ofNullable(todo.getCompleted())
                .ifPresent(completed -> findTodo.setCompleted(completed));

        // todo 상태 업데이트
        Optional.ofNullable(todo.getTodoStatus())
                .ifPresent(todoStatus -> findTodo.setTodoStatus(todoStatus));
        
        return todoRepository.save(findTodo);
    }

    // 전체 리스트 조회
    @Transactional(readOnly = true)
    public Page<Todo> findTodoList(int page, int size) {
        return todoRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    // 특정 id 조회
    @Transactional(readOnly = true)
    public Todo findTodo(int id) {
        return findVerifiedTodoByQuery(id);
    }

    // 전체 리스트 삭제
    public Page<Todo> deleteTodoList(int page, int size) {
        return todoRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    // 특정 id 삭제
    public void deleteTodo(int id) {
        Todo todo = findVerifiedTodo(id);
        todoRepository.delete(todo);
    }

    @Transactional(readOnly = true)
    public Todo findVerifiedTodo(int id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        Todo findTodo =
                optionalTodo.orElseThrow(()
                        -> new BusinessLogicException(ExceptionCode.TODO_ORDER_FOUND));
        return findTodo;
    }

    @Transactional(readOnly = true)
    private void verifyExistOrder(int todoOrder) {
        Optional<Todo> todo = todoRepository.findByTodoOrder(todoOrder);

        if (todo.isPresent())
            throw new BusinessLogicException(ExceptionCode.TODO_ORDER_EXISTS);
    }

    private Todo findVerifiedTodoByQuery(int id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        Todo findTodo =
                optionalTodo.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.TODO_ORDER_FOUND));

        return findTodo;
    }
}
