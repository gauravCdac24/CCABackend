package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.City;

public interface CityRepository extends JpaRepository<City, Long>{

	@Query("FROM City a WHERE a.cityId=:cityId")
	City findByCityId (@Param("cityId")Long cityId);

	@Query("FROM City a WHERE a.stateId.stateId=:stateId")
	List<City> findCityByStateId(Long stateId);
	
	// 
	
	@Query("FROM City WHERE status = 'Active' ORDER BY cityName ASC")
    List<City> findAllActiveCity();
	
    @Query("FROM City WHERE status = 'Inactive' ORDER BY cityName ASC")
    List<City> findAllInActiveCity();
	
	@Query("FROM City a WHERE a.cityName=:cityName")
	City findByCityName(@Param("cityName")String cityName);

	@Query("FROM City a WHERE a.cityId=:cityId")
	boolean deletebyCityId(@Param("cityId")long cityId);
	
}
