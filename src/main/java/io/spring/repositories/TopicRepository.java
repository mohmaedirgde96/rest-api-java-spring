package io.spring.repositories;

import org.springframework.data.repository.CrudRepository;

import io.spring.models.Topic;

public interface TopicRepository extends CrudRepository<Topic, Integer> {

}
