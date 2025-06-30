package uk.gov.hmcts.reform.dev.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.gov.hmcts.reform.dev.models.Task;

public interface TasksRepository extends CrudRepository<Task, Integer> {

    Task getTaskById(Integer id);

} 
