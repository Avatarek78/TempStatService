package cz.fg.tempstatservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Object for serialization of message in ID exception
 */
@Data
@AllArgsConstructor
public class IdExceptionResponse {

    private String message;

}
