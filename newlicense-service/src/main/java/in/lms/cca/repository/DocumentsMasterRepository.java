package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.master.DocumentsMaster;

public interface DocumentsMasterRepository extends JpaRepository<DocumentsMaster, Long>{

	@Query("FROM DocumentsMaster a WHERE a.documentsId=:documentsId")
	DocumentsMaster findByDocumentsMasterId(@Param("documentsId")Long documentsId);

	@Query("SELECT d FROM DocumentsMaster d WHERE d.documentsTitle = :docName")
	DocumentsMaster findDocumentsMasterByName(String docName);
}
