package in.lms.cca.service;

import java.util.Optional;

import in.lms.cca.entity.LdifDocument;

public interface ILdifDocumentService {

	Optional<LdifDocument> addLdifDocument(LdifDocument obj);

}
