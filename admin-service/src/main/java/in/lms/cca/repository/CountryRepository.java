package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.Country;
import jakarta.transaction.Transactional;

public interface CountryRepository extends JpaRepository<Country, Long>{

		//Delete
		@Modifying
		@Transactional
		@Query("DELETE FROM Country c WHERE c.countryId=:countryId")
		void deleteByCountryId(Long countryId);
		
		//Find By Country Id
		@Query("FROM Country a WHERE a.countryId=:countryId")
		Country findByCountryId(Long countryId);
		
		//Find By Country Name
		@Query("FROM Country a WHERE a.countryName =:countryName")
		Country findByCountryName(String countryName);
		
		//Find By Phone Code
		@Query("FROM Country a WHERE a.phoneCode =:phoneCode")
		Country findByPhoneCode(String phoneCode);
				
		@Query("FROM Country WHERE status = 'Active' ORDER BY created DESC")
		List<Country> findAllActiveCountry();
		
		@Query("FROM Country WHERE status = 'Inactive' ORDER BY created DESC")
		List<Country> findAllInActiveCountry();
	
}
