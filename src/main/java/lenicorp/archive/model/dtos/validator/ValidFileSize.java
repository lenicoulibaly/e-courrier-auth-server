package lenicorp.archive.model.dtos.validator;

import jakarta.inject.Inject;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lenicorp.archive.controller.repositories.DocumentValidationRuleRepo;
import lenicorp.archive.model.dtos.request.UploadDocReq;
import lenicorp.archive.model.utils.FileUtils;
import lenicorp.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileSize.ValidFileSizeValidator.class})
@Documented
public @interface ValidFileSize
{
    String message() default "Fichier trop volumineux";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @RequiredArgsConstructor
    class ValidFileSizeValidator implements ConstraintValidator<ValidFileSize, UploadDocReq>
    {
        @Inject
        DocumentValidationRuleRepo documentValidationRuleRepo;

        @ConfigProperty(name = "document.default.max.size", defaultValue = "5242880") // 5MB default
        Long defaultMaxSize;

        @Override
        public boolean isValid(UploadDocReq dto, ConstraintValidatorContext context)
        {
            InputStream file = dto.getFile();
            if(file == null) return true;

            String docTypeCode = dto.getDocTypeCode();
            if(docTypeCode == null) return true;

            String extension = null;
            try
            {
                extension = FileUtils.getExtensionFromInputStream(file);
            } catch (IOException e)
            {
                e.printStackTrace();
                throw new AppException(e.getMessage());
            }
            if(extension == null || extension.isEmpty()) return true;

            // Get max file size for this document type and extension
            Long maxSize = documentValidationRuleRepo.getMaxFileSizeForTypeAndExtension(docTypeCode, extension);

            // If no specific rule exists, use default max size
            if(maxSize == null) {
                maxSize = defaultMaxSize;
            }
            FileUtils.InputStreamDetails streamDetails = null;
            try
            {
                streamDetails = FileUtils.getInputStreamDetails(file);
                file = streamDetails.getInputStream();
            } catch (IOException e)
            {
                e.printStackTrace();
                throw new AppException(e.getMessage());
            }

            return streamDetails.getSize() <= maxSize;
        }
    }
}
