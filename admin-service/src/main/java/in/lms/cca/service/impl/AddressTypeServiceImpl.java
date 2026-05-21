package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AddressType;
import in.lms.cca.repository.AddressTypeRepository;
import in.lms.cca.service.IAddressTypeService;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class AddressTypeServiceImpl implements IAddressTypeService{

	@Autowired
	private AddressTypeRepository addressTypeRepo;
	
	@Override
	public Optional<AddressType> addAddressType(AddressType addressType) {
		
		if(addressType == null) {
		
			Optional.empty();
		}
		
		try {
			return Optional.of(addressTypeRepo.save(addressType));
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<AddressType> updateAddressType(AddressType addressType) {
		
		if(addressType == null) {
			return Optional.empty();
		}
		
		if(addressType.getAddressTypeId() == null) {
			return Optional.empty();
		}
		
		try {
			return Optional.of(addressTypeRepo.save(addressType));
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public boolean deleteAddressTypeById(Integer id) {
		
		AddressType addressType = addressTypeRepo.findByAddressTypeId(id);
		
		if(addressType == null)
			return false;
		
		try {
			addressTypeRepo.deleteByAddressTypeId(id);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public AddressType getAddressTypeById(Integer id) {
		return addressTypeRepo.findByAddressTypeId(id);
	}

	@Override
	public List<AddressType> getAllAddressType() {
		return addressTypeRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public AddressType getAddressTypeByName(String addressType) {
		
		return addressTypeRepo.findAddressTypeByName(addressType);
	}



}
