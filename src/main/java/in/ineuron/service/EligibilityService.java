package in.ineuron.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import in.ineuron.request.SearchRequest;
import in.ineuron.response.SearchResponse;

public interface EligibilityService {
	
	public List<String> getUniquePlanNames();
	
	public List<String> getUniquePlanStatuses();
	
	public List<SearchResponse> search(SearchRequest request);
	
	public void generateExcel(HttpServletResponse response) throws Exception;
	
	public void generatePdf(HttpServletResponse response)throws Exception;
	

}
