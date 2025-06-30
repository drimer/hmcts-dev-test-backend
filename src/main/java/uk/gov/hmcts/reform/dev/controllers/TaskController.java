package uk.gov.hmcts.reform.dev.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import uk.gov.hmcts.reform.dev.dto.TaskCreateRequestDto;
import uk.gov.hmcts.reform.dev.dto.TaskResponseDto;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TasksRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class TaskController {

    private TasksRepository tasksRepository;

    public TaskController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @PostMapping("/tasks")
    public TaskResponseDto createTask(@Valid @RequestBody TaskCreateRequestDto taskCreateRequestDto) {
        var modelMapper = new ModelMapper();
        var task = modelMapper.map(taskCreateRequestDto, Task.class);

        var savedTask = tasksRepository.save(task);

        return modelMapper.map(savedTask, TaskResponseDto.class);
    }

    @GetMapping("/tasks/{id}")
    public TaskResponseDto getTaskById(@PathVariable("id") int id) {
        var task = tasksRepository.getTaskById(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var modelMapper = new ModelMapper();
        return modelMapper.map(task, TaskResponseDto.class);
    }
    
}
