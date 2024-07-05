package com.project.Task.Manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Task.Manager.models.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}