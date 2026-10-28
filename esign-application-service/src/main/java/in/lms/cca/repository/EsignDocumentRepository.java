package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.EsignDocument;
import jakarta.transaction.Transactional;


public interface EsignDocumentRepository extends JpaRepository<EsignDocument, Long>{

	@Query("FROM EsignDocument a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.esignDocType = :documentType AND a.status = :status ORDER BY created DESC")
	List<EsignDocument> findEsignDocumentByAppIdDocumentTypeAndStatus(Long esignLicenseeAppId, String documentType, String status);

	@Query("FROM EsignDocument a WHERE a.eSignDocId = :id")
	EsignDocument findEsignDocumentById(Long id);

	@Modifying
	@Transactional
	@Query("UPDATE EsignDocument a SET a.status='Inactive' WHERE a.esignLicenseeAppId.esignLicenseeAppId = :id")
	void inactiveAllDocumentByAppId(Long id);

	@Query("FROM EsignDocument a WHERE a.fileName = :fileName")
	EsignDocument findEsignDocumentByFileName(String fileName);

}
