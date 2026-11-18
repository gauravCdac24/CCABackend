package in.lms.cca.service;

import java.util.List;
import java.util.Optional;
import in.lms.cca.entity.AddressType;

public interface IAddressTypeService {

	Optional<AddressType> addAddressType(AddressType addressType);
	Optional<AddressType> updateAddressType(AddressType addressType);
	boolean deleteAddressTypeById(Integer id);
	AddressType getAddressTypeById(Integer id);
	List<AddressType> getAllAddressType();
	AddressType getAddressTypeByName(String addressType);
	
}
