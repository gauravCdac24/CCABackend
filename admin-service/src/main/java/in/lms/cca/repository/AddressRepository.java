package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.Address;
import jakarta.transaction.Transactional;

public interface AddressRepository extends JpaRepository<Address, Long>{

	//Delete
	@Modifying
	@Transactional
	@Query("DELETE FROM Address a WHERE a.addressId=:addressId")
	void deleteByAddressId(Long addressId);
	
	//Find By Address Id
	@Query("FROM Address a WHERE a.addressId=:addressId")
	Address findByAddressId(@Param("addressId")Long addressId);
	
	//Find By Created By
	@Query("FROM Address a WHERE a.createdBy=:createdBy ORDER BY created DESC")
	List<Address> findByCreatedBy(String createdBy);
	
	//Find by address type id and address id
	@Query("FROM Address a WHERE a.addressId=:addressId AND a.addressTypeId.addressTypeId=:addressTypeId")
	Address findByAddressIdAndAddressTypeId(Long addressId, Integer addressTypeId);
	
	//Find by address type name and address id
	@Query("FROM Address a WHERE a.addressId=:addressId AND a.addressTypeId.addressTypeName=:addressTypeName")
	Address findByAddressIdAndAddressTypeName(Long addressId, String addressTypeName);

//	@Query("FROM Address a WHERE a.addressId=:addressId")
//	Address getAddressById(@Param("addressId")Long addressId);
//	
//	
}
