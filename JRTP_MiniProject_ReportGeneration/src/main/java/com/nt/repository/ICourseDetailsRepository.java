package com.nt.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nt.entity.CourseDetails;

public interface ICourseDetailsRepository extends JpaRepository<CourseDetails, Integer> {
	
	@Query("select distinct(courseCategory) from CourseDetails")
	public Set<String> getUniqueCourseDetails();
	
	@Query("select distinct(trainingMode) from CourseDetails")
	public Set<String> getUniqueTrainingModes();
	
	@Query("select distinct(facultyName) from CourseDetails")
	public Set<String> getUniqueFacultyNames();

}
