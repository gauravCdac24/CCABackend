package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IntentApplication;

public interface IntentApplicationRepository extends JpaRepository<IntentApplication, Long> {

	@Query("FROM IntentApplication a WHERE a.userName=:userName ORDER BY a.intentAppId DESC")
	List<IntentApplication> findAllByUserName(@Param("userName") String userName);

	@Query("FROM IntentApplication a WHERE a.intentAppId=:intentAppId")
	IntentApplication findByIntentAppId(Long intentAppId);
	
	@Query(value = "SELECT ia.* " +
            "FROM new_license_management.intent_application ia " +
            "INNER JOIN audit_management.application_audit aa ON ia.user_name = aa.applicant_user_name " +
            "INNER JOIN audit_management.audit_agency_selection aas ON aa.app_audit_id = aas.app_audit_id " +
            "WHERE aas.audit_agency_id = :auditAgencyId", 
            nativeQuery = true)
List<IntentApplication> findByAuditAgencyId(@Param("auditAgencyId") Long auditAgencyId);


@Query(value = "SELECT ia.* FROM new_license_management.intent_application ia " +
            "INNER JOIN audit_management.application_audit aa ON ia.user_name = aa.applicant_user_name " +
            "INNER JOIN audit_management.audit_agency_selection aas ON aa.app_audit_id = aas.app_audit_id " +
            "WHERE aas.audit_agency_id = :auditAgencyId " +
            "AND ia.application_status = :applicationStatus",
            nativeQuery = true)
List<IntentApplication> findByAuditAgencyIdAndApplicationStatus(@Param("auditAgencyId") Long auditAgencyId,
        @Param("applicationStatus") String applicationStatus);



Optional<IntentApplication> findByUniqueCode(String uniqueCode);

Optional<IntentApplication> findByUserName(String userName);

@Query(value = "SELECT * FROM new_license_management.intent_application WHERE application_status != 'Draft' AND application_status != 'Payment_Pending' AND application_status != 'Payment_Failed' ORDER BY intent_app_id DESC", nativeQuery = true)
List<IntentApplication> findAllApplication();

boolean existsByUniqueCode(String uniqueCode);

@Query(value = "SELECT * FROM new_license_management.intent_application WHERE application_status =:applicationStatus", nativeQuery = true)
List<IntentApplication> findByApplicationStatus(@Param("applicationStatus") String applicationStatus);

@Query(value = "SELECT DISTINCT ia.* FROM new_license_management.intent_application ia " +
		"INNER JOIN new_license_management.payment_proof pp ON ia.intent_app_id = pp.intent_app_id " +
		"WHERE pp.status = 'Active' " +
		"AND (ia.application_status IS NULL OR ia.application_status NOT IN ('Edit_Upon_Review')) " +
		"ORDER BY ia.intent_app_id DESC",
		nativeQuery = true)
List<IntentApplication> findWithActivePaymentProofPendingVerification();
} 
