package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.DSCeSignIssued;

public interface DSCeSignIssuedRepository extends JpaRepository<DSCeSignIssued, Long>{

	@Query("FROM DSCeSignIssued a WHERE a.caUsername = :username ORDER BY created DESC")
	List<DSCeSignIssued> findDSCeSignIssuedByUsername(String username);

	@Query("FROM DSCeSignIssued a WHERE a.dscesignIssuedId = :id")
	DSCeSignIssued findDSCeSignIssuedById(Long id);

	@Query("FROM DSCeSignIssued a WHERE a.countryId = :countryId AND a.stateId = :stateId AND a.year = :year AND a.month = :month")
	DSCeSignIssued findDSCeSignIssuedByYearMonthCountryAndState(String countryId, String stateId, String year, String month);

	@Query("FROM DSCeSignIssued a WHERE a.caUsername IN (:ca) AND a.month IN (:month) AND a.stateId IN (:state) AND a.year IN (:year)")
	List<DSCeSignIssued> findAllEsignDSCByCAMonthStateAndYear(@Param("ca") List<String> ca, @Param("month") List<String> month, @Param("state") List<String> state, @Param("year") List<String> year);

	@Query("FROM DSCeSignIssued a WHERE a.month = :month AND a.year = :year")
	List<DSCeSignIssued> findMonthlyDSCEsignData(String month, String year);

	@Query("FROM DSCeSignIssued a WHERE a.year = :years")
	List<DSCeSignIssued> findYearlyDSCEsignData(String years);

	@Query("FROM DSCeSignIssued a WHERE a.caUsername = :caUsername AND a.countryId = :countryId AND a.stateId = :stateId AND a.year = :year AND a.month = :month")
	DSCeSignIssued getDSCeSignIssuedByYearMonthCountryAndStateUsername(@Param("caUsername") String caUsername, @Param("countryId") String countryId, @Param("stateId") String stateId, @Param("year") String year, @Param("month") String month);

	@Query("FROM DSCeSignIssued a WHERE a.caUsername = :username AND a.month = :month AND a.year = :year")
	List<DSCeSignIssued> getAllDSCeSignIssuedByYearMonthAndUsername(String username, String month, String year);

	


}
