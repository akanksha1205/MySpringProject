package com.infy.infyinterns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.infy.infyinterns.entity.Mentor;

@Repository
public interface MentorRepository extends CrudRepository<Mentor, Integer>
{
     //add methods if required
	@Query(value = "Select * from mentor m where m.projects_mentored = :numberOfProjectsMentored", nativeQuery = true)
	List<Mentor> getMentorWithNumberOfProject(@Param("numberOfProjectsMentored") Integer numberOfProjectsMentored);
}
