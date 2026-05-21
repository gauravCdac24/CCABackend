package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AdditionalDocumentEntity;
import jakarta.transaction.Transactional;

public interface AdditionalDocumentRepository extends JpaRepository<AdditionalDocumentEntity, Long>{

	@Query("FROM AdditionalDocumentEntity a WHERE a.status='Active' ORDER BY created DESC")
	List<AdditionalDocumentEntity> findAllActiveAdditionalDocumentEntity();

	@Query("FROM AdditionalDocumentEntity a WHERE a.status='Inactive' ORDER BY created DESC")
	List<AdditionalDocumentEntity> findAllInActiveAdditionalDocumentEntity();

	@Query("FROM AdditionalDocumentEntity a WHERE a.additionalDocumentId=:id")
	AdditionalDocumentEntity findByAdditionalDocumentEntityId(@Param("id")Long id);

	@Modifying
	@Transactional
	@Query("DELETE FROM AdditionalDocumentEntity a WHERE a.additionalDocumentId=:stateId")
	void deleteByAdditionalDocumentEntityId(@Param("stateId")Long stateId);

	
	@Query("FROM AdditionalDocumentEntity a WHERE a.documentName=:documentname")
	AdditionalDocumentEntity getDocumentName(@Param("documentname")String documentname);
	

}
