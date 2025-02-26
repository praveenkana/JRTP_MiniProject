package com.nt.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.nt.entity.SearchInputs;
import com.nt.entity.SearchResults;

import jakarta.servlet.http.HttpServletResponse;

public interface ICourseMgmtService {

	public Set<String> showAllCategories();

	public Set<String> showAllTrainingModes();

	public Set<String> showAllFaculties();

	public List<SearchResults> showCourseByFilters(SearchInputs inputs);

	public void generatePdfReport(SearchInputs inputs, HttpServletResponse res)throws Exception;

	public void generateExcelReport(SearchInputs inputs, HttpServletResponse res) throws IOException;

	public void generatePdfReport(HttpServletResponse response) throws DocumentException, IOException;

	public void generateExcelReport(HttpServletResponse response) throws IOException;
}
