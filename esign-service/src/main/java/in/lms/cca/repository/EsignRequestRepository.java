package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.ESignRequest;


public interface EsignRequestRepository extends JpaRepository<ESignRequest, Long>{

	ESignRequest findByTransactionId(String espTxnID);

}
