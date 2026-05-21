package in.lms.cca.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class AuditAgencyFormDto {
    private String auditAgencyName;
    private String applicantUserName;
    private String undertakingFileName;
    private String remark;
    private String intentAppId;

    private MultipartFile undertakingFile;

    private List<AuditorDescription> auditorDesscription;

    private List<AuditScope> auditScope;


	
    
  

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUndertakingFileName() {
		return undertakingFileName;
	}

	public void setUndertakingFileName(String undertakingFileName) {
		this.undertakingFileName = undertakingFileName;
	}

	public String getApplicantUserName() {
		return applicantUserName;
	}

	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}

	public String getAuditAgencyName() {
		return auditAgencyName;
	}

	public void setAuditAgencyName(String auditAgencyName) {
		this.auditAgencyName = auditAgencyName;
	}

	public String getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}

	public MultipartFile getUndertakingFile() {
		return undertakingFile;
	}

	public void setUndertakingFile(MultipartFile undertakingFile) {
		this.undertakingFile = undertakingFile;
	}

	public List<AuditorDescription> getAuditorDesscription() {
		return auditorDesscription;
	}

	public void setAuditorDesscription(List<AuditorDescription> auditorDesscription) {
		this.auditorDesscription = auditorDesscription;
	}

	public List<AuditScope> getAuditScope() {
		return auditScope;
	}

	public void setAuditScope(List<AuditScope> auditScope) {
		this.auditScope = auditScope;
	}

	public static class AuditorDescription {
		private String auditorDescriptionId;
        private String auditorName;
        private String certificateType;
        private String certificateName;
        private String certificateExpiry;
        private MultipartFile file;
      
        
        
		public String getAuditorDescriptionId() {
			return auditorDescriptionId;
		}
		public void setAuditorDescriptionId(String auditorDescriptionId) {
			this.auditorDescriptionId = auditorDescriptionId;
		}
		public String getAuditorName() {
			return auditorName;
		}
		public void setAuditorName(String auditorName) {
			this.auditorName = auditorName;
		}
		public String getCertificateType() {
			return certificateType;
		}
		public void setCertificateType(String certificateType) {
			this.certificateType = certificateType;
		}
		public MultipartFile getFile() {
			return file;
		}
		public void setFile(MultipartFile file) {
			this.file = file;
		}
		public String getCertificateName() {
			return certificateName;
		}
		public void setCertificateName(String certificateName) {
			this.certificateName = certificateName;
		}
		public String getCertificateExpiry() {
			return certificateExpiry;
		}
		public void setCertificateExpiry(String certificateExpiry) {
			this.certificateExpiry = certificateExpiry;
		}
		
        
        
    }

    public static class AuditScope {
    	private String auditScopeId;
        private String auditTitle;
        private String description;
        private String startDate;
        private String endDate;
        
        
        
		public String getAuditScopeId() {
			return auditScopeId;
		}
		public void setAuditScopeId(String auditScopeId) {
			this.auditScopeId = auditScopeId;
		}
		public String getAuditTitle() {
			return auditTitle;
		}
		public void setAuditTitle(String auditTitle) {
			this.auditTitle = auditTitle;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
        
    }
}