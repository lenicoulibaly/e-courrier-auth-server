package lenicorp.archive.model.dtos.validator;

import jakarta.inject.Inject;
import jakarta.servlet.http.Part;
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
@Constraint(validatedBy = {ValidFileExtension.ValidFileExtensionValidator.class,
        ValidFileExtension.ValidFileExtensionValidatorOnDTO.class, ValidFileExtension.ValidFileExtensionValidatorOnBase64Url.class})
@Documented
public @interface ValidFileExtension
{
    String message() default "Type de fichier non pris en charge";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @RequiredArgsConstructor
    class ValidFileExtensionValidator implements ConstraintValidator<ValidFileExtension, Part>
    {
        @Inject
        DocumentValidationRuleRepo documentValidationRuleRepo;

        @Override
        public boolean isValid(Part file, ConstraintValidatorContext context)
        {
            if(file == null) return true;
            if(file.getSubmittedFileName() == null || file.getSubmittedFileName().equals("")) return true;

            // We can't validate the extension without knowing the document type
            // This validator should be used at the class level with ValidFileExtensionValidatorOnDTO
            return true;
        }
    }

    @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnBase64Url implements ConstraintValidator<ValidFileExtension, String>
    {
        @Inject
        DocumentValidationRuleRepo documentValidationRuleRepo;

        @ConfigProperty(name = "document.default.type", defaultValue = "DOC")
        String defaultDocType;

        @Override
        public boolean isValid(String extension, ConstraintValidatorContext context)
        {
            if(extension == null || extension.isEmpty()) return true;

            // Use default document type for validation
            return documentValidationRuleRepo.isExtensionAllowedForType(defaultDocType, extension);
        }
    }

    @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnDTO implements ConstraintValidator<ValidFileExtension, UploadDocReq>
    {
        @Inject
        DocumentValidationRuleRepo documentValidationRuleRepo;

        @Override
        public boolean isValid(UploadDocReq dto, ConstraintValidatorContext context)
        {
            InputStream file = dto.getFile();
            if(file == null) return true;

            String extension = null;
            try
            {
                extension = FileUtils.getExtensionFromInputStream(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new AppException(e.getMessage());
            }
            if(dto.getDocTypeCode() == null) return true;

            return documentValidationRuleRepo.isExtensionAllowedForType(dto.getDocTypeCode(), extension);
        }
    }
}
