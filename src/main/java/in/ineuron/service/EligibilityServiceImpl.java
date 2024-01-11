package in.ineuron.service;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import in.ineuron.entity.EligibilityDetails;
import in.ineuron.repository.EgilibilityRepo;
import in.ineuron.request.SearchRequest;
import in.ineuron.response.SearchResponse;

@Service
public class EligibilityServiceImpl implements EligibilityService {

	@Autowired
	private EgilibilityRepo eligibilityRepo;

	@Override
	public List<String> getUniquePlanNames() {
		return eligibilityRepo.findPlanNames();
	}

	@Override
	public List<String> getUniquePlanStatuses() {
		return eligibilityRepo.findPlanStatuses();
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) {
		List<SearchResponse> response = new ArrayList<>();
		EligibilityDetails queryBuilder = new EligibilityDetails();

		String planName = request.getPlanName();
		if (planName != null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}
		String status = request.getPlanStatus();
		if (status != null && !status.equals("")) {
			queryBuilder.setPlanStatus(status);
		}

		LocalDate startDate = request.getPlanStartDate();
		if (startDate != null) {
			queryBuilder.setPlanStartDate(startDate);
		}
		LocalDate endDate = request.getPlanEndDate();
		if (endDate != null) {
			queryBuilder.setPlanEndDate(endDate);
		}
		Example<EligibilityDetails> of = Example.of(queryBuilder);
		List<EligibilityDetails> all = eligibilityRepo.findAll();
		for(EligibilityDetails entity : all ) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(entity, sr);
			response.add(sr);
		}
		return response;
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws IOException {
	List<EligibilityDetails> all = eligibilityRepo.findAll();
	HSSFWorkbook workbook = new HSSFWorkbook(); 
	HSSFSheet sheet = workbook.createSheet();
	HSSFRow row = sheet.createRow(0);
	row.createCell(0).setCellValue("Name");
	row.createCell(1).setCellValue("Mobile");

	row.createCell(2).setCellValue("Gender");
	row.createCell(3).setCellValue("Ssn");

	int i=1;
	
	for(EligibilityDetails entity : all) {
	HSSFRow hssfRow = sheet.createRow(i);
		hssfRow.createCell(0).setCellValue(entity.getName());
		hssfRow.createCell(1).setCellValue(entity.getMobile());
		hssfRow.createCell(2).setCellValue(String.valueOf(entity.getGender()));
		hssfRow.createCell(3).setCellValue(entity.getSsn());
		i++;
	}
	ServletOutputStream stream = response.getOutputStream();
	workbook.write(stream);
	workbook.close();
	stream.close();	
}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception{
	List<EligibilityDetails> all = eligibilityRepo.findAll();
	
	Document document = new Document(PageSize.A4);
	PdfWriter.getInstance(document, response.getOutputStream());
	document.open();
	
	Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	font.setSize(18);
	font.setColor(Color.BLUE);
	
	Paragraph paragraph = new Paragraph("Search Report", font);
	paragraph.setAlignment(Paragraph.ALIGN_CENTER);
	
	
	document.add(paragraph);
	
	PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100f);
    table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 1.5f});
    table.setSpacingBefore(10);
    
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(Color.BLUE);
    cell.setPadding(4);
    
    font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(Color.WHITE);
     
    cell.setPhrase(new Phrase("Name", font));
     
    table.addCell(cell);
     
    cell.setPhrase(new Phrase("Mobile", font));
    table.addCell(cell);
     
    cell.setPhrase(new Phrase("Ssn", font));
    table.addCell(cell);
     
    cell.setPhrase(new Phrase("Gender", font));
    table.addCell(cell);
      
	
    for(EligibilityDetails entity : all) {
    	table.addCell(entity.getName());
    	table.addCell(String.valueOf(entity.getMobile()));
    	table.addCell(String.valueOf(entity.getSsn()));
    	table.addCell(String.valueOf(entity.getGender()));
    
	}
    document.add(table);
	document.close();
	}

}
