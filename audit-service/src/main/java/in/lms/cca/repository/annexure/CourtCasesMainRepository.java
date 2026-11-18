
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CourtCasesMain;

public interface CourtCasesMainRepository extends JpaRepository<CourtCasesMain, Long>{

	@Query("FROM CourtCasesMain a WHERE a.annexureMainId.annexureMainId = :id")
	CourtCasesMain findByAnnexureId(Long id);
	
	@Query("FROM CourtCasesMain a WHERE a.courtCasesMainId = :id")
	CourtCasesMain findByCourtCasesMainId(Long id);
	
}

