package cvut.fel.felhub.impersonationservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cvut.fel.felhub.impersonationservice.datamodel.dto.error.ErrorResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.error.ValidationErrorResponseDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.error.ViolationDTO;
import cvut.fel.felhub.impersonationservice.datamodel.dto.usermap.ExceptionDTO;
import cvut.fel.felhub.impersonationservice.exception.EntityAlreadyExistsException;
import cvut.fel.felhub.impersonationservice.exception.ImpersonationForbiddenException;
import cvut.fel.felhub.impersonationservice.exception.InvalidImpersonationException;
import cvut.fel.felhub.impersonationservice.exception.NonExistentEntityException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    ValidationErrorResponseDTO onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationErrorResponseDTO error = new ValidationErrorResponseDTO();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new ViolationDTO(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        error.setHttpStatus("422");
        return error;
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponseDTO handleFeignStatusException(FeignException e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionDTO exceptionDTO = objectMapper.readValue(e.contentUTF8(), ExceptionDTO.class);

        return new ErrorResponseDTO(exceptionDTO.getCode(), exceptionDTO.getTitle());
    }

    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleFeignStatusException(FeignException.NotFound e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionDTO exceptionDTO = objectMapper.readValue(e.contentUTF8(), ExceptionDTO.class);

        return new ErrorResponseDTO(exceptionDTO.getCode(), exceptionDTO.getTitle());
    }

    @ExceptionHandler(ImpersonationForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleImpersonationForbiddenException(ImpersonationForbiddenException e) {
        return new ErrorResponseDTO("403", e.getMessage());
    }

    @ExceptionHandler(NonExistentEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNonExistentEntityException(NonExistentEntityException e) {
        return new ErrorResponseDTO("404", e.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        return new ErrorResponseDTO("409", e.getMessage());
    }

    @ExceptionHandler(InvalidImpersonationException.class)
    //@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    /* todo disabled response status annotation on this handler to remove it from swagger. If there are multiple
    responses for the same response code (422 in this case), swagger displays it as oneOf -> {} construct.
    This is part of swagger standard but current version of openAPI generator does not support it, therefore
    this was making it impossible to generate clients from the specification. OpenAPI gen version 3 should support
    oneOf -> {} construct. @ResponseStatus can be uncommented when openAPI gen v3 is released and we switch to it.

    Meanwhile, we must keep only response for each response code in the swagger.

    See openAPI roadmap: https://openapi-generator.tech/docs/roadmap/#short-term
     */
    public ErrorResponseDTO handleInvalidImpersonationException(InvalidImpersonationException e) {
        return new ErrorResponseDTO("422", e.getMessage());
    }
}
