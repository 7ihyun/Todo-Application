package project.ToDoApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import project.ToDoApp.dto.TodoDto;
import project.ToDoApp.entity.Todo;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {
    Todo todoPostToTodo(TodoDto.Post requestBody);
    Todo todoPatchToTodo(TodoDto.Patch requestBody);
    TodoDto.Response todoToResponse(Todo todo);
    List<TodoDto.Response> todosToResponses(List<Todo> todos);
}
