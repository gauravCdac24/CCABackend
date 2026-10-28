package in.lms.cca.payload;

import java.util.List;

public class IndividualStep1Payload {
	    private String indivAppId;
	    private String intentAppId;
	    private String salutation;
	    private String firstName;
	    private String middleName;
	    private String lastName;
	    private List<EmailPayload> emailId;
		private String fathersSalutation;
	    private String fatherFirstName;
	    private String fatherMiddleName;
	    private String fatherLastName;
	    private boolean knownByOtherName;
	    private String otherFirstName;
	    private String otherMiddleName;
	    private String otherLastName;
	    private String dob;
	    private String gender;
	    private String nationality;
	    private String userName;
	    private String appTypeMasterId;
	    private String createdBy;
	    private String updatedBy;
	    private String created;
	    private String updated;

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

	    public String getFathersSalutation() {
	        return fathersSalutation;
	    }

	    public void setFathersSalutation(String fathersSalutation) {
	        this.fathersSalutation = fathersSalutation;
	    }

	    public String getFatherFirstName() {
	        return fatherFirstName;
	    }

	    public void setFatherFirstName(String fatherFirstName) {
	        this.fatherFirstName = fatherFirstName;
	    }

	    public String getFatherMiddleName() {
	        return fatherMiddleName;
	    }

	    public void setFatherMiddleName(String fatherMiddleName) {
	        this.fatherMiddleName = fatherMiddleName;
	    }

	    public String getFatherLastName() {
	        return fatherLastName;
	    }

	    public void setFatherLastName(String fatherLastName) {
	        this.fatherLastName = fatherLastName;
	    }

	    public boolean isKnownByOtherName() {
	        return knownByOtherName;
	    }

	    public void setKnownByOtherName(boolean knownByOtherName) {
	        this.knownByOtherName = knownByOtherName;
	    }

	    public String getOtherFirstName() {
	        return otherFirstName;
	    }

	    public void setOtherFirstName(String otherFirstName) {
	        this.otherFirstName = otherFirstName;
	    }

	    public String getOtherMiddleName() {
	        return otherMiddleName;
	    }

	    public void setOtherMiddleName(String otherMiddleName) {
	        this.otherMiddleName = otherMiddleName;
	    }

	    public String getOtherLastName() {
	        return otherLastName;
	    }

	    public void setOtherLastName(String otherLastName) {
	        this.otherLastName = otherLastName;
	    }

	    public String getDob() {
	        return dob;
	    }

	    public void setDob(String dob) {
	        this.dob = dob;
	    }

	    public String getGender() {
	        return gender;
	    }

	    public void setGender(String gender) {
	        this.gender = gender;
	    }

	    public String getNationality() {
	        return nationality;
	    }

	    public void setNationality(String nationality) {
	        this.nationality = nationality;
	    }
	    

		public List<EmailPayload> getEmailId() {
			return emailId;
		}

		public void setEmailId(List<EmailPayload> emailId) {
			this.emailId = emailId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getAppTypeMasterId() {
			return appTypeMasterId;
		}

		public void setAppTypeMasterId(String appTypeMasterId) {
			this.appTypeMasterId = appTypeMasterId;
		}

		public String getIndivAppId() {
			return indivAppId;
		}

		public void setIndivAppId(String indivAppId) {
			this.indivAppId = indivAppId;
		}

		
		public String getIntentAppId() {
			return intentAppId;
		}

		public void setIntentAppId(String intentAppId) {
			this.intentAppId = intentAppId;
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

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public String getUpdated() {
			return updated;
		}

		public void setUpdated(String updated) {
			this.updated = updated;
		}

		@Override
		public String toString() {
			return "IndividualStep1Payload [indivAppId=" + indivAppId + ", intentAppId=" + intentAppId + ", salutation="
					+ salutation + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
					+ ", emailId=" + emailId + ", fathersSalutation=" + fathersSalutation + ", fatherFirstName="
					+ fatherFirstName + ", fatherMiddleName=" + fatherMiddleName + ", fatherLastName=" + fatherLastName
					+ ", knownByOtherName=" + knownByOtherName + ", otherFirstName=" + otherFirstName
					+ ", otherMiddleName=" + otherMiddleName + ", otherLastName=" + otherLastName + ", dob=" + dob
					+ ", gender=" + gender + ", nationality=" + nationality + ", userName=" + userName
					+ ", appTypeMasterId=" + appTypeMasterId + "]";
		}

		
		

		
		
		

}
