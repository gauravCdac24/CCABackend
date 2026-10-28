package in.lms.cca.payload;

import java.util.List;

public class AuthenticationResponse {

	private final String jwt;
	private List<String> roles;
	private String userName;
	private String status;
	private String refreshToken;
	private String name;
	
	
	public AuthenticationResponse(String jwt, String refreshToken, List<String> roles, String userName, String status, String name) {
		super();
		this.jwt = jwt;
		this.roles = roles;
		this.userName = userName;
		this.status = status;
		this.refreshToken = refreshToken;
		this.name = name;
	}

	
	public AuthenticationResponse(String jwt, List<String> roles, String userName, String status, String name) {
		super();
		this.jwt = jwt;
		this.roles = roles;
		this.userName = userName;
		this.status = status;
		this.name = name;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getJwt() {
		return jwt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	

}
