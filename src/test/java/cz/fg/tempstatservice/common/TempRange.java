package cz.fg.tempstatservice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Temperature range for testing purposes.
 */
@AllArgsConstructor
@Getter
public class TempRange {
    private Float low;
    private Float high;
}
