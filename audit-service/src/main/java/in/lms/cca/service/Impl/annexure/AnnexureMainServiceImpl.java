package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.AnnexureMain;
import in.lms.cca.repository.annexure.AnnexureMainRepository;
import in.lms.cca.service.annexure.IAnnexureMainService;
import in.lms.cca.util.global.AnnualAuditContext;
import in.lms.cca.util.global.RenewalAnnexureContext;

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
		Long intentAppId = RenewalAnnexureContext.getIntentAppId();
		if (intentAppId != null) {
			return repo.findAnnexureByStatusUsernameAndIntentAppId(status, username, intentAppId);
		}
		AnnexureMain fresh = repo.findAnnexureByStatusAndUsername(status, username);
		if (fresh != null) {
			return fresh;
		}
		return activeRenewalAnnexure(username);
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
		Long intentAppId = RenewalAnnexureContext.getIntentAppId();
		if (intentAppId != null) {
			return repo.findAnnexureByStatusUsernameAndIntentAppId("Active", normalized, intentAppId);
		}
		AnnexureMain active = repo.findAnnexureByStatusAndUsername("Active", normalized);
		if (active != null) {
			return active;
		}
		AnnexureMain renewal = activeRenewalAnnexure(normalized);
		if (renewal != null) {
			return renewal;
		}
		List<AnnexureMain> rows = repo.findFreshByUserNameOrderByIdDesc(normalized);
		return rows.isEmpty() ? null : rows.get(0);
	}

	private AnnexureMain activeRenewalAnnexure(String username) {
		if (username == null || username.isBlank()) {
			return null;
		}
		List<AnnexureMain> renewalRows = repo.findActiveRenewalByUsername(username);
		return renewalRows.isEmpty() ? null : renewalRows.get(0);
	}

	@Override
	public AnnexureMain addAnnexureMain(AnnexureMain sobj) {
		if (sobj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

		if (sobj.getAuditScheduleId() == null) {
			sobj.setAuditScheduleId(AnnualAuditContext.getAuditScheduleId());
		}
		if (sobj.getIntentAppId() == null) {
			sobj.setIntentAppId(RenewalAnnexureContext.getIntentAppId());
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
