package in.lms.cca.dto;

import in.lms.cca.entity.ReviewESPApplication;

public class PreviousReviewedApplicationDTO {

	PreviewApplicationDTO previousData;
	ReviewESPApplication reviewESPApp;
	
	public PreviewApplicationDTO getPreviousData() {
		return previousData;
	}
	public void setPreviousData(PreviewApplicationDTO previousData) {
		this.previousData = previousData;
	}
	public ReviewESPApplication getReviewESPApp() {
		return reviewESPApp;
	}
	public void setReviewESPApp(ReviewESPApplication reviewESPApp) {
		this.reviewESPApp = reviewESPApp;
	}
	
	
	
}
