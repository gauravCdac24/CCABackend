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
@Table(name = "connectivity_details", schema="audit_management")
public class ConnectivityDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connectivity_details_id", length = 11)
    private Long connectivityDetailsId;
    
    @ManyToOne
    @JoinColumn(name = "connectivity_main_id", referencedColumnName = "connectivity_main_id")
    private ConnectivityMain connectivityMainId;
    
    @Column(name = "name", length = 250)
    private String name;
    
    @Column(name = "description", length = 250)
    private String description;

	public Long getConnectivityDetailsId() {
		return connectivityDetailsId;
	}

	public void setConnectivityDetailsId(Long connectivityDetailsId) {
		this.connectivityDetailsId = connectivityDetailsId;
	}

	public ConnectivityMain getConnectivityMainId() {
		return connectivityMainId;
	}

	public void setConnectivityMainId(ConnectivityMain connectivityMainId) {
		this.connectivityMainId = connectivityMainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    



    
    
}
