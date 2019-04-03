package cz.fg.tempstatservice.repositories;

import cz.fg.tempstatservice.entities.Temperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Interface for {@Link Temperature} entity persistence.
 */
@Repository
public interface TemperatureRepository extends PagingAndSortingRepository<Temperature, Long>, TemperatureRepositoryExt {

    /**
     * Getting value of start and end date of the longest period when temperatures didn't fall below the value lowTemp
     * and at the same time temperatures didn't rise above the value highTemp.
     * @param lowTemp {@link Float}
     * @param highTemp {@link Float}
     * @return Collection of {@link Object} arrays, where each array containing this data for each period:
     * <HTML>
     * <BR>
     * [0] - {@link java.sql.Timestamp} Start date and time.<BR>
     * [1] - {@link java.sql.Timestamp} End date and time.<BR>
     * [2] - {@link Float} Value of minimum temperature.<BR>
     * [3] - {@link Float} Value of maximum temperature.<BR>
     * [4] - {@link java.math.BigInteger} Count of measurements.<BR>
     * </HTML>
     */
    @Query(value = "SELECT MIN(date_and_time) start" +
            " , MAX(date_and_time) end" +
            " , MIN(temp_value) minTemp" +
            " , MAX(temp_value) maxTemp" +
            " , COUNT(id) countOfMeasurements" +
            " FROM " +
            "   ( SELECT *" +
            "      , CASE WHEN @prev_temp_val >= :lowTemp AND @prev_temp_val <= :highTemp THEN @range\\:=@range ELSE @range\\:=@range+1 END grp" +
            "      , @prev_temp_val\\:=temp_value prev_temp_val" +
            "      FROM temperature" +
            "      , (SELECT @prev_temp_val\\:=null,@range\\:=0) vars" +
            "      ORDER BY date_and_time" +
            "   ) all_ranges" +
            " WHERE temp_value >= :lowTemp AND temp_value <= :highTemp" +
            " GROUP BY grp HAVING COUNT(id) > 1;",
            nativeQuery = true)
    Iterable<Object[]> findLongestPeriodByTempRange(@Param("lowTemp") Float lowTemp, @Param("highTemp") Float highTemp);

    /**
     * Getting value of start and end date of the longest period when temperatures didn't fall below the value lowTemp
     * and at the same time temperatures didn't rise above the value highTemp. But with extra condition when hours
     * of measuring must be between the entered values hourFrom and hourTo for each day.
     * @param lowTemp {@link Float}
     * @param highTemp {@link Float}
     * @param hourFrom {@link Integer}
     * @param hourTo {@link Integer}
     * @return Collection of {@link Object} arrays, where each array containing this data for each period:
     * <HTML>
     * <BR>
     * [0] - {@link java.sql.Timestamp} Start date and time.<BR>
     * [1] - {@link java.sql.Timestamp} End date and time.<BR>
     * [2] - {@link Float} Value of minimum temperature.<BR>
     * [3] - {@link Float} Value of maximum temperature.<BR>
     * [4] - {@link java.math.BigInteger} Count of measurements.<BR>
     * </HTML>
     */
    @Query(value = "SELECT MIN(date_and_time) start" +
            " , MAX(date_and_time) end" +
            " , MIN(temp_value) minTemp" +
            " , MAX(temp_value) maxTemp" +
            " , COUNT(id) countOfMeasurements" +
            " FROM " +
            "   ( SELECT *" +
            "      , CASE WHEN @prev_temp_val >= :lowTemp AND @prev_temp_val <= :highTemp THEN @range\\:=@range ELSE @range\\:=@range+1 END grp" +
            "      , @prev_temp_val\\:=temp_value prev_temp_val" +
            "      FROM temperature" +
            "      , (SELECT @prev_temp_val\\:=null,@range\\:=0) vars" +
            "      ORDER BY date_and_time" +
            "   ) all_ranges" +
            " WHERE temp_value >= :lowTemp AND temp_value <= :highTemp AND HOUR(date_and_time) >= :hourFrom AND HOUR(date_and_time) < :hourTo" +
            " GROUP BY grp HAVING COUNT(id) > 1",
            nativeQuery = true)
    Iterable<Object[]> findLongestPeriodByTempRangeAndDayPeriod(@Param("lowTemp") Float lowTemp,
                                                                @Param("highTemp") Float highTemp,
                                                                @Param("hourFrom") Integer hourFrom,
                                                                @Param("hourTo") Integer hourTo);

    /**
     * Find {@link Temperature} records by date and time range
     * @param dateFrom
     * @param dateTo
     * @return Collection of {@link Temperature}
     */
    @Query("from Temperature t where t.dateAndTime >= :dateFrom and t.dateAndTime <= :dateTo order by t.dateAndTime asc")
    Iterable<Temperature> findByDateAndTime(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

}
