package cz.fg.tempstatservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity which represents record about temperature in database.
 */
@Entity
@Data
public class Temperature {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Date and time is required")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private Date dateAndTime;

    @NotNull(message = "Temperature is required")
    @Column(scale=2)
    private Float temperature;
}
