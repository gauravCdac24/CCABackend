package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.DocumentMaster;

public interface DocumentRepository extends JpaRepository<DocumentMaster, Long>{

	@Query("FROM DocumentMaster a WHERE a.documentName =:documentName")
	DocumentMaster getDocumentByName(String documentName);

	@Query("FROM DocumentMaster a WHERE a.documentId =:documentId")
	DocumentMaster getDocumentById(Long documentId);

	@Query("FROM DocumentMaster a WHERE a.status ='Active'")
	List<DocumentMaster> getAllActiveDocument();

}
