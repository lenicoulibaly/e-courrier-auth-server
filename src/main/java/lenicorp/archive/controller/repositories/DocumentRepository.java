package lenicorp.archive.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.archive.model.dtos.response.ReadDocDTO;
import lenicorp.archive.model.entities.Document;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;

import java.util.List;

@ApplicationScoped
public class DocumentRepository implements PanacheRepository<Document>
{
    /**
     * Get the document path by document ID
     * @param docId the document ID
     * @return the document path
     */
    public String getDocumentPath(Long docId) {
        Document doc = findById(docId);
        return doc != null ? doc.getDocPath() : null;
    }

    /**
     * Get all documents for an object (user, association, or section)
     * @param userId the user ID
     * @param assoId the association ID
     * @param sectionId the section ID
     * @param key the search key
     * @param pageRequest the page request
     * @return a page of documents
     */
    public Page<ReadDocDTO> getAllDocsForObject(Long userId, Long assoId, Long sectionId, String key, PageRequest pageRequest) {
        // This is a placeholder implementation
        // In a real implementation, you would query the database for documents
        return new Page<>(List.of(), 0L, pageRequest.getPage(), pageRequest.getSize());
    }

    /**
     * Get all documents for a versement
     * @param versementId the versement ID
     * @return a list of documents
     */
    public List<ReadDocDTO> getDocsByVersementId(Long versementId) {
        // This is a placeholder implementation
        // In a real implementation, you would query the database for documents
        return List.of();
    }

    /**
     * Get the logo for an association
     * @param assoId the association ID
     * @return the logo document
     */
    public ReadDocDTO getAssoLogo(Long assoId) {
        // This is a placeholder implementation
        // In a real implementation, you would query the database for the logo
        return null;
    }
}
