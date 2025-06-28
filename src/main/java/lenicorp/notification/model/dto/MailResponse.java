package lenicorp.notification.model.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class MailResponse
{
    private boolean success;
    private String messageId;
    private String errorMessage;
    private LocalDateTime sentAt;
}