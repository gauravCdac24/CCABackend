package in.lms.cca.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.repository.AuditReportDocumentRepository;
import in.lms.cca.service.AuditReportDocumentService;
import jakarta.persistence.EntityManager;


@Service
@Transactional
public class AuditReportDocumentServiceImpl implements AuditReportDocumentService {
	
	
	@Autowired
	private AuditReportDocumentRepository auditReportDocumentRepository;

	@Autowired
	private EntityManager entityManager;

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
	public AuditReportDocumentEntity getByAuditId(ApplicationAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return null;
		}
		return getActiveByAppAuditId(applicationAuditEntity.getAppAuditId());
	}

	@Override
	public AuditReportDocumentEntity getActiveByAppAuditId(Long appAuditId) {
		if (appAuditId == null) {
			return null;
		}
		return auditReportDocumentRepository.getActiveByAppAuditId(appAuditId);
	}

	@Override
	public ApplicationAuditEntity resolveAuditReference(ApplicationAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return null;
		}
		return entityManager.getReference(ApplicationAuditEntity.class, applicationAuditEntity.getAppAuditId());
	}

	@Override
	public int linkOrphanDocumentsToApplicant(Long appAuditId, List<Long> criteriaDocIds) {
		if (appAuditId == null || criteriaDocIds == null || criteriaDocIds.isEmpty()) {
			return 0;
		}
		ApplicationAuditEntity auditRef = entityManager.getReference(ApplicationAuditEntity.class, appAuditId);
		List<AuditReportDocumentEntity> orphans = auditReportDocumentRepository
				.findOrphansByCriteriaDocIds(criteriaDocIds);
		int linked = 0;
		for (AuditReportDocumentEntity orphan : orphans) {
			orphan.setAppAuditId(auditRef);
			orphan.setUpdated(new java.util.Date());
			if (auditReportDocumentRepository.save(orphan).getCriteriaDocId() != null) {
				linked++;
			}
		}
		return linked;
	}

	@Override
	public List<AuditReportDocumentEntity> findAllWithNullAppAuditId() {
		return auditReportDocumentRepository.findAllWithNullAppAuditId();
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
