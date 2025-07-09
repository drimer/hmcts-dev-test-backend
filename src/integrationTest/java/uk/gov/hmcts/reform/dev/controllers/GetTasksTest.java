package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
class GetTasksTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockitoBean
    private transient TasksRepository tasksRepository;

    @DisplayName("Should return empty list of tasks with 200 response code")
    @Test
    void getTasksEndpoint() throws Exception {
        when(tasksRepository.findAll()).thenReturn(Collections.emptyList());

        MvcResult response = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @DisplayName("Should return list with tasks with 200 response code")
    @Test
    void getTasksEndpointWithTasks() throws Exception {
        when(tasksRepository.findAll()).thenReturn(List.of(
                new Task()
                        .setId(1)
                        .setTitle("Test Task")
                        .setDescription("description test")
                        .setStatus("OPEN")));

        MvcResult response = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isEqualTo(
                "[{\"id\":1,\"title\":\"Test Task\",\"description\":\"description test\",\"status\":\"OPEN\",\"createdDate\":null}]");
    }

    @DisplayName("Should return individual task when found by ID")
    @Test
    void getTaskByIdEndpoint() throws Exception {
        when(tasksRepository.getTaskById(1)).thenReturn(
                new Task()
                        .setId(0)
                        .setTitle("Test Task")
                        .setDescription("description test")
                        .setStatus("OPEN"));

        MvcResult response = mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isEqualTo(
                "{\"id\":0,\"title\":\"Test Task\",\"description\":\"description test\",\"status\":\"OPEN\",\"createdDate\":null}");
    }

    @DisplayName("Should return 404 when task not found by ID")
    @Test
    void getTaskByIdEndpointNotFound() throws Exception {
        when(tasksRepository.getTaskById(1)).thenReturn(null);

        MvcResult response = mockMvc.perform(get("/tasks/")).andExpect(status().is4xxClientError()).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(404);
    }

}
