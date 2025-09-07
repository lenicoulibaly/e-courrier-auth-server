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
@Constraint(validatedBy = {
        ValidFileExtension.ValidFileExtensionValidatorOnDTO.class, ValidFileExtension.ValidFileExtensionValidatorOnBase64Url.class})
@Documented
public @interface ValidFileExtension
{
    String message() default "Type de fichier non pris en charge";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};



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