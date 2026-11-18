package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.repository.AuditReportDocumentRepository;
import in.lms.cca.service.AuditReportDocumentService;


@Service
@Transactional
public class AuditReportDocumentServiceImpl implements AuditReportDocumentService {
	
	
	@Autowired
	private AuditReportDocumentRepository auditReportDocumentRepository;

	@Override
	public Optional<AuditReportDocumentEntity> saveData(AuditReportDocumentEntity auditReportDocumentEntity) {
		if(auditReportDocumentEntity==null)
			  return Optional.empty();
			 try {
				 AuditReportDocumentEntity savedAuditReportDocumentEntity = auditReportDocumentRepository.save(auditReportDocumentEntity);
		            return Optional.of(savedAuditReportDocumentEntity);
		        } catch (Exception e) {
		            return Optional.empty();
		        }
	}

	@Override
	public AuditReportDocumentEntity getByAuditId(LicenseeAuditEntity applicationAuditEntity) {
		// TODO Auto-generated method stub
		return auditReportDocumentRepository.getByAuditId(applicationAuditEntity);
	}

	@Override
	public Optional<AuditReportDocumentEntity> updateData(AuditReportDocumentEntity documentEntity) {
		if(documentEntity==null)
			  return Optional.empty();
		
		if(documentEntity.getCriteriaDocId()==null)
			  return Optional.empty();
			 try {
				 AuditReportDocumentEntity savedAuditReportDocumentEntity = auditReportDocumentRepository.save(documentEntity);
		            return Optional.of(savedAuditReportDocumentEntity);
		        } catch (Exception e) {
		            return Optional.empty();
		        }
	}

	@Override
	public List<AuditReportDocumentEntity> findByAuditId(Long appAuditId) {
		// TODO Auto-generated method stub
		return auditReportDocumentRepository.findByAuditId(appAuditId);
	}

	@Override
	public AuditReportDocumentEntity findByAuditsId(Long appAuditId) {
		// TODO Auto-generated method stub
		return auditReportDocumentRepository.findByAuditsId(appAuditId);
	}

}
