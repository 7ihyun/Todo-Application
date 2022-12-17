package project.ToDoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class TodoDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String title;

        private int todoOrder;

        private Boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private Long id;

        @NotBlank
        private String title;

        private int todoOrder;

        private Boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private int todoOrder;
        private Boolean completed;
        private String url;
    }
}
