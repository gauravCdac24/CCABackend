package in.lms.cca.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AddressType;
import jakarta.transaction.Transactional;

public interface AddressTypeRepository extends JpaRepository<AddressType, Integer>{

	//Delete
		@Modifying
		@Transactional
		@Query("DELETE FROM AddressType a WHERE a.addressTypeId=:addressTypeId")
		void deleteByAddressTypeId(Integer addressTypeId);
		
		//Find By Address Type Id
		@Query("FROM AddressType a WHERE a.addressTypeId=:addressTypeId")
		AddressType findByAddressTypeId(Integer addressTypeId);

		@Query("FROM AddressType a WHERE a.addressTypeName=:addressType")
		AddressType findAddressTypeByName(@Param("addressType") String addressType);
}


