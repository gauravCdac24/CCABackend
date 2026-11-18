package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.CPSDocument;
import jakarta.transaction.Transactional;

public interface CPSDocumentRepository extends JpaRepository<CPSDocument, Long>{

	@Query("FROM CPSDocument a WHERE a.cpsDocId=:cpsDocId")
	CPSDocument findByCPSDocId (@Param("cpsDocId")Long cpsDocId);

	@Query("FROM CPSDocument WHERE status = 'Active' ORDER BY created DESC")
	List<CPSDocument> findAllActiveCPSDocument();
	
	@Query("FROM CPSDocument WHERE status = 'Inactive' ORDER BY created DESC")
	List<CPSDocument> findAllInActiveCPSDocument();
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CPSDocument c WHERE c.cpsDocId=:cpsDocId")
	void deleteBycpsDocId(Long cpsDocId);
}
