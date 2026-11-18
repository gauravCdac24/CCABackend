package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.CSRDocument;
import jakarta.transaction.Transactional;

public interface CSRDocumentRepository extends JpaRepository<CSRDocument, Long> {

	@Query("FROM CSRDocument a WHERE a.csrDocId=:csrDocId")
	CSRDocument findByCSRDocId(@Param("csrDocId") Long csrDocId);

	@Query("FROM CSRDocument WHERE status = 'Active' ORDER BY created DESC")
	List<CSRDocument> findAllActiveCSRDocument();

	@Query("FROM CSRDocument WHERE status = 'Inactive' ORDER BY created DESC")
	List<CSRDocument> findAllInActiveCSRDocument();

	@Modifying
	@Transactional
	@Query("DELETE FROM CSRDocument c WHERE c.csrDocId=:csrDocId")
	void deleteBycsrDocId(Long csrDocId);
}
