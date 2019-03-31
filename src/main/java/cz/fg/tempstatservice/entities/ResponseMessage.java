package cz.fg.tempstatservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple common response message serializable into Json.
 */

@Data
@AllArgsConstructor
public class ResponseMessage {
    private String message;
}
