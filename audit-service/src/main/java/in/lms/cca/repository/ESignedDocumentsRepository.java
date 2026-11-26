package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ESignedDocumentsEntity;

public interface ESignedDocumentsRepository extends JpaRepository< ESignedDocumentsEntity,Long> {

	@Query("FROM ESignedDocumentsEntity a WHERE a.eSignedId = :eSignDocId")
	ESignedDocumentsEntity getEsignDocumentById(@Param("eSignDocId")Long eSignDocId);

	@Query("FROM ESignedDocumentsEntity a WHERE a.userName = :eSignDocId")
	List<ESignedDocumentsEntity> getEsignDocumentByUserName(@Param("eSignDocId")String eSignDocId);


	/**
	 * All eSign rows for an applicant: the plain username (legacy / cover letter
	 * flows) plus every composite signer row such as {@code UCCA...|AUDITOR:63}
	 * or {@code UCCA...|ACCEPTANCE}.
	 */
	@Query("FROM ESignedDocumentsEntity a WHERE a.userName = :userName OR a.userName LIKE CONCAT(:userName, '|%')")
	List<ESignedDocumentsEntity> getAllForApplicant(@Param("userName") String userName);

}
