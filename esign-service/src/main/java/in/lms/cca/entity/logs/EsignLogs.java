package in.lms.cca.entity.logs;


import in.lms.cca.entity.ESignRequest;
import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "eSignLogs", schema="cca_logs_management")
public class EsignLogs extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id", length = 11)
	private Long logId;
	
	@OneToOne
	@JoinColumn(name = "esign_request_id", referencedColumnName = "esign_request_id")
	private ESignRequest eSignRequestId;
	
	@Column(name = "action", length = 50)
	private String action;
	
	@Column(name = "ip_address", length = 74)
	private Integer ipAddress;
	
}
