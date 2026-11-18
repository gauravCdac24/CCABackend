package in.lms.cca.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AuditNCSEntity;
import in.lms.cca.repository.AuditNCSRepository;
import in.lms.cca.service.AuditNCSService;

@Service
@Transactional
public class AuditNCSServiceImpl implements AuditNCSService {
	
	@Autowired
	private AuditNCSRepository auditNCSRepository;

	@Override
	public Optional<AuditNCSEntity> saveData(AuditNCSEntity auditNCSEntity) {
		 if (auditNCSEntity == null) {
	            return Optional.empty();
	        }

	        try {
	        	AuditNCSEntity savedAuditNCSEntity = auditNCSRepository.save(auditNCSEntity);
	            return Optional.of(savedAuditNCSEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public AuditNCSEntity findByAuditId(Long appAuditId) {
		// TODO Auto-generated method stub
		return auditNCSRepository.findByAuditId(appAuditId);
	}

	@Override
	public Optional<AuditNCSEntity> downloadfileBydDocumentName(String decrypt) {
		// TODO Auto-generated method stub
		return auditNCSRepository.downloadfileBydDocumentName(decrypt);
	}

}
