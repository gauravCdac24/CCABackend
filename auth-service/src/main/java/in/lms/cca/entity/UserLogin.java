package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_login", schema="auth_users_management")
public class UserLogin extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_id", length = 8)
	private Integer loginId;
	
	@Column(name = "user_id",  length = 74)
	private String userId;
	
	@Column(name = "password", length = 74)
	private String password;
	
	@Column(name = "previous_password", length = 74)
	private String previousPassword;
	
	@Column(name = "username", length = 50, unique=true)
	private String userName;
	
	@Column(name = "email_id", length = 50, unique=true)
	private String emailId;
	
	@Column(name = "mobile", length = 10, unique=true)
	private String mobile;
	
	@Column(name = "salutation", length = 6)
	private String salutation;

	@Column(name = "first_name", length = 100)
	private String firstName;

	@Column(name = "middle_name", length = 30)
	private String middleName;

	@Column(name = "last_name", length = 45)
	private String lastName;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;
	
	
	
	

	public UserLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserLogin(String userName, String emailId, String mobile, String salutation, String firstName,
			String middleName, String lastName) {
		super();
		this.userName = userName;
		this.emailId = emailId;
		this.mobile = mobile;
		this.salutation = salutation;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPreviousPassword() {
		return previousPassword;
	}

	public void setPreviousPassword(String previousPassword) {
		this.previousPassword = previousPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	


	

}
