package in.lms.cca.dto;

public class FirmAuthorizedRepresentativeDTO {
	
	    private String authorizedRepresentativeId;
	    private String blockNo;
	    private String village;
	    private String postOffice;
	    private String subDivision;
	    private String country;
	    private String city;
	    private String state;
	    private String pin;
	    private String fax;
	    private String telephoneNo;
	    private String firstName;
	    private String lastName;
	    private String middleName;
	    private String salutation;
	    private String naturesOfBusiness;
	    private String userName;
        private String addressId;
        
        
	    public String getAddressId() {
			return addressId;
		}

		public void setAddressId(String addressId) {
			this.addressId = addressId;
		}

		public String getAuthorizedRepresentativeId() {
			return authorizedRepresentativeId;
		}

		public void setAuthorizedRepresentativeId(String authorizedRepresentativeId) {
			this.authorizedRepresentativeId = authorizedRepresentativeId;
		}

		public String getBlockNo() {
	        return blockNo;
	    }

	    public void setBlockNo(String blockNo) {
	        this.blockNo = blockNo;
	    }

	    public String getVillage() {
	        return village;
	    }

	    public void setVillage(String village) {
	        this.village = village;
	    }

	    public String getPostOffice() {
	        return postOffice;
	    }

	    public void setPostOffice(String postOffice) {
	        this.postOffice = postOffice;
	    }

	    public String getSubDivision() {
	        return subDivision;
	    }

	    public void setSubDivision(String subDivision) {
	        this.subDivision = subDivision;
	    }

	    public String getCountry() {
	        return country;
	    }

	    public void setCountry(String country) {
	        this.country = country;
	    }

	    public String getCity() {
	        return city;
	    }

	    public void setCity(String city) {
	        this.city = city;
	    }

	    public String getState() {
	        return state;
	    }

	    public void setState(String state) {
	        this.state = state;
	    }

	    public String getPin() {
	        return pin;
	    }

	    public void setPin(String pin) {
	        this.pin = pin;
	    }

	    public String getFax() {
	        return fax;
	    }

	    public void setFax(String fax) {
	        this.fax = fax;
	    }

	    public String getTelephoneNo() {
	        return telephoneNo;
	    }

	    public void setTelephoneNo(String telephoneNo) {
	        this.telephoneNo = telephoneNo;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getMiddleName() {
	        return middleName;
	    }

	    public void setMiddleName(String middleName) {
	        this.middleName = middleName;
	    }

	    public String getSalutation() {
	        return salutation;
	    }

	    public void setSalutation(String salutation) {
	        this.salutation = salutation;
	    }

	    public String getNaturesOfBusiness() {
	        return naturesOfBusiness;
	    }

	    public void setNaturesOfBusiness(String naturesOfBusiness) {
	        this.naturesOfBusiness = naturesOfBusiness;
	    }

	    public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

		@Override
		public String toString() {
			return "FirmAuthorizedRepresentativeDTO [authorizedRepresentativeId=" + authorizedRepresentativeId
					+ ", blockNo=" + blockNo + ", village=" + village + ", postOffice=" + postOffice + ", subDivision="
					+ subDivision + ", country=" + country + ", city=" + city + ", state=" + state + ", pin=" + pin
					+ ", fax=" + fax + ", telephoneNo=" + telephoneNo + ", firstName=" + firstName + ", lastName="
					+ lastName + ", middleName=" + middleName + ", salutation=" + salutation + ", naturesOfBusiness="
					+ naturesOfBusiness + ", userName=" + userName + ", addressId=" + addressId + "]";
		}

		
	}