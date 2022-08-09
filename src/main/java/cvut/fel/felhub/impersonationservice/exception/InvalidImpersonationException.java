package cvut.fel.felhub.impersonationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidImpersonationException extends RuntimeException {

    public InvalidImpersonationException() {
    }

    public InvalidImpersonationException(String message) {
        super(message);
    }
}
