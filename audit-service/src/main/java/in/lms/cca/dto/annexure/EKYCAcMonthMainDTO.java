package in.lms.cca.dto.annexure;

import java.util.List;

public class EKYCAcMonthMainDTO {
	
	private List<EKYCAcMonthDetailsDTO> acMonthDetailsDTO;
	private String userName;
	public List<EKYCAcMonthDetailsDTO> getAcMonthDetailsDTO() {
		return acMonthDetailsDTO;
	}
	public void setAcMonthDetailsDTO(List<EKYCAcMonthDetailsDTO> acMonthDetailsDTO) {
		this.acMonthDetailsDTO = acMonthDetailsDTO;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "EKYCAcMonthMainDTO [acMonthDetailsDTO=" + acMonthDetailsDTO + ", userName=" + userName + "]";
	}
	
	

}
