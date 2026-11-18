package in.lms.cca.entity;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ekycmode_review", schema="esign_application_management")
public class EkycModeReview extends AbstractTimestampEntity{

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ekyc_mode_review", length = 11)
    private Long ekycModeReview;
	
	@ManyToOne
	@JoinColumn(name = "ekyc_mode_id", referencedColumnName = "ekyc_mode_id")
	private EkycMode ekycModeId;
	
	@ManyToOne
	@JoinColumn(name = "esp_review_id", referencedColumnName = "esp_review_id")
	private ReviewESPApplication espReviewId;

	public Long getEkycModeReview() {
		return ekycModeReview;
	}

	public void setEkycModeReview(Long ekycModeReview) {
		this.ekycModeReview = ekycModeReview;
	}

	public EkycMode getEkycModeId() {
		return ekycModeId;
	}

	public void setEkycModeId(EkycMode ekycModeId) {
		this.ekycModeId = ekycModeId;
	}

	public ReviewESPApplication getEspReviewId() {
		return espReviewId;
	}

	public void setEspReviewId(ReviewESPApplication espReviewId) {
		this.espReviewId = espReviewId;
	}


	
	
	
		
	
	
}
