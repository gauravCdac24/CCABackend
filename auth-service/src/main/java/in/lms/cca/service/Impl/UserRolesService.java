package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.UserLogin;
import in.lms.cca.entity.UserRoles;
import in.lms.cca.repository.UserRolesRepository;
import in.lms.cca.service.IUserRolesService;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserRolesService implements IUserRolesService {

	@Autowired
	private UserRolesRepository uroleRepo;
	
	@Override
	public UserRoles saveUserRole(UserRoles urole) {
		
		return uroleRepo.save(urole);
		
	}

	@Override
	public List<UserRoles> getAllRolesByLoginId(Integer loginId) {
		return uroleRepo.findByLoginId(loginId);
	}

	@Override
	public Optional<UserRoles> updateRole(UserRoles updatedRole) {
		// TODO Auto-generated method stub
		if(updatedRole.getUserRoleId()==null)
		{
			return Optional.empty();
		}
		

		 try {
		        // Fetch the user using the repository method
		        UserRoles upatedRole =uroleRepo.save(updatedRole);
		        
		        // Return the fetched user if present
		        return Optional.of(upatedRole);
		    } catch (Exception e) {
		        // Log the exception (you may use a logger instead of printing to console)
		        System.err.println("An error occurred while fetching the user login: " + e.getMessage());
		        
		        // Return an empty Optional if an exception occurs
		        return Optional.empty();
		    }
		
		
	}

	@Override
	public UserRoles getRuserRoleById(Long roleId) {
		// TODO Auto-generated method stub
		return uroleRepo.findByLoginId(roleId);
	}

	@Override
	public List<UserRoles> getAllActiveRolesByLoginId(Integer intValue) {
		// TODO Auto-generated method stub
		return uroleRepo.findAllActiveRoleByLoginId(intValue);
	}

	
	
}
