package com.infy.infyinterns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.infy.infyinterns.entity.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer>
{

    // add methods if required

}
