package project.ToDoApp.exception;

import lombok.Getter;

public enum ExceptionCode {
    TODO_ORDER_FOUND(404, "Todo Order found"),
    TODO_ORDER_EXISTS(409, "Todo Order exists");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
