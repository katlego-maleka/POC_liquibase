package com.qualica.liquibasejunit.data;

import com.qualica.liquibasejunit.domain.IDomainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by katlego on 1/9/14.
 */
public abstract class BaseRepository<I extends IDomainEntity> {
    private Logger LOG = LoggerFactory.getLogger(BaseRepository.class);
    protected EntityManagerFactory emf;

    /**
     * The configuration database entity manager.
     */
    public abstract EntityManager getEntityManager();

    @Transactional(propagation = Propagation.REQUIRED)
    public I save(I entity) {
        if (entity.getId() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} has a null id. To persist ...", entity.getClass().getName());
            }
            getEntityManager().persist(entity);
        } else {
            if (getEntityManager().contains(entity)) {
                getEntityManager().merge(entity);
            } else {
                throw new IllegalArgumentException("There's no " + getEntityClass().getName() + " record with ID: " + entity.getId());
            }
        }
        return entity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        I found = findOne(id);
        getEntityManager().remove(found);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public I findOne(Long id) {
        I found = getEntityManager().find(getEntityClass(), id);
        if (found == null) {
            throw new IllegalArgumentException("There's no " + getEntityClass().getName() + " record with ID: " + id);
        }
        return found;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<I> findAll() {
        String query = "SELECT e FROM " + getEntityClass().getName() + " e";

        if (LOG.isDebugEnabled()) {
            LOG.debug("To create query: {}", query);
        }
        return getEntityManager().createQuery(query).getResultList();
    }

    /**
     * @return The class that this repository performs persistence for
     */
    protected abstract Class<I> getEntityClass();
}
