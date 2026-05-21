package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AuditControlEntity;
import in.lms.cca.repository.AuditRepository;
import in.lms.cca.service.AuditService;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {
	
	@Autowired
	private AuditRepository auditRepository;

	@Override
	public Optional<AuditControlEntity> save(AuditControlEntity control) {
		 if (control == null) {
	            return Optional.empty();
	        }

	        try {
	        	AuditControlEntity savedAuditControlEntity = auditRepository.save(control);
	            return Optional.of(savedAuditControlEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<AuditControlEntity> getAuditControlsByUser(String userName) {
		// TODO Auto-generated method stub
		return auditRepository.getAuditControlsByUser(userName);
	}

	@Override
	public void deleteByControlId(int idToRemove) {
		// TODO Auto-generated method stub
		 auditRepository.deleteByControlId(idToRemove);
	}
	

}
