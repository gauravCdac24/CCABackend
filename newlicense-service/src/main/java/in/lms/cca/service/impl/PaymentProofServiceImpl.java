package in.lms.cca.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.PaymentProof;
import in.lms.cca.repository.PaymentProofRepository;
import in.lms.cca.service.IPaymentProof;


@Service
@Transactional
public class PaymentProofServiceImpl implements IPaymentProof {
	
	
	@Autowired
	private PaymentProofRepository paymentProofRepository;
	
	

	@Override
    public Optional<PaymentProof> addPaymentProof(PaymentProof paymentProof) {
        if (paymentProof == null) {
            return Optional.empty(); 
        }

        
        PaymentProof savedProof = paymentProofRepository.save(paymentProof);
        return Optional.of(savedProof);
    }



	@Override
	public PaymentProof getChangedThePaymentStatus(Long intentAppId) {
		// TODO Auto-generated method stub
		return paymentProofRepository.getChangedThePaymentStatus(intentAppId) ;
	}



	@Override
	public Optional<PaymentProof> updateIntentApp(PaymentProof inApp) {
		  if (inApp == null) {
	            return Optional.empty(); 
	        }
		  if (inApp.getPaymentProofId() == null) {
	            return Optional.empty(); 
	        }
	        
	        PaymentProof savedProof = paymentProofRepository.save(inApp);
	        return Optional.of(savedProof);
	}



	@Override
	public PaymentProof getAllIntentApplicationByIntentId(Long decryptedIntentId) {
		// TODO Auto-generated method stub
		return paymentProofRepository.getAllIntentApplicationByIntentId(decryptedIntentId);
	}
	

	
	

	
}
