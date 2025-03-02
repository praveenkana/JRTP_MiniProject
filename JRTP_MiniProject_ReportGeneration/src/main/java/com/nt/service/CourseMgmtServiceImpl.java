package com.nt.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.nt.entity.CourseDetails;
import com.nt.entity.SearchInputs;
import com.nt.entity.SearchResults;
import com.nt.repository.ICourseDetailsRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CourseMgmtServiceImpl implements ICourseMgmtService {

	@Autowired
	private ICourseDetailsRepository courserepo;

	@Override
	public Set<String> showAllCategories() {
		return courserepo.getUniqueCourseDetails();
	}

	@Override
	public Set<String> showAllTrainingModes() {
		return courserepo.getUniqueTrainingModes();
	}

	@Override
	public Set<String> showAllFaculties() {
		return courserepo.getUniqueFacultyNames();
	}

	@Override
	public List<SearchResults> showCourseByFilters(SearchInputs inputs) {

		CourseDetails entity = new CourseDetails();

		String fName = inputs.getFacultyName();
		/*
		 * if(fName!=null && !fName.equals("")&& fName.length()!=0) {
		 * entity.setFacultyName(fName); }
		 */
		if (StringUtils.hasLength(fName)) {
			entity.setFacultyName(fName);
		}
		String trainingMode = inputs.getTrainingMode();
		/*
		 * if(trainingMode!=null && !trainingMode.equals("") &&
		 * trainingMode.length()!=0) { entity.setTrainingMode(trainingMode); }
		 */
		if (StringUtils.hasLength(trainingMode)) {
			entity.setTrainingMode(trainingMode);
		}
		String category = inputs.getCourseCategory();
		/*
		 * if(category!=null && !category.equals("") && category.length()!=0) {
		 * entity.setTrainingMode(category); }
		 */
		if (StringUtils.hasLength(category)) {
			entity.setCourseCategory(category);
		}
		LocalDateTime startsOn = inputs.getStartsOn();
		if (startsOn != null)
			entity.setStartDate(startsOn);

		Example<CourseDetails> example = Example.of(entity);
		// perform search operation with filters of the Example object
		List<CourseDetails> cousrseDetails = courserepo.findAll(example);

		// convert list<Entity> object to List<SearchResult> object
		List<SearchResults> list = new ArrayList<>();
		cousrseDetails.forEach(course -> {
			SearchResults result = new SearchResults();
			BeanUtils.copyProperties(course, result);
			list.add(result);
		});

		return list;
	}

	@Override
	public void generatePdfReport(SearchInputs inputs, HttpServletResponse res) throws DocumentException, IOException {

		// get search results
		List<SearchResults> resultList = showCourseByFilters(inputs);
		// create document object
		Document document = new Document(PageSize.A4);
		// get pdfWriter to write to the document and response obj
		PdfWriter writer = PdfWriter.getInstance(document, res.getOutputStream());
		// open the document
		document.open();
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);

		// create paragraph
		Paragraph para = new Paragraph("Search Report Of Courses", font);
		para.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(para);

		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(70);
		table.setWidths(new float[] { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f });
		table.setSpacingBefore(2.0f);
		// prepare heading row cells in the pdf table

		PdfPCell cell = new PdfPCell();
		cell.setPhrase(new Phrase("courseId"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseName"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("category"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("facultyName"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("location"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("fee"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseStatus"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("trainingMode"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("adminContact"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("startDate"));
		table.addCell(cell);

		//add data to pdf-cells
		
		resultList.forEach(result->{
			table.addCell(String.valueOf(result.getCourseId()));
			table.addCell(result.getCourseName());
			table.addCell(result.getCourseCategory());
			table.addCell(result.getFacultyName());
			table.addCell(result.getLocation());
			table.addCell(String.valueOf(result.getFee()));
			table.addCell(result.getCourseStatus());
			table.addCell(result.getTrainingMode());
			table.addCell(String.valueOf(result.getAdminContact()));
			table.addCell(result.getStartDate().toString());
		});
		//add table to document
		document.add(table);
		document.close();
	}

	@Override
	public void generateExcelReport(SearchInputs inputs, HttpServletResponse res) throws IOException {
		try(ServletOutputStream os = res.getOutputStream();
				HSSFWorkbook workbook = new HSSFWorkbook();){
			// get search results
			List<SearchResults> resultList = showCourseByFilters(inputs);
			// create excel workbook
			
			// create sheet in workbook
			HSSFSheet sheet1 = workbook.createSheet("CourseDetails");
			// create header row in sheet1
			HSSFRow headerRow = sheet1.createRow(0);
			headerRow.createCell(0).setCellValue("coursedId");
			headerRow.createCell(1).setCellValue("courseName");
			headerRow.createCell(2).setCellValue("location");
			headerRow.createCell(3).setCellValue("courseCategory");
			headerRow.createCell(4).setCellValue("facultyName");
			headerRow.createCell(5).setCellValue("fee");
			headerRow.createCell(6).setCellValue("adminContact");
			headerRow.createCell(7).setCellValue("trainingMode");
			headerRow.createCell(8).setCellValue("startDate");
			headerRow.createCell(9).setCellValue("courseStatus");

			int i = 1;

			for (SearchResults result : resultList) {

				HSSFRow datarow = sheet1.createRow(i);
				datarow.createCell(0).setCellValue(result.getCourseId());
				datarow.createCell(1).setCellValue(result.getCourseName());
				datarow.createCell(2).setCellValue(result.getLocation());
				datarow.createCell(3).setCellValue(result.getCourseCategory());
				datarow.createCell(4).setCellValue(result.getFacultyName());
				datarow.createCell(5).setCellValue(result.getFee());
				datarow.createCell(6).setCellValue(result.getAdminContact());
				datarow.createCell(7).setCellValue(result.getTrainingMode());
				datarow.createCell(8).setCellValue(result.getStartDate());
				datarow.createCell(9).setCellValue(result.getCourseStatus());
				i++;

			}

		
			
			// write excel data to output stream
			workbook.write(os);
		

		}


			
		}
		
		
	@Override
	public void generatePdfReport(HttpServletResponse response) throws DocumentException, IOException {
		List<CourseDetails> list = courserepo.findAll();
		List<SearchResults>listResults=new ArrayList<>();
		list.forEach(res->{
			SearchResults sr=new SearchResults();
			BeanUtils.copyProperties(res, sr);
			listResults.add(sr);
		});
		
		Document document = new Document(PageSize.A4);
		// get pdfWriter to write to the document and response obj
		PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		// open the document
		document.open();
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(30);

		// create paragraph
		Paragraph para = new Paragraph("Search Report Of Courses", font);
		para.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(para);

		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(70);
		table.setWidths(new float[] { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f });
		table.setSpacingBefore(2.0f);
		// prepare heading row cells in the pdf-table

		PdfPCell cell = new PdfPCell();
		cell.setPhrase(new Phrase("courseId"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseName"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("category"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("facultyName"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("location"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("fee"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("courseStatus"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("trainingMode"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("adminContact"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("startDate"));
		table.addCell(cell);

		//add data to pdf cells
		
		listResults.forEach(result->{
			table.addCell(String.valueOf(result.getCourseId()));
			table.addCell(result.getCourseName());
			table.addCell(result.getCourseCategory());
			table.addCell(result.getFacultyName());
			table.addCell(result.getLocation());
			table.addCell(String.valueOf(result.getFee()));
			table.addCell(result.getCourseStatus());
			table.addCell(result.getTrainingMode());
			table.addCell(String.valueOf(result.getAdminContact()));
			table.addCell(result.getStartDate().toString());
		});
		//add table to document
		document.add(table);
		document.close();
		
	}

	@Override
	public void generateExcelReport(HttpServletResponse response) throws IOException {
		List<CourseDetails> list = courserepo.findAll();
		List<SearchResults> listResults=new ArrayList<>();
		list.forEach(res->{
			SearchResults sr=new SearchResults();
			BeanUtils.copyProperties(res, sr);
			listResults.add(sr);
			
		});
		
		// create excel workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// create sheet in workbook
		HSSFSheet sheet1 = workbook.createSheet("CourseDetails");
		// create header row in sheet1
		HSSFRow headerRow = sheet1.createRow(0);
		headerRow.createCell(0).setCellValue("coursedId");
		headerRow.createCell(1).setCellValue("courseName");
		headerRow.createCell(2).setCellValue("location");
		headerRow.createCell(3).setCellValue("courseCategory");
		headerRow.createCell(4).setCellValue("facultyName");
		headerRow.createCell(5).setCellValue("fee");
		headerRow.createCell(6).setCellValue("adminContact");
		headerRow.createCell(7).setCellValue("trainingMode");
		headerRow.createCell(8).setCellValue("startDate");
		headerRow.createCell(9).setCellValue("courseStatus");

		int i = 1;

		for (SearchResults result : listResults) {

			HSSFRow datarow = sheet1.createRow(i);
			datarow.createCell(0).setCellValue(result.getCourseId());
			datarow.createCell(1).setCellValue(result.getCourseName());
			datarow.createCell(2).setCellValue(result.getLocation());
			datarow.createCell(3).setCellValue(result.getCourseCategory());
			datarow.createCell(4).setCellValue(result.getFacultyName());
			datarow.createCell(5).setCellValue(result.getFee());
			datarow.createCell(6).setCellValue(result.getAdminContact());
			datarow.createCell(7).setCellValue(result.getTrainingMode());
			datarow.createCell(8).setCellValue(result.getStartDate());
			datarow.createCell(9).setCellValue(result.getCourseStatus());
			i++;

		}

		// get output stream object pointing to response obj
		ServletOutputStream os = response.getOutputStream();
		// write excel data to output stream
		workbook.write(os);
		os.close();
		workbook.close();

	}
		
	}


