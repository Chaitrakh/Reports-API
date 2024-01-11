package in.ineuron.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.ineuron.entity.EligibilityDetails;
import in.ineuron.repository.EgilibilityRepo;

@Component
public class AppRunner implements ApplicationRunner {
	
	@Autowired
	private EgilibilityRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		EligibilityDetails details1 = new EligibilityDetails();
		details1.setEligId(1);
		details1.setName("Chaitra");
		details1.setGender('F');
		details1.setMobile(9731787914L);
		details1.setSsn(34567L);
		details1.setPlanName("SSNP");
		details1.setPlanStatus("Approved");
		
		repo.save(details1);
		
		EligibilityDetails details2 = new EligibilityDetails();
		details2.setEligId(2);
		details2.setName("Sneha");
		details2.setGender('F');
		details2.setMobile(973909999L);
		details2.setSsn(6789L);
		details2.setPlanName("SPN");
		details2.setPlanStatus("Approved");
		
		repo.save(details2);
		
	}

}
