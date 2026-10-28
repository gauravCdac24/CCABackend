package in.lms.cca.entity.annexure;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "court_cases_main", schema = "audit_management")
public class CourtCasesMain extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_cases_main_id", length = 11)
    private Long courtCasesMainId;

    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;

    @Column(name = "active_court_cases", length = 200)
    private String activeCourtCases;

    @Column(name = "court_cases_count", length = 200)
    private String courtCasesCount;

    @Column(name = "police_complaints_count", length = 200)
    private String policeComplaintsCount;
    
    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
 

	public Long getCourtCasesMainId() {
		return courtCasesMainId;
	}

	public void setCourtCasesMainId(Long courtCasesMainId) {
		this.courtCasesMainId = courtCasesMainId;
	}

	public String getAuditorVerification() {
		return auditorVerification;
	}

	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}

	public String getActiveCourtCases() {
		return activeCourtCases;
	}

	public void setActiveCourtCases(String activeCourtCases) {
		this.activeCourtCases = activeCourtCases;
	}

	public String getCourtCasesCount() {
		return courtCasesCount;
	}

	public void setCourtCasesCount(String courtCasesCount) {
		this.courtCasesCount = courtCasesCount;
	}

	public String getPoliceComplaintsCount() {
		return policeComplaintsCount;
	}

	public void setPoliceComplaintsCount(String policeComplaintsCount) {
		this.policeComplaintsCount = policeComplaintsCount;
	}

	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}


    
    
    
}