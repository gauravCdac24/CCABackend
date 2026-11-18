package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.LdifDocument;


public interface LdifDocumentRepository extends JpaRepository<LdifDocument, Long>{

}
