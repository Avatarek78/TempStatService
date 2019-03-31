package cz.fg.tempstatservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for case when requested entity is not found according to id in database.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdException extends RuntimeException {

    public IdException(String message) {
        super(message);
    }
}
