package in.lms.cca.service;

import java.util.Optional;

import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.PaymentVerification;

public interface IPaymentVerficationService {

	Optional<PaymentVerification> addPaymentProof(PaymentVerification paymentVerification);

	PaymentVerification getByIntentId(IntentApplication intentApp);

	Optional<PaymentVerification> updatePymentVerification(PaymentVerification paymentVerification);

	PaymentVerification getChangedThePaymentStatus(Long intentAppId);

}
