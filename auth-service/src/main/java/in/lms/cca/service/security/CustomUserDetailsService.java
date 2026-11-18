package in.lms.cca.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.UserLogin;
import in.lms.cca.entity.UserRoles;
import in.lms.cca.repository.UserLoginRepository;
import in.lms.cca.repository.UserRolesRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserLoginRepository userLoginRepo;
	
	@Autowired
	private UserRolesRepository userRoleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserLogin> userLoginDetails = userLoginRepo.findByUserName(username);
		UserLogin user = userLoginDetails.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
		
		
		
		return new User(user.getUserName(), user.getPassword(),
				getGrantedAuthority(user.getLoginId()));
	}
	
	
	private Collection<GrantedAuthority> getGrantedAuthority(Integer loginId) {
		
		List<UserRoles> uroles = userRoleService.findByLoginId(loginId);
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		for(UserRoles ur: uroles) {
			authorities.add(new SimpleGrantedAuthority(ur.getRoleName()));
		}
		
		return authorities;
	}

}