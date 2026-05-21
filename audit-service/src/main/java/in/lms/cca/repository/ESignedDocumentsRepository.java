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

}
