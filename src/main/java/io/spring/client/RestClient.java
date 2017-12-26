package io.spring.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.spring.models.Course;
import io.spring.models.Topic;

public class RestClient {

	public static final String REST_SERVICE_URI = "http://localhost:8080/api/";
	public static final String plainClientCredentials="user1:user1";
	public static final String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));

	public static void main(String[] args) {
		// testing topics
		createTopics();
		getAllTopics();
		getTopic(1);
		updateTopic(1);
		deleteTopic(3);
		
		// testing courses
		createCourses();
		getCoursesByTopic(2);
		getCourse(2, 3);
		updateCourse(1, 1);
		deleteCourse(2, 4);
	}

	// get all topics
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void getAllTopics() {
		System.out.println("Getting all topics...");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		ResponseEntity<List> response = restTemplate.exchange(REST_SERVICE_URI + "/topics/", HttpMethod.GET, request, List.class);
        List<LinkedHashMap<String, Object>> topics = (List<LinkedHashMap<String, Object>>)response.getBody();

		if (!topics.isEmpty()) {
			for (LinkedHashMap<String, Object> topic : topics) {
				System.out.println("Topic: id = " + topic.get("id") + ", name = " + topic.get("name") + ", description = "
						+ topic.get("description"));
			}
		} else {
			System.out.println("There are no topics...");
		}
	}
	
	// get topic by id
    private static void getTopic(int id){
        System.out.println("Getting topic by given id...");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<Topic> response = restTemplate.exchange(REST_SERVICE_URI + "topics/" + id, HttpMethod.GET, request, Topic.class);
        Topic topic = response.getBody();
        System.out.println("Topic with provided id " + id + ": name = " + topic.getName() + ", description = "
				+ topic.getDescription());
    }

	// create topics using post method
	private static void createTopics() {
		System.out.println("Creating topics...");
		RestTemplate restTemplate = new RestTemplate();
		List<Topic> topics = getDummyTopicData();
		for (Topic t : topics) {
			HttpEntity<Object> request = new HttpEntity<Object>(t, getHeaders());
			restTemplate.postForLocation(REST_SERVICE_URI + "topics/", request, Topic.class);
			System.out.println("Created topic: id = " + t.getId() + ", name= " + t.getName() + ", description = "
					+ t.getDescription());
		}
	}
	
	// update topic by id
    private static void updateTopic(int id) {
        System.out.println("Updating topic with id = " + id);
        RestTemplate restTemplate = new RestTemplate();
        Topic topic  = new Topic(id, "Updated topic name", "Updated topic description");
        HttpEntity<Object> request = new HttpEntity<Object>(topic, getHeaders());
        restTemplate.exchange(REST_SERVICE_URI + "topics/" + id, HttpMethod.PUT, request, Topic.class);
        System.out.println("Updated topic values: name = " + topic.getName() + 
        		", description= " + topic.getDescription());
    }
  
    // delete user
    private static void deleteTopic(int id) {
        System.out.println("Deleting topic with id = " + id + "...");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        restTemplate.exchange(REST_SERVICE_URI + "topics/" + id, HttpMethod.DELETE, request, Topic.class);
        System.out.println("Topic deleted...");
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static void getCoursesByTopic(int topicId) {
    	System.out.println("Getting courses for provided topic id...");
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		ResponseEntity<List> response = restTemplate.exchange(REST_SERVICE_URI + "/topics/" + topicId + "/courses/", 
				HttpMethod.GET, request, List.class);
        List<LinkedHashMap<String, Object>> courses = (List<LinkedHashMap<String, Object>>) response.getBody();

		if (!courses.isEmpty()) {
			for (LinkedHashMap<String, Object> course : courses) {
				System.out.println("Course: id = " + course.get("id") + ", name = " + course.get("name") + ", description = "
						+ course.get("description"));
			}
		} else {
			System.out.println("There are no courses for provided id...");
		}
	}
    
    // get course by topic and course id
    private static void getCourse(int topicId, int courseId) {
    	System.out.println("Getting course by given id...");
        RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<Course> response = restTemplate.exchange(REST_SERVICE_URI + "topics/" + topicId + "/courses/" + courseId, 
        		HttpMethod.GET, request, Course.class);
        Course course = response.getBody();
        
        System.out.println("Course with provided id " + courseId + ": name = " + course.getName() + ", description = "
				+ course.getDescription());
    }
    
    // create courses using post method
 	private static void createCourses() {
 		System.out.println("Creating courses...");
 		RestTemplate restTemplate = new RestTemplate();
 		List<Course> courses = getDummyCourseData();
 		for (Course c : courses) {
 			HttpEntity<Object> request = new HttpEntity<Object>(c, getHeaders());
 			restTemplate.postForLocation(REST_SERVICE_URI + "topics/" + c.getTopic().getId() + "/courses/", request, Course.class);
 			System.out.println("Created course: id = " + c.getId() + ", name= " + c.getName() + ", description = "
 					+ c.getDescription() + ", topicid = " + c.getTopic().getId());
 		}
 	}
 	
 	// update course by id
    private static void updateCourse(int topicId, int courseId) {
        System.out.println("Updating course with id = " + courseId);
        RestTemplate restTemplate = new RestTemplate();
        Course course  = new Course(courseId, "Updated course name", "Updated course description", topicId);
        
        HttpEntity<Object> request = new HttpEntity<Object>(course, getHeaders());
        restTemplate.exchange(REST_SERVICE_URI + "topics/" + topicId + "/courses/" + courseId, HttpMethod.PUT, request, Course.class);
        
        System.out.println("Updated course values: name = " + course.getName() + 
        		", description= " + course.getDescription());
    }
  
    // delete course
    private static void deleteCourse(int topicId, int courseId) {
        System.out.println("Deleting course with id = " + courseId + "...");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        restTemplate.exchange(REST_SERVICE_URI + "topics/" + topicId + "/courses/" + courseId, HttpMethod.DELETE, request, Topic.class);
        System.out.println("Course deleted...");
    }

	private static List<Topic> getDummyTopicData() {
		List<Topic> dummyTopicData = new ArrayList<>();
		Topic topic1 = new Topic(1, "First topic", "First topic description");
		Topic topic2 = new Topic(2, "Second topic", "Second topic description");
		Topic topic3 = new Topic(3, "Third topic", "Third topic description");
		dummyTopicData.add(topic1);
		dummyTopicData.add(topic2);
		dummyTopicData.add(topic3);
		return dummyTopicData;
	}
	
	private static List<Course> getDummyCourseData() {
		List<Course> dummyCourseData = new ArrayList<>();
		Course course1 = new Course(1, "First course", "First course description", 1);
		Course course2 = new Course(2, "Second course", "Second course description", 1);
		Course course3 = new Course(3, "Third course", "Third course description", 2);
		Course course4 = new Course(4, "Fourth course", "Fourth course description", 2);
		dummyCourseData.add(course1);
		dummyCourseData.add(course2);
		dummyCourseData.add(course3);
		dummyCourseData.add(course4);
		return dummyCourseData;
	}
	
    // add HTTP Authorization header, using Basic-Authentication to send user-credentials.
    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64ClientCredentials);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
