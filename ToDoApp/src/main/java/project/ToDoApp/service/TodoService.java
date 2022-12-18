package project.ToDoApp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ToDoApp.dto.TodoDto;
import project.ToDoApp.entity.Todo;
import project.ToDoApp.exception.BusinessLogicException;
import project.ToDoApp.exception.ExceptionCode;
import project.ToDoApp.utils.CustomBeanUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final CustomBeanUtils<Todo> beanUtils;

    public TodoService(TodoRepository todoRepository, CustomBeanUtils<Todo> beanUtils) {
        this.todoRepository = todoRepository;
        this.beanUtils = beanUtils;
    }

    // todo 등록
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    // 내용 및 완료 여부 수정
    public Todo updateTodo(Long id, TodoDto.Patch requestBody) {
        Todo findTodo = findTodo(id);

        Optional.ofNullable(requestBody.getTitle())
                .ifPresent(title -> findTodo.setTitle(title));
        Optional.ofNullable(requestBody.getTodoOrder())
                .ifPresent(todoOrder -> findTodo.setTodoOrder(todoOrder));
        Optional.ofNullable(requestBody.getCompleted())
                .ifPresent(completed -> findTodo.setCompleted(completed));
        
        return todoRepository.save(findTodo);
    }

    // 전체 리스트 조회
    @Transactional(readOnly = true)
    public List<Todo> findTodos() {  // public Page<Todo> findTodos(int page, int size)
        return todoRepository.findAll(); // PageRequest.of(page, size, Sort.by("id").descending())
    }

    // 특정 id 조회
    @Transactional(readOnly = true)
    public Todo findTodo(Long id) {
        return findVerifiedTodoById(id);
    }

    // 전체 리스트 삭제
    public void deleteTodos() {    // public Page<Todo> deleteTodos(int page, int size)
        todoRepository.deleteAll(); // PageRequest.of(page, size, Sort.by("id").descending())
    }

    // 특정 id 삭제
    public void deleteTodo(Long id) {
        Todo todo = findVerifiedTodoById(id);
        todoRepository.delete(todo);
    }

    /*
    @Transactional(readOnly = true)
    void verifyExistOrder(int todoOrder) {
        Optional<Todo> todo = todoRepository.findByTodoOrder(todoOrder);

        if (todo.isPresent())
            throw new BusinessLogicException(ExceptionCode.TODO_ORDER_EXISTS);
    }
    */

    private Todo findVerifiedTodoById(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);

        return optionalTodo.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.TODO_ORDER_FOUND));
    }
}
