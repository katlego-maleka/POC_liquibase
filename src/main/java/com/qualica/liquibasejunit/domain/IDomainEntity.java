package com.qualica.liquibasejunit.domain;

import java.io.Serializable;

/**
 * Created by katlego on 1/9/14.
 */
public interface IDomainEntity extends Serializable {
    public Long getId();

    public void setId(Long id);
}
