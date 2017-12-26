package io.spring.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.spring.models.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {
	
	public List<Course> findByTopicId(int topicId);

}
