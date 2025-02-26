package com.nt.controller;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.entity.SearchInputs;
import com.nt.entity.SearchResults;
import com.nt.service.ICourseMgmtService;


import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/report-api")
public class CourseReportOperationsController {
	@Autowired
	private ICourseMgmtService service;

	@GetMapping("/course")
	public ResponseEntity<?> fetchCourseCategories() {
		try {
			Set<String> coursesInfo = service.showAllCategories();
			return new ResponseEntity<Set<String>>(coursesInfo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/training-mode")
	public ResponseEntity<?> fetchTrainingModess() {
		try {
			Set<String> trainingModes = service.showAllTrainingModes();
			return new ResponseEntity<Set<String>>(trainingModes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/faculty")
	public ResponseEntity<?> fetchFaculties() {
		try {
			Set<String> facultyInfo = service.showAllFaculties();
			return new ResponseEntity<Set<String>>(facultyInfo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/search")
	public ResponseEntity<?> fetchCourseResultByFilter(@RequestBody SearchInputs inputs) {
		try {
			List<SearchResults> list = service.showCourseByFilters(inputs);
			return new ResponseEntity<List<SearchResults>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/pdf-report")
	public void howPdfReport(@RequestBody SearchInputs inputs, HttpServletResponse response)
			throws Exception {
		
		try {
		response.setContentType("application/pdf");
		// set the content-disposition header to make the response content going to browser as
		// downloadable type
		response.setHeader("Content-Disposition", "attachment;fileName=courses.pdf");
		service.generatePdfReport(inputs, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	@PostMapping("/excel-report")
	public void showExcelReport(@RequestBody SearchInputs inputs,HttpServletResponse response) {
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment;fileName=courses.xls");
			service.generateExcelReport(inputs, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@GetMapping("/pdf-report")
	public void howPdfReport( HttpServletResponse response)
			throws Exception {
		
		try {
		response.setContentType("application/pdf");
		// set the content-disposition header to make the response content going to browser as
		// downloadable type
		response.setHeader("Content-Disposition", "attachment;fileName=courses.pdf");
		service.generatePdfReport(response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	@GetMapping("/excel-report")
	public void showExcelReport(HttpServletResponse response) {
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment;fileName=courses.xls");
			service.generatePdfReport(response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}














