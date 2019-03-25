package cz.fg.tempstatservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.fg.tempstatservice.utils.TimeUtils;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Entity which represents record about tempValue in database.
 */
@Entity
@Data
public class Temperature {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date_and_time")
    @NotNull(message = "Date and time is required")
    @JsonFormat(pattern = TimeUtils.DATE_FORMAT)
    private Date dateAndTime;

    @NotNull(message = "Temperature is required")
    @Column(scale=2, name = "temp_value")
    private Float tempValue;
}
