package in.lms.cca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.MinimumAttemptsDTO;
import in.lms.cca.dto.StateDTO;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.MinimumAttempts;
import in.lms.cca.entity.State;
import in.lms.cca.service.IAddressService;
import in.lms.cca.service.IMinimumAttemptsService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.MinimumAttemptsServiceAPIs;
import in.lms.cca.util.api.StateServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class MinimumAttemptsController {
	
	@Autowired
	private IMinimumAttemptsService minimumAttemptsServ;
	
	@PostMapping(MinimumAttemptsServiceAPIs.ADD_MINIMUM_ATTEMPTS)
	public ResponseEntity<?> addNewMinimumAttempts(@RequestBody MinimumAttemptsDTO minimumAttemptsObj) {
	    
	    // Validate applicationScrutiny
	    if (minimumAttemptsObj.getApplicationScrutiny() == null) {
	        return new ResponseEntity<>("Please enter application scrutiny attempts.", HttpStatus.BAD_REQUEST);
	    } else if (!(minimumAttemptsObj.getApplicationScrutiny() > 1 && minimumAttemptsObj.getApplicationScrutiny() < 10)) {
	        return new ResponseEntity<>("Application Scrutiny attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
	    }

	    // Validate applicationReview
	    if (minimumAttemptsObj.getApplicationReview() == null) {
	        return new ResponseEntity<>("Please enter application review attempts.", HttpStatus.BAD_REQUEST);
	    }else if (!(minimumAttemptsObj.getApplicationReview() > 1 && minimumAttemptsObj.getApplicationReview() < 10)) {
	        return new ResponseEntity<>("Application Review attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
	    }
	    
	    
	    // Validate applicationAudit
	    if (minimumAttemptsObj.getApplicationAudit() == null) {
	        return new ResponseEntity<>("Please enter application audit attempts.", HttpStatus.BAD_REQUEST);
	    }else if (!(minimumAttemptsObj.getApplicationAudit() > 1 && minimumAttemptsObj.getApplicationAudit() < 10)) {
	        return new ResponseEntity<>("Application audit attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
	    }

	    // Validate esignApplicationReview
	    if (minimumAttemptsObj.getEsignApplicationReview() == null) {
	    	return new ResponseEntity<>("Please enter eSign application attempts.", HttpStatus.BAD_REQUEST);
	    } else if (!(minimumAttemptsObj.getEsignApplicationReview() > 1 && minimumAttemptsObj.getEsignApplicationReview() < 10)) {
	        return new ResponseEntity<>("eSign application review attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
	    }

	    // Validate annualAuditReview
	    if (minimumAttemptsObj.getAnnualAuditReview() == null) {
	        return new ResponseEntity<>("Please enter annual audit review attemps.", HttpStatus.BAD_REQUEST);
	    } else if (!(minimumAttemptsObj.getAnnualAuditReview() > 1 && minimumAttemptsObj.getAnnualAuditReview() < 10)) {
	        return new ResponseEntity<>("Annual audit review attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
	    }

	    try {
	        List<MinimumAttempts> minimumAttemptsList = minimumAttemptsServ.getAllMinimumAttempts();

	        if (minimumAttemptsList.isEmpty()) {
	            // Create a new MinimumAttempts object and set its properties
	            MinimumAttempts newMinimumAttempts = new MinimumAttempts();
	            newMinimumAttempts.setAnnualAuditReview(minimumAttemptsObj.getAnnualAuditReview());
	            newMinimumAttempts.setApplicationAudit(minimumAttemptsObj.getApplicationAudit());
	            newMinimumAttempts.setApplicationReview(minimumAttemptsObj.getApplicationReview());
	            newMinimumAttempts.setApplicationScrutiny(minimumAttemptsObj.getApplicationScrutiny());
	            newMinimumAttempts.setEsignApplicationReview(minimumAttemptsObj.getEsignApplicationReview());
	            newMinimumAttempts.setStatus("Active");

	            // Save the new MinimumAttempts object to the database
	            minimumAttemptsServ.addMinimumAttempts(newMinimumAttempts);

	            // Print the status of the new MinimumAttempts object
	            System.out.println(newMinimumAttempts.getStatus());

	            return new ResponseEntity<>("Minimum Attempts successfully added.", HttpStatus.OK);
	        } else {
	            // Set the status of each MinimumAttempts to "Inactive"
	            for (MinimumAttempts attempt : minimumAttemptsList) {
	                attempt.setStatus("Inactive");
	            }

	            // Save the updated MinimumAttempts back to the database
	            for (MinimumAttempts attempt : minimumAttemptsList) {
	                minimumAttemptsServ.addMinimumAttempts(attempt);
	            }

	            // Create a new MinimumAttempts object and set its properties
	            MinimumAttempts newMinimumAttempts = new MinimumAttempts();
	            newMinimumAttempts.setAnnualAuditReview(minimumAttemptsObj.getAnnualAuditReview());
	            newMinimumAttempts.setApplicationAudit(minimumAttemptsObj.getApplicationAudit());
	            newMinimumAttempts.setApplicationReview(minimumAttemptsObj.getApplicationReview());
	            newMinimumAttempts.setApplicationScrutiny(minimumAttemptsObj.getApplicationScrutiny());
	            newMinimumAttempts.setEsignApplicationReview(minimumAttemptsObj.getEsignApplicationReview());
	            newMinimumAttempts.setStatus("Active");

	            // Save the new MinimumAttempts object to the database
	            Optional<MinimumAttempts> savedAttempt = minimumAttemptsServ.addMinimumAttempts(newMinimumAttempts);
	            System.out.println(newMinimumAttempts.getStatus());

	            // Check if the new MinimumAttempts object was saved successfully
	            if (savedAttempt.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while saving the Minimum Attempts. Please try again.", HttpStatus.BAD_REQUEST);
	            } else {
	                return new ResponseEntity<>("Minimum Attempts successfully added.", HttpStatus.OK);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while saving the Minimum Attempts. Please try again.", HttpStatus.BAD_REQUEST);
	    }
	}



@GetMapping(MinimumAttemptsServiceAPIs.GET_ALL_MINIMUM_ATTEMPTS)
public ResponseEntity<?> getAllMinimumAttempts(){
	
	List<MinimumAttempts> minimumAttemptsList = minimumAttemptsServ.getAllMinimumAttempts();
	return new ResponseEntity<>(minimumAttemptsList, HttpStatus.OK); 
	
}

@GetMapping(MinimumAttemptsServiceAPIs.GET_ALL_ACTIVE_MINIMUM_ATTEMPTS)
public ResponseEntity<?> getAllMinimumAttemptsActiveStatus(){
	
	List<MinimumAttempts> minimumAttemptsList = minimumAttemptsServ.getAllActiveMinimumAttempts();
	return new ResponseEntity<>(minimumAttemptsList, HttpStatus.OK); 
	
}

@GetMapping(MinimumAttemptsServiceAPIs.CHANGE_MINIMUM_ATTEMPTS_STATUS)
public void changeMinimumAttemptsStatus(@RequestParam("id") String stateId) {
	
	String id = EncryptionUtil.decrypt(stateId);
	
	try {
		MinimumAttempts c = minimumAttemptsServ.getMinimumAttemptsById(Long.parseLong(id));
		
		if(c.getStatus().equals("Active"))
			c.setStatus("Inactive");
		else
			c.setStatus("Active");
		
		minimumAttemptsServ.updateMinimumAttempts(c);
	}
	catch(Exception e) {
		
	}
	
	
	
}

@GetMapping(MinimumAttemptsServiceAPIs.GET_MINIMUM_ATTEMPTS_BY_ID)
public ResponseEntity<?> getMinimumnAttemptsByID(@RequestParam("id") String minimunAttemptsId)
{
	String id = EncryptionUtil.decrypt(minimunAttemptsId);
	
	try {
		MinimumAttempts c = minimumAttemptsServ.getMinimumAttemptsById(Long.parseLong(id));
		System.out.println(c.getAttemptId());
		return new ResponseEntity<>(c, HttpStatus.OK); 
	}catch(Exception e) {
		return new ResponseEntity<>("Invalid Attempts Id.", HttpStatus.BAD_REQUEST);
	}
	
}

@GetMapping(MinimumAttemptsServiceAPIs.DELETE_MINIMUM_ATTEMPTS_BY_ID)
public ResponseEntity<?> deleteMinimumAttemptsById(@RequestParam("id") String minimunAttemptsId) {
	
	String id = EncryptionUtil.decrypt(minimunAttemptsId);
	
	
		boolean res = minimumAttemptsServ.deleteByMinimumAttemptId(Long.parseLong(id));
		
		if(res) {
			return new ResponseEntity<>("Minimum Attempts is successfully deleted.", HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<>("Error in deleting the Minimum Attempts.", HttpStatus.BAD_REQUEST);
		}
	
}


@PostMapping(MinimumAttemptsServiceAPIs.UPDATE_MINIMUM_ATTEMPTS)
public ResponseEntity<?> updateMinimumAttempts(@RequestBody MinimumAttemptsDTO minimumAttemptsObj) {
    
	// Validate applicationScrutiny
    if (minimumAttemptsObj.getApplicationScrutiny() == null) {
        return new ResponseEntity<>("Please enter application scrutiny attempts.", HttpStatus.BAD_REQUEST);
    } else if (!(minimumAttemptsObj.getApplicationScrutiny() > 1 && minimumAttemptsObj.getApplicationScrutiny() < 10)) {
        return new ResponseEntity<>("Application Scrutiny attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
    }

    // Validate applicationReview
    if (minimumAttemptsObj.getApplicationReview() == null) {
        return new ResponseEntity<>("Please enter application review attempts.", HttpStatus.BAD_REQUEST);
    }else if (!(minimumAttemptsObj.getApplicationReview() > 1 && minimumAttemptsObj.getApplicationReview() < 10)) {
        return new ResponseEntity<>("Application Review attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
    }
    
    
    // Validate applicationAudit
    if (minimumAttemptsObj.getApplicationAudit() == null) {
        return new ResponseEntity<>("Please enter application audit attempts.", HttpStatus.BAD_REQUEST);
    }else if (!(minimumAttemptsObj.getApplicationAudit() > 1 && minimumAttemptsObj.getApplicationAudit() < 10)) {
        return new ResponseEntity<>("Application audit attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
    }

    // Validate esignApplicationReview
    if (minimumAttemptsObj.getEsignApplicationReview() == null) {
    	return new ResponseEntity<>("Please enter eSign application attempts.", HttpStatus.BAD_REQUEST);
    } else if (!(minimumAttemptsObj.getEsignApplicationReview() > 1 && minimumAttemptsObj.getEsignApplicationReview() < 10)) {
        return new ResponseEntity<>("eSign application review attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
    }

    // Validate annualAuditReview
    if (minimumAttemptsObj.getAnnualAuditReview() == null) {
        return new ResponseEntity<>("Please enter annual audit review attemps.", HttpStatus.BAD_REQUEST);
    } else if (!(minimumAttemptsObj.getAnnualAuditReview() > 1 && minimumAttemptsObj.getAnnualAuditReview() < 10)) {
        return new ResponseEntity<>("Annual audit review attempt should be in the range of 1 to 9.", HttpStatus.BAD_REQUEST);
    }

    try {
        List<MinimumAttempts> minimumAttemptsList = minimumAttemptsServ.getAllMinimumAttempts();

        if (minimumAttemptsList.isEmpty()) {
            // Create a new MinimumAttempts object and set its properties
            MinimumAttempts newMinimumAttempts = new MinimumAttempts();
            newMinimumAttempts.setAnnualAuditReview(minimumAttemptsObj.getAnnualAuditReview());
            newMinimumAttempts.setApplicationAudit(minimumAttemptsObj.getApplicationAudit());
            newMinimumAttempts.setApplicationReview(minimumAttemptsObj.getApplicationReview());
            newMinimumAttempts.setApplicationScrutiny(minimumAttemptsObj.getApplicationScrutiny());
            newMinimumAttempts.setEsignApplicationReview(minimumAttemptsObj.getEsignApplicationReview());
            newMinimumAttempts.setStatus("Active");

            // Save the new MinimumAttempts object to the database
            minimumAttemptsServ.addMinimumAttempts(newMinimumAttempts);

            // Print the status of the new MinimumAttempts object
            System.out.println(newMinimumAttempts.getStatus());

            return new ResponseEntity<>("Minimum Attempts successfully updated.", HttpStatus.OK);
        } else {
            // Set the status of each MinimumAttempts to "Inactive"
            for (MinimumAttempts attempt : minimumAttemptsList) {
                attempt.setStatus("Inactive");
            }

            // Save the updated MinimumAttempts back to the database
            for (MinimumAttempts attempt : minimumAttemptsList) {
                minimumAttemptsServ.addMinimumAttempts(attempt);
            }

            // Create a new MinimumAttempts object and set its properties
            MinimumAttempts newMinimumAttempts = new MinimumAttempts();
            newMinimumAttempts.setAnnualAuditReview(minimumAttemptsObj.getAnnualAuditReview());
            newMinimumAttempts.setApplicationAudit(minimumAttemptsObj.getApplicationAudit());
            newMinimumAttempts.setApplicationReview(minimumAttemptsObj.getApplicationReview());
            newMinimumAttempts.setApplicationScrutiny(minimumAttemptsObj.getApplicationScrutiny());
            newMinimumAttempts.setEsignApplicationReview(minimumAttemptsObj.getEsignApplicationReview());
            newMinimumAttempts.setStatus("Active");

            // Save the new MinimumAttempts object to the database
            Optional<MinimumAttempts> savedAttempt = minimumAttemptsServ.addMinimumAttempts(newMinimumAttempts);
            System.out.println(newMinimumAttempts.getStatus());

            // Check if the new MinimumAttempts object was saved successfully
            if (savedAttempt.isEmpty()) {
                return new ResponseEntity<>("An error occurred while updating the Minimum Attempts. Please try again.", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Minimum Attempts successfully updated.", HttpStatus.OK);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("An error occurred while updating the Minimum Attempts. Please try again.", HttpStatus.BAD_REQUEST);
    }
}

}