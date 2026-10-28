package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.State;

public interface IStateService {

	Optional<State> addState(State stateObj);
	Optional<State> updateState(State stateObj);
	List<State> getAllState();
	List<State> getAllActiveState();
	List<State> getAllInactiveState();
	List<State> getAllStateByCountryId(Long id);
	State getStateById(Long id);
	State getStateName(String stateName);
	boolean deleteByStateId(Long stateId);
	List<State> getAllStateByCountryName(String countryName);
	
}
