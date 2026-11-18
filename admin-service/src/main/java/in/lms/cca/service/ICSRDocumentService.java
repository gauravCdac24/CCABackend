package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.CSRDocument;

public interface ICSRDocumentService {

	Optional<CSRDocument> addCSRDocument(CSRDocument csrDocumentObj);

	Optional<CSRDocument> updateCSRDocument(CSRDocument csrDocumentObj);

	List<CSRDocument> getAllCSRDocument();

	List<CSRDocument> getAllActiveCSRDocument();

	List<CSRDocument> getAllInactiveCSRDocument();

	CSRDocument getByCSRDocId(Long csrDocId);

	boolean deleteBycsrDocId(Long csrDocId);

	CSRDocument getCSRDocById(long csrDocuId);

	Optional<CSRDocument> downloadfile(long csrDocuId);
}
