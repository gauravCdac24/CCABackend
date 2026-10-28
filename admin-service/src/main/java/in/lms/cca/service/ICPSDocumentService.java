package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.CPSDocument;

public interface ICPSDocumentService {

	Optional<CPSDocument> addCPSDocument(CPSDocument cpsDocumentObj);
	Optional<CPSDocument> updateCPSDocument(CPSDocument cpsDocumentObj);
	List<CPSDocument> getAllCPSDocument();
	List<CPSDocument> getAllActiveCPSDocument();
	List<CPSDocument> getAllInactiveCPSDocument();
	CPSDocument getByCPSDocId (Long cpsDocId);
	boolean deleteBycpsDocId(Long cpsDocId);
	CPSDocument getCPSDocById(long cpsDocuId);
	Optional<CPSDocument> downloadfile(long cpsDocuId);
}
