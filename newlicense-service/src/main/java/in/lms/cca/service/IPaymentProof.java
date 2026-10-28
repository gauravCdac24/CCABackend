package in.lms.cca.service;

import java.util.Optional;

import in.lms.cca.entity.PaymentProof;

public interface IPaymentProof {

	Optional<PaymentProof> addPaymentProof(PaymentProof paymentProof);

	PaymentProof getChangedThePaymentStatus(Long intentAppId);

	Optional<PaymentProof> updateIntentApp(PaymentProof inApp);

	PaymentProof getAllIntentApplicationByIntentId(Long decryptedIntentId);

}
