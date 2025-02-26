package com.nt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResults {
	
	private Integer courseId;
	private String courseName;
	private String location;
	private String courseCategory;
	private String facultyName;
	private Double fee;
	private Long adminContact;
	private String trainingMode;
	private LocalDateTime startDate;
	private String courseStatus;

}
