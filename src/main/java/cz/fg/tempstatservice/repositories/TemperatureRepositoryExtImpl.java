package cz.fg.tempstatservice.repositories;

import cz.fg.tempstatservice.entities.Temperature;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Implementation of TemperatureRepositoryExt interface.
 */
@Repository
public class TemperatureRepositoryExtImpl implements TemperatureRepositoryExt {

    @PersistenceContext
    private EntityManager em;

    public Iterable<Temperature> findByTempRange(Float lowTemp, Float highTemp) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Temperature> query = cb.createQuery(Temperature.class);
        Root temp = query.from(Temperature.class);
        query.select(temp)
                .where(cb.and(
                        cb.greaterThanOrEqualTo(temp.get("tempValue"), lowTemp),
                        cb.lessThanOrEqualTo(temp.get("tempValue"), highTemp)))
                .orderBy(cb.asc(temp.get("tempValue")));
        return em.createQuery(query).getResultList();
    }
}
