package in.lms.cca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.service.LicenseeAuditservice;
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.api.ClientDashboardAPIs;

@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class ClientDashboardController {

	//Annual Audit Service
	@Autowired
	private LicenseeAuditservice annualAuditServ;
	
	@GetMapping(ClientDashboardAPIs.GET_ANNUAL_AUDIT_SCHEDULE_REPORT)
	public ResponseEntity<?> getAllAnnualAuditSchedule(){
		try {

			//Get all
			List<AnnualAuditScheduleEntity> list = annualAuditServ.getAllAnnualAuditSchedule();
			return new ResponseEntity<>(list, HttpStatus.OK);
		
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
}
