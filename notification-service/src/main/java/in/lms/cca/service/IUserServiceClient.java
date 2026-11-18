package in.lms.cca.service;

import in.lms.cca.payload.UserResponse;

public interface IUserServiceClient {

	UserResponse getUserDetailsByUsername(String username);
	
}
