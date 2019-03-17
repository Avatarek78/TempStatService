package cz.fg.tempstatservice.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for input data validation through @Valid annotation before parameters (entities) in controllers methods.
 * Each specific validation is written under each field of validated entity by "validation-api" annotations.
 */
@Service
public class MapValidationErrorService {

    /**
     * Returns map of errors.
     * @param result BindingResult for request.
     * @return ResponseEntity with errors or null if no errors found while data validation.
     */
    public ResponseEntity<?> getErrorsMap(BindingResult result){
        if(result.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError error: result.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
