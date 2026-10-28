package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.entity.Address;

public interface IAddressService {

	Optional<Address> addAddress(Address address);
	Optional<Address> updateAddress(Address address);
	boolean deleteAddressById(Long id);
	Address getAddressById(Long id);
	List<Address> getAllAddress();
	Address getAddressByIdAndAddressTypeId(Long addressId, Integer addressTypeId);
	Address getAddressByIdAndAddressTypeName(Long addressId, String addressTypeName);
	List<Address> getAddressByCreatedBy(String createdBy);
	Address getAddressByIds(Long addressId);
	Optional<Address> addAddres(Address address);
	Optional<Address> updateAddres(Address address);
	
	
}
