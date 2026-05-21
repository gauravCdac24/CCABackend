package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.Intent;

public interface IntentRepository extends JpaRepository<Intent, Long>{

	@Query("FROM Intent a WHERE a.intentId=:intentId")
	Intent findByIntentId (@Param("intentId") Long intentId);
	
	@Query("FROM Intent a WHERE a.addressId.addressId=:addressId")
	Intent findIntentByAddressId (Long addressId);
	
	@Query("FROM Intent WHERE status = 'Active' ORDER BY created DESC")
	List<Intent> findAllActiveIntent();
	
	@Query("FROM Intent WHERE status = 'Inactive' ORDER BY created DESC")
	List<Intent> findAllInActiveIntent();

	@Query("FROM Intent a WHERE a.intentId=:intentId")
	boolean deletebyIntentId(long intent);

	@Query("FROM Intent a WHERE a.intentId=:userId")
	Intent getByuserId(@Param("userId")Long userId);
	
	@Query("FROM Intent a WHERE a.uniqueCodeId.uniqueCodeId=:uniqueCodeId")
	Intent findByUniqueCodeId(@Param("uniqueCodeId") Long uniqueCodeId);
	
}
