package cz.fg.tempstatservice.repositories;

import cz.fg.tempstatservice.entities.Temperature;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Class for Temperature entity persistence.
 */
@Repository
public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

}
