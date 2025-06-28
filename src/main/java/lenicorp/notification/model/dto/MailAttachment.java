package lenicorp.notification.model.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class MailAttachment {
    
    private String fileName;
    private byte[] content;
    private String contentType;
    private boolean inline = false;
    private String contentId; // Pour les images inline
}