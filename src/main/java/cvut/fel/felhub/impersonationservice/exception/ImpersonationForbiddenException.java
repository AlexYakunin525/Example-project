package cvut.fel.felhub.impersonationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ImpersonationForbiddenException extends RuntimeException {
    public ImpersonationForbiddenException(String message) {
        super(message);
    }
}
