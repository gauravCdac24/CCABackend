package in.lms.cca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.lms.cca.dto.DocumentsMasterDTO;
import in.lms.cca.entity.master.DocumentsMaster;
import in.lms.cca.service.IDocumentsMasterService;
import in.lms.cca.util.api.DocumentsMasterAPIs;
import in.lms.cca.util.api.RenewLicenseServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(RenewLicenseServiceAPIs.RENEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin
public class DocumentsMasterController {

    @Autowired
    private IDocumentsMasterService documentsMasterServ;

    @PostMapping(DocumentsMasterAPIs.ADD_DOCUMENTS)
    public ResponseEntity<?> addDocument(@RequestBody DocumentsMasterDTO docDTO) {
        // Server-side validation
        if (docDTO.getDocumentsTitle() == null || docDTO.getDocumentsTitle().trim().isEmpty()) {
            return new ResponseEntity<>("Document title cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        if (!docDTO.getDocumentsTitle().trim().matches("^[A-Za-z0-9 ]+$")) {
            return new ResponseEntity<>("Document title can only contain alphanumeric characters and spaces.", HttpStatus.BAD_REQUEST);
        }

        if (docDTO.getDocumentsTitle().length() < 3 || docDTO.getDocumentsTitle().length() > 100) {
            return new ResponseEntity<>("Document title length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
        }

        // Check for unique document title
        DocumentsMaster existingDoc = documentsMasterServ.getDocumentsMasterByName(docDTO.getDocumentsTitle().trim());
        if (existingDoc != null) {
            return new ResponseEntity<>("Duplicate document title is not allowed.", HttpStatus.BAD_REQUEST);
        }

        try {
            DocumentsMaster doc = new DocumentsMaster();
            doc.setDocumentsTitle(docDTO.getDocumentsTitle().trim());
            doc.setMandatory(docDTO.isMandatory());
            doc.setDocumentsFor(docDTO.getDocumentsFor());
            doc.setStatus("Active");

            Optional<DocumentsMaster> result = documentsMasterServ.addDocumentsMaster(doc);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Error occurred while saving Document.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Document successfully added.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while saving the Document.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(DocumentsMasterAPIs.GET_ALL_DOCUMENTS)
    public ResponseEntity<?> getAllDocuments() {
        List<DocumentsMaster> documentsList = documentsMasterServ.getAllDocumentsMaster();
        return new ResponseEntity<>(documentsList, HttpStatus.OK);
    }

    @GetMapping(DocumentsMasterAPIs.GET_DOCUMENTS_BY_ID)
    public ResponseEntity<?> getDocumentById(@RequestParam("id") String docId) {
        try {
            Long id = Long.parseLong(EncryptionUtil.decrypt(docId));
            DocumentsMaster doc = documentsMasterServ.getDocumentsMasterById(id);

            if (doc == null) {
                return new ResponseEntity<>("Document not found.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(doc, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Document ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid Document ID.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(DocumentsMasterAPIs.UPDATE_DOCUMENTS)
    public ResponseEntity<?> updateDocument(@RequestBody DocumentsMasterDTO docDTO) {
        // Server-side validation
        if (docDTO.getDocumentsTitle() == null || docDTO.getDocumentsTitle().trim().isEmpty()) {
            return new ResponseEntity<>("Document title cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        if (!docDTO.getDocumentsTitle().trim().matches("^[A-Za-z0-9 ]+$")) {
            return new ResponseEntity<>("Document title can only contain alphanumeric characters and spaces.", HttpStatus.BAD_REQUEST);
        }

        if (docDTO.getDocumentsTitle().length() < 3 || docDTO.getDocumentsTitle().length() > 100) {
            return new ResponseEntity<>("Document title length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
        }

        try {
            Long id = Long.parseLong(EncryptionUtil.decrypt(docDTO.getDocumentsId()));
            DocumentsMaster doc = documentsMasterServ.getDocumentsMasterById(id);
            if (doc == null) {
                return new ResponseEntity<>("Document not found.", HttpStatus.NOT_FOUND);
            }

            // Check for unique document title
            DocumentsMaster existingDoc = documentsMasterServ.getDocumentsMasterByName(docDTO.getDocumentsTitle().trim());
            if (existingDoc != null && !existingDoc.getDocumentsId().equals(id)) {
                return new ResponseEntity<>("Duplicate document title is not allowed.", HttpStatus.BAD_REQUEST);
            }

            doc.setDocumentsTitle(docDTO.getDocumentsTitle().trim());
            doc.setMandatory(docDTO.isMandatory());
            doc.setDocumentsFor(docDTO.getDocumentsFor());
            doc.setStatus(docDTO.getStatus());

            Optional<DocumentsMaster> result = documentsMasterServ.updateDocumentsMaster(doc);
            if (result.isEmpty()) {
                return new ResponseEntity<>("Error occurred while updating Document.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Document successfully updated.", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Document ID format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the Document.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
