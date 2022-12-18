package project.ToDoApp.restdocs;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.ToDoApp.controller.TodoController;
import project.ToDoApp.dto.TodoDto;
import project.ToDoApp.entity.Todo;
import project.ToDoApp.mapper.TodoMapper;
import project.ToDoApp.service.TodoService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class TodoControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper mapper;

    @Autowired
    private Gson gson;

    @Test
    public void postTodoTest() throws Exception {
        // given
        TodoDto.Post post = new TodoDto.Post("work", 1, false);
        String content = gson.toJson(post);

        TodoDto.Response response =
                new TodoDto.Response(
                        1L, "work", 1, false, "http://localhost:8080/todos/1");

            // Stubbing
        given(mapper.todoPostToTodo(Mockito.any(TodoDto.Post.class))).willReturn(new Todo());
        given(todoService.createTodo(Mockito.any(Todo.class))).willReturn(new Todo());
        given(mapper.todoToResponse(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                post("/todos")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(post.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(post.getCompleted()))
                .andDo(document("post-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("등록 순서").optional(),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("등록 순서"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("url 정보")
                                )
                        )
                ));
    }

    @Test
    public void getTodoTest() throws Exception {
        // given
        Long id = 1L;

        TodoDto.Response response = new TodoDto.Response(1L, "work", 1, false, "http://localhost:8080/todos/1");

        given(todoService.findTodo(Mockito.anyLong())).willReturn(new Todo());
        given(mapper.todoToResponse(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                get("/todos/{todo-id}", id)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(response.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(response.getCompleted()))
                .andDo(document("get-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todo-id").description("Todo 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("등록 순서"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("url 정보")
                                )
                        )
                ));
    }

    @Test
    public void getTodosTest() throws Exception {
        //given
        Todo todo1 = new Todo(1L, "work", 1, false);
        Todo todo2 = new Todo(2L, "eat", 2, false);
        Todo todo3 = new Todo(3L, "sleep", 3, false);


        List<TodoDto.Response> responses = List.of(
                new TodoDto.Response(1L, "work", 1, false, "http://localhost:8080/todos/1"),
                new TodoDto.Response(2L, "eat", 2, false, "http://localhost:8080/todos/2"),
                new TodoDto.Response(3L, "sleep", 3, false, "http://localhost:8080/todos/3"));

        given(todoService.findTodos()).willReturn(new ArrayList<>());
        given(mapper.todosToResponses(Mockito.anyList())).willReturn(responses);

        // when
        ResultActions actions = mockMvc.perform(
                get("/todos")
                        .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(responses.size()))
                .andExpect(jsonPath("$.[0].id").value(responses.get(0).getId()))
                .andExpect(jsonPath("$.[0].title").value(responses.get(0).getTitle()))
                .andExpect(jsonPath("$.[0].todoOrder").value(responses.get(0).getTodoOrder()))
                .andExpect(jsonPath("$.[0].completed").value(responses.get(0).getCompleted()))
                .andDo(document("get-todos",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                List.of(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("결과 리스트"),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("[].todoOrder").type(JsonFieldType.NUMBER).description("등록 순서"),
                                        fieldWithPath("[].completed").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                        fieldWithPath("[].url").type(JsonFieldType.STRING).description("url 정보")
                                )
                        )
                ));
    }

    @Test
    public void patchTodoTest() throws Exception {
        // given
        Long id = 1L;

        TodoDto.Patch patch = new TodoDto.Patch(1L, "work", 1, true);
        String content = gson.toJson(patch);

        TodoDto.Response response = new TodoDto.Response(1L, "work", 1, true, "http://localhost:8080/todos/1");

        given(todoService.updateTodo(Mockito.anyLong(), Mockito.any(TodoDto.Patch.class))).willReturn(new Todo());
        given(mapper.todoToResponse(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/todos/{todo-id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(patch.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(patch.getCompleted()))
                .andDo(document("patch-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("todo-id").description("Todo 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일").optional(),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("등록 순서").optional(),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("todoOrder").type(JsonFieldType.NUMBER).description("등록 순서"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("url 정보")
                                )
                        )
                ));
    }

    @Test
    public void deleteTodoTest() throws Exception {
        // given
        Long id = 1L;

        doNothing().when(todoService).deleteTodo(Mockito.anyLong());

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/todos/{todo-id}", id));

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void deleteTodosTest() throws Exception {
        // given
        doNothing().when(todoService).deleteTodos();

        // when
        ResultActions actions = mockMvc.perform(delete("/todos"));

        // then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-todos",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
