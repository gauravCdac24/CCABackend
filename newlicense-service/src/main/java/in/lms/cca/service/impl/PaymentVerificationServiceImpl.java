package in.lms.cca.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.PaymentVerification;
import in.lms.cca.repository.PaymentVarificationRepository;
import in.lms.cca.service.IPaymentVerficationService;

@Service
@Transactional
public class PaymentVerificationServiceImpl implements IPaymentVerficationService {
	
	@Autowired
	private PaymentVarificationRepository paymentVarificationRepository;

	@Override
	public Optional<PaymentVerification> addPaymentProof(PaymentVerification paymentVerification) {
		
		 if (paymentVerification == null) {
	            return Optional.empty(); 
	        }
	    try {
	    
	        PaymentVerification savedPayment = paymentVarificationRepository.save(paymentVerification);
	        
	       
	        return Optional.of(savedPayment);
	    } catch (Exception e) {
	       
	        e.printStackTrace();
	        return Optional.empty();
	    }
	}

	@Override
	public PaymentVerification getByIntentId(IntentApplication intentApp) {
		// TODO Auto-generated method stub
		return paymentVarificationRepository.getByIntentId(intentApp);
	}

	@Override
	public Optional<PaymentVerification> updatePymentVerification(PaymentVerification paymentVerification) {
		 if (paymentVerification == null) {
	            return Optional.empty(); 
	        }
		 if (paymentVerification.getPaymentVerifiId() == null) {
	            return Optional.empty(); 
	        }
	    try {
	    
	        PaymentVerification savedPayment = paymentVarificationRepository.save(paymentVerification);
	        
	       
	        return Optional.of(savedPayment);
	    } catch (Exception e) {
	       
	        e.printStackTrace();
	        return Optional.empty();
	    }
	}

	@Override
	public PaymentVerification getChangedThePaymentStatus(Long intentAppId) {
		// TODO Auto-generated method stub
		return paymentVarificationRepository.getChangedThePaymentStatus(intentAppId);
	}
	

}
