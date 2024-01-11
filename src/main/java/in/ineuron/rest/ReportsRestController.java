package in.ineuron.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ineuron.request.SearchRequest;
import in.ineuron.response.SearchResponse;
import in.ineuron.service.EligibilityServiceImpl;

@RestController
public class ReportsRestController {
	
	@Autowired
	private EligibilityServiceImpl service;

	@GetMapping("/plans")
	public ResponseEntity<List<String>> getPlanNames(){
		List<String> planNames = service.getUniquePlanNames();
		return new ResponseEntity<>(planNames, HttpStatus.OK);
	}
	
	@GetMapping("/statues")
	public ResponseEntity<List<String>> getPlanStatues(){
		List<String> planStatuses = service.getUniquePlanStatuses();
		return new ResponseEntity<>(planStatuses, HttpStatus.OK);
	}
	
	@PostMapping("/search")
	public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest request){
		List<SearchResponse> search = service.search(request);
		return new ResponseEntity<>(search, HttpStatus.OK);
	}
	
	@GetMapping("/excel")
	public void excelReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		
		String headerKey="Content-Disposition";
		String headerValue="attatchment;fileName=data.xls";
		
		response.setHeader(headerKey, headerValue);
		service.generateExcel(response);
	}
	
	@GetMapping("/pdf")
	public void pdfReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=data.pdf";
		
		response.setHeader(headerKey, headerValue);
		service.generatePdf(response);
	}
}
