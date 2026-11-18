package in.lms.cca.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "asp_audit_report", schema = "dashboard_management")
public class AspAuditReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asp_audit_report_id")
	private Long aspAuditReportId;

	@Column(name = "aspid")
	private Long aspId;

	@Column(name = "file_name", length = 200)
	private String fileName;

	@Column(name = "audit_report_date")
	private Date auditReportDate;

	public Long getAspAuditReportId() {
		return aspAuditReportId;
	}

	public void setAspAuditReportId(Long aspAuditReportId) {
		this.aspAuditReportId = aspAuditReportId;
	}

	public Long getAspId() {
		return aspId;
	}

	public void setAspId(Long aspId) {
		this.aspId = aspId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getAuditReportDate() {
		return auditReportDate;
	}

	public void setAuditReportDate(Date auditReportDate) {
		this.auditReportDate = auditReportDate;
	}
}
