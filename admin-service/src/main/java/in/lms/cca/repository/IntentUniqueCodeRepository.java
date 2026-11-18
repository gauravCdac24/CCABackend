package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.IntentUniqueCode;


public interface IntentUniqueCodeRepository extends JpaRepository<IntentUniqueCode, Long>{

	@Query("FROM IntentUniqueCode a WHERE a.uniqueCode=:uniqueCode AND status='Active'")
	IntentUniqueCode findByActiveUniqueCode(Long uniqueCode);

	@Query("FROM IntentUniqueCode a WHERE a.uniqueCodeId=:uniqueCodeId")
	IntentUniqueCode findUniqueCodeById(Long uniqueCodeId);

	@Query("FROM IntentUniqueCode a WHERE a.emailId=:emailId")
	IntentUniqueCode findUniqueCodeByEmailId(String emailId);

	@Query("FROM IntentUniqueCode a WHERE a.mobileNo=:mobileNo")
	IntentUniqueCode findUniqueCodeByMobileNo(String mobileNo);

	@Query("FROM IntentUniqueCode a WHERE a.organizationName=:organizationName")
	IntentUniqueCode findUniqueCodeByOrganizationName(String organizationName);

	@Query("FROM IntentUniqueCode a WHERE a.uniqueCodeId=:id AND status='Active'")
	IntentUniqueCode findActiveUniqueCodeById(Long id);

	@Query("FROM IntentUniqueCode a WHERE a.uniqueCode=:uniqueCode")
	IntentUniqueCode findByUniqueCode(Long uniqueCode);

}
