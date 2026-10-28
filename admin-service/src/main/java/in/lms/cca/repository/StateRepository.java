package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.State;

public interface StateRepository extends JpaRepository<State, Long>{

		@Query("FROM State a WHERE a.stateId=:stateId")
		State findByStateId (@Param("stateId")Long stateId);

		@Query("FROM State a WHERE a.countryId.countryId=:countryId")
		List<State> findStateByCountryId(Long countryId);
		
		@Query("FROM State WHERE status = 'Active' ORDER BY created DESC")
		List<State> findAllActiveState();
		
		@Query("FROM State WHERE status = 'Inactive' ORDER BY created DESC")
		List<State> findAllInActiveState();

		@Query("FROM State a WHERE a.stateName=:stateName")
		State findByStateName(@Param("stateName")String stateName);

		
		@Query("DELETE FROM State c WHERE c.stateId=:stateId")
		void deleteByStateId(Long stateId);

		@Query("FROM State a WHERE a.countryId.countryName=:countryName")
		List<State> findAllStateByCountryName(String countryName);
	
}
