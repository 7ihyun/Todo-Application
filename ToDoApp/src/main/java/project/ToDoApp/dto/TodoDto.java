package project.ToDoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.ToDoApp.entity.Todo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TodoDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String title;

        @NotNull
        private int todoOrder;

        @NotNull
        private Boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private int id;

        @NotBlank
        private String title;

        @NotNull
        private Boolean completed;

        private Todo.TodoStatus todoStatus;

        public void setId(int id) {
            this.id = id;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private int id;

        @NotBlank
        private String title;

        @NotNull
        private int todoOrder;

        @NotNull
        private Boolean completed;
        private Todo.TodoStatus todoStatus;

        public String getTodoStatus() {
            return todoStatus.getStatus();
        }
    }
}
