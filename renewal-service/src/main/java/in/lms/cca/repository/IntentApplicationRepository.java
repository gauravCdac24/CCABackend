package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IntentApplication;

public interface IntentApplicationRepository extends JpaRepository<IntentApplication, Long> {

	//
	@Query("FROM IntentApplication a WHERE a.userName=:userName")
	IntentApplication findIntentAppById(@Param("userName")String userName);

	@Query("FROM IntentApplication a WHERE a.intentAppId=:intentAppId")
	IntentApplication findByIntentAppId(Long intentAppId);

	@Query("FROM IntentApplication a WHERE a.userName=:aplicantUserName")
	List<IntentApplication> getAllIntentApplicationByApplicantUserName(@Param("aplicantUserName")String aplicantUserName);
	

} 
