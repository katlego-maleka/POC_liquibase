package com.qualica.liquibasejunit.data;

import com.qualica.liquibasejunit.domain.Party;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by katlego on 1/7/14.
 *
 * See https://spring.io/guides/gs/accessing-data-jpa/
 */
@Repository
@Transactional
public class PartyRepository extends BaseRepository<Party> {
    private Logger LOG = LoggerFactory.getLogger(PartyRepository.class);

    @PersistenceContext(name="main.pu")
    private EntityManager em;


    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
        return em;
    }

    @Override
    protected Class<Party> getEntityClass() {
        return Party.class;  //To change body of implemented methods use File | Settings | File Templates.
    }
    List<Party> findByModifiedBy(String modifiedByName) {
        Query namedQuery = getEntityManager().createNamedQuery("person.findByModifiedBy");
        namedQuery.setParameter("modifiedBy", modifiedByName);
        return namedQuery.getResultList();
    }
}
