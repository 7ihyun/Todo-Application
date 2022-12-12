package project.ToDoApp.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ToDoApp.entity.Todo;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByTodoOrder(int todoOrder);

    @Query(value = "SELECT c FROM Todo c WHERE c.id = :id")
    Optional<Todo> findById(int id);
}
