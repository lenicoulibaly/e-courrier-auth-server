package lenicorp.archive.controller.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.archive.model.entities.DocumentValidationRule;
import lenicorp.types.model.entities.Type;

import java.util.List;

@ApplicationScoped
public class DocumentValidationRuleRepo implements PanacheRepository<DocumentValidationRule> {

    /**
     * Find all validation rules for a specific document type
     * @param documentType the document type
     * @return list of validation rules
     */
    public List<DocumentValidationRule> findByDocumentType(Type documentType) {
        return list("documentType", documentType);
    }

    /**
     * Find all validation rules for a specific document type code
     * @param typeCode the document type code
     * @return list of validation rules
     */
    public List<DocumentValidationRule> findByDocumentTypeCode(String typeCode) {
        return list("documentType.uniqueCode", typeCode);
    }

    /**
     * Check if an extension is allowed for a specific document type
     * @param typeCode the document type code
     * @param extension the file extension
     * @return true if the extension is allowed, false otherwise
     */
    public boolean isExtensionAllowedForType(String typeCode, String extension) {
        return count("documentType.uniqueCode = ?1 and lower(allowedExtension) = lower(?2)", 
                typeCode, extension) > 0;
    }

    /**
     * Get the maximum file size allowed for a specific document type and extension
     * @param typeCode the document type code
     * @param extension the file extension
     * @return the maximum file size in bytes, or null if no rule exists
     */
    public Long getMaxFileSizeForTypeAndExtension(String typeCode, String extension) {
        DocumentValidationRule rule = find("documentType.uniqueCode = ?1 and lower(allowedExtension) = lower(?2)", 
                typeCode, extension).firstResult();
        return rule != null ? rule.getMaxFileSize() : null;
    }
}
