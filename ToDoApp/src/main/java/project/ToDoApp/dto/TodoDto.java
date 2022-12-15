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

        private Boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private Long id;

        @NotBlank
        private String title;

        @NotNull
        private int todoOrder;

        @NotNull
        private Boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;

        @NotBlank
        private String title;

        @NotNull
        private int todoOrder;

        @NotNull
        private Boolean completed;

        private String url;
    }
}
