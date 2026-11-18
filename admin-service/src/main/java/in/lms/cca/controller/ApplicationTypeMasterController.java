package in.lms.cca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.ApplicationTypeMasterDTO;
import in.lms.cca.entity.ApplicationTypeMaster;
import in.lms.cca.service.IApplicationTypeMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.ApplicationTypeMasterAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class ApplicationTypeMasterController {

    @Autowired
    private IApplicationTypeMasterService appTypeServ;

    @PostMapping(ApplicationTypeMasterAPIs.ADD_APPLICATION_TYPE)
    public ResponseEntity<?> addApplicationType(@RequestBody ApplicationTypeMasterDTO appTypeDTO) {
        // Server-side validation
        if (appTypeDTO.getAppType() == null || appTypeDTO.getAppType().trim().isEmpty()) {
            return new ResponseEntity<>("Application Type cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        if (!appTypeDTO.getAppType().trim().matches("^[A-Za-z ]+$")) {
            return new ResponseEntity<>("Application Type can only contain alphabets and spaces.", HttpStatus.BAD_REQUEST);
        }

        if (appTypeDTO.getAppType().length() < 3 || appTypeDTO.getAppType().length() > 50) {
            return new ResponseEntity<>("Application Type length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }

        // Check for unique application type
        if (appTypeServ.getApplicationTypeMasterByName(appTypeDTO.getAppType().trim()) != null) {
            return new ResponseEntity<>("Application Type already exists.", HttpStatus.BAD_REQUEST);
        }

        try {
            ApplicationTypeMaster appType = new ApplicationTypeMaster();
            appType.setAppType(appTypeDTO.getAppType().trim());
            appType.setStatus("Active");

            Optional<ApplicationTypeMaster> result = appTypeServ.addApplicationType(appType);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Error occurred while saving Application Type.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Application Type successfully added.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while saving the Application Type.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(ApplicationTypeMasterAPIs.GET_ALL_APPLICATION_TYPES)
    public ResponseEntity<?> getAllApplicationTypes() {
        List<ApplicationTypeMaster> appTypeList = appTypeServ.getAllApplicationTypeMaster();
        return new ResponseEntity<>(appTypeList, HttpStatus.OK);
    }

    @GetMapping(ApplicationTypeMasterAPIs.GET_APPLICATION_TYPE_BY_ID)
    public ResponseEntity<?> getApplicationTypeById(@RequestParam("id") String appTypeId) {
        try {
            Long id = Long.parseLong(EncryptionUtil.decrypt(appTypeId));
            ApplicationTypeMaster appType = appTypeServ.getApplicationTypeMasterById(id);
            
            if (appType == null) {
                return new ResponseEntity<>("Application Type not found.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(appType, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid Application Type ID.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApplicationTypeMasterAPIs.UPDATE_APPLICATION_TYPE)
    public ResponseEntity<?> updateApplicationType(@RequestBody ApplicationTypeMasterDTO appTypeDTO) {
        // Server-side validation
        if (appTypeDTO.getAppType() == null || appTypeDTO.getAppType().trim().isEmpty()) {
            return new ResponseEntity<>("Application Type cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        if (!appTypeDTO.getAppType().trim().matches("^[A-Za-z ]+$")) {
            return new ResponseEntity<>("Application Type can only contain alphabets and spaces.", HttpStatus.BAD_REQUEST);
        }

        if (appTypeDTO.getAppType().length() < 3 || appTypeDTO.getAppType().length() > 50) {
            return new ResponseEntity<>("Application Type length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }

        try {
            Long id = Long.parseLong(EncryptionUtil.decrypt(appTypeDTO.getAppTypeMasterId()));

            // Check if the application type exists
            ApplicationTypeMaster appType = appTypeServ.getApplicationTypeMasterById(id);
            if (appType == null) {
                return new ResponseEntity<>("Application Type not found.", HttpStatus.NOT_FOUND);
            }

            // Check for unique application type
            ApplicationTypeMaster existingAppType = appTypeServ.getApplicationTypeMasterByName(appTypeDTO.getAppType().trim());
            if (existingAppType != null && !existingAppType.getAppTypeMasterId().equals(id)) {
                return new ResponseEntity<>("Application Type already exists.", HttpStatus.BAD_REQUEST);
            }

            appType.setAppType(appTypeDTO.getAppType().trim());
            appType.setStatus(appTypeDTO.getStatus());

            Optional<ApplicationTypeMaster> result = appTypeServ.updateApplicationType(appType);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Error occurred while updating Application Type.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Application Type successfully updated.", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Application Type ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the Application Type.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
