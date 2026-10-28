package in.lms.cca.dto;

import java.util.List;

import in.lms.cca.entity.EkycMode;
import in.lms.cca.entity.EsignDocument;

public class StepTwoResponseDTO {

	private EsignDocument esignDocument;
	private List<EkycMode> eKYCMode;
	
	public EsignDocument getEsignDocument() {
		return esignDocument;
	}
	public void setEsignDocument(EsignDocument esignDocument) {
		this.esignDocument = esignDocument;
	}
	public List<EkycMode> geteKYCMode() {
		return eKYCMode;
	}
	public void seteKYCMode(List<EkycMode> eKYCMode) {
		this.eKYCMode = eKYCMode;
	}
	
	
	
}
