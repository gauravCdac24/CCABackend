package in.lms.cca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.lms.cca.dto.ServiceMasterDTO;
import in.lms.cca.entity.master.ServiceMaster;
import in.lms.cca.service.IServiceMasterService;
import in.lms.cca.util.api.ServiceMasterAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin
public class ServiceMasterController {

    @Autowired
    private IServiceMasterService serviceMasterServ;

    @PostMapping(ServiceMasterAPIs.ADD_SERVICE)
    public ResponseEntity<?> addService(@RequestBody ServiceMasterDTO serviceDTO) {
        // Server-side validation
        if (serviceDTO.getServiceTitle() == null || serviceDTO.getServiceTitle().trim().isEmpty()) {
            return new ResponseEntity<>("Service title cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        if (!serviceDTO.getServiceTitle().trim().matches("^[A-Za-z0-9 ]+$")) {
            return new ResponseEntity<>("Service title can only contain alphanumeric characters and spaces.", HttpStatus.BAD_REQUEST);
        }

        if (serviceDTO.getServiceTitle().length() < 3 || serviceDTO.getServiceTitle().length() > 100) {
            return new ResponseEntity<>("Service title length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
        }

        // Check for unique service title
        ServiceMaster existingService = serviceMasterServ.getServiceMasterByName(serviceDTO.getServiceTitle().trim());
        if (existingService != null) {
            return new ResponseEntity<>("Duplicate service title is not allowed.", HttpStatus.BAD_REQUEST);
        }

        try {
            ServiceMaster service = new ServiceMaster();
            service.setServiceTitle(serviceDTO.getServiceTitle().trim());
            service.setStatus("Active");
            service.setCreatedBy(serviceDTO.getCreatedBy());
            service.setUpdatedBy(serviceDTO.getUpdatedBy());

            Optional<ServiceMaster> result = serviceMasterServ.addServiceMaster(service);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Error occurred while saving Service.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Service successfully added.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while saving the Service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(ServiceMasterAPIs.GET_ALL_SERVICES)
    public ResponseEntity<?> getAllServices() {
        List<ServiceMaster> serviceList = serviceMasterServ.getAllServiceMaster();
        return new ResponseEntity<>(serviceList, HttpStatus.OK);
    }

    @GetMapping(ServiceMasterAPIs.GET_SERVICE_BY_ID)
    public ResponseEntity<?> getServiceById(@RequestParam("id") String serviceId) {
        try {
            Long id = Long.parseLong(EncryptionUtil.decrypt(serviceId));
            ServiceMaster service = serviceMasterServ.getServiceMasterById(id);

            if (service == null) {
                return new ResponseEntity<>("Service not found.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(service, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Service ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid Service ID.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ServiceMasterAPIs.UPDATE_SERVICE)
    public ResponseEntity<?> updateService(@RequestBody ServiceMasterDTO serviceDTO) {
        // Server-side validation
        if (serviceDTO.getServiceTitle() == null || serviceDTO.getServiceTitle().trim().isEmpty()) {
            return new ResponseEntity<>("Service title cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        if (!serviceDTO.getServiceTitle().trim().matches("^[A-Za-z0-9 ]+$")) {
            return new ResponseEntity<>("Service title can only contain alphanumeric characters and spaces.", HttpStatus.BAD_REQUEST);
        }

        if (serviceDTO.getServiceTitle().length() < 3 || serviceDTO.getServiceTitle().length() > 100) {
            return new ResponseEntity<>("Service title length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
        }

        try {
            Long id = Long.parseLong(EncryptionUtil.decrypt(serviceDTO.getServiceId()));
            ServiceMaster service = serviceMasterServ.getServiceMasterById(id);
            if (service == null) {
                return new ResponseEntity<>("Service not found.", HttpStatus.NOT_FOUND);
            }

            // Check for unique service title
            ServiceMaster existingService = serviceMasterServ.getServiceMasterByName(serviceDTO.getServiceTitle().trim());
            if (existingService != null && !existingService.getServiceId().equals(id)) {
                return new ResponseEntity<>("Duplicate service title is not allowed.", HttpStatus.BAD_REQUEST);
            }

            service.setServiceTitle(serviceDTO.getServiceTitle().trim());
            service.setStatus(serviceDTO.getStatus());
            service.setUpdatedBy(serviceDTO.getUpdatedBy());

            Optional<ServiceMaster> result = serviceMasterServ.updateServiceMaster(service);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Error occurred while updating Service.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Service successfully updated.", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Service ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the Service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

    
}
