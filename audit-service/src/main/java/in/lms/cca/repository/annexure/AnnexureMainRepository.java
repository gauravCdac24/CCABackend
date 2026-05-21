package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.AnnexureMain;

public interface AnnexureMainRepository extends JpaRepository<AnnexureMain, Long>{

	@Query("FROM AnnexureMain a WHERE a.status = :status AND a.userName = :username")
	AnnexureMain findAnnexureByStatusAndUsername(String status, String username);

	
	@Query("FROM AnnexureMain a WHERE a.status ='Active'")
	List<AnnexureMain> getAllAnnexureMain();

	@Query("FROM AnnexureMain a WHERE a.userName = :username ORDER BY a.annexureMainId DESC")
	List<AnnexureMain> findByUserNameOrderByIdDesc(String username);

}
