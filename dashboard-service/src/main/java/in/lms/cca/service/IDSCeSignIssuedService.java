package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.DSCeSignIssued;

public interface IDSCeSignIssuedService {

	Optional<DSCeSignIssued> addDSCeSignIssued(DSCeSignIssued obj);
	List<DSCeSignIssued> getAllDSCeSignIssued();
	List<DSCeSignIssued> getDSCeSignIssuedByUsername(String username);
	DSCeSignIssued getDSCeSignIssuedById(Long id);
	DSCeSignIssued getDSCeSignIssuedByYearMonthCountryAndState(String countryId, String stateId, String year,
			String month);
	List<DSCeSignIssued> getAllEsignDSCByCAMonthStateAndYear(List<String> ca, List<String> month, List<String> state,
			List<String> year);
	List<DSCeSignIssued> getMonthlyDSCEsignData(String month, String year);
	List<DSCeSignIssued> getYearlyDSCEsignData(String years);
	DSCeSignIssued getDSCeSignIssuedByYearMonthCountryAndStateUsername(String caUsername, String countryId, String stateId,
			String year, String month);
	List<DSCeSignIssued> getAllDSCeSignIssuedByYearMonthAndUsername(String username, String month, String year);
	
	
}
