package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.AnnexureMain;
import in.lms.cca.repository.annexure.AnnexureMainRepository;
import in.lms.cca.service.annexure.IAnnexureMainService;
import in.lms.cca.util.global.AnnualAuditContext;

@Service
@Transactional
public class AnnexureMainServiceImpl implements IAnnexureMainService{

	@Autowired
	private AnnexureMainRepository repo;
	
	@Override
	public AnnexureMain getAnnexureByStatusAndUsername(String status, String username) {
		Long auditScheduleId = AnnualAuditContext.getAuditScheduleId();
		if (auditScheduleId != null) {
			return repo.findAnnexureByStatusUsernameAndAuditScheduleId(status, username, auditScheduleId);
		}
		return repo.findAnnexureByStatusAndUsername(status, username);
	}

	@Override
	public AnnexureMain getAnnexureForAuditorView(String username) {
		if (username == null || username.isBlank()) {
			return null;
		}
		String normalized = username.replace("\"", "").trim();
		Long auditScheduleId = AnnualAuditContext.getAuditScheduleId();
		if (auditScheduleId != null) {
			AnnexureMain scheduled = repo.findAnnexureByStatusUsernameAndAuditScheduleId("Active", normalized,
					auditScheduleId);
			if (scheduled != null) {
				return scheduled;
			}
		}
		AnnexureMain active = repo.findAnnexureByStatusAndUsername("Active", normalized);
		if (active != null) {
			return active;
		}
		List<AnnexureMain> rows = repo.findByUserNameOrderByIdDesc(normalized);
		return rows.isEmpty() ? null : rows.get(0);
	}

	@Override
	public AnnexureMain addAnnexureMain(AnnexureMain sobj) {
		if (sobj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

		if (sobj.getAuditScheduleId() == null) {
			sobj.setAuditScheduleId(AnnualAuditContext.getAuditScheduleId());
		}

	    try {
	        return repo.save(sobj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save Annexure object", e);
	    }
	}

	@Override
	public List<AnnexureMain> getAllAnnexureMain() {
		return repo.getAllAnnexureMain();
	}

}
