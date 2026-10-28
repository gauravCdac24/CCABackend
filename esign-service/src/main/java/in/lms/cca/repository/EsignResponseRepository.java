package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.ESignResponse;

public interface EsignResponseRepository extends JpaRepository<ESignResponse, Long>{

	@Query("FROM ESignResponse a WHERE a.eSignResponseId = :id")
	ESignResponse getByEsignResponseId(Long id);

}
