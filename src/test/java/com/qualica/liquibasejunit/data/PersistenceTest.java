package com.qualica.liquibasejunit.data;

import com.qualica.liquibasejunit.domain.Party;
import com.qualica.liquibasejunit.springconfig.MainConfig;
import com.qualica.liquibasejunit.springconfig.profiles.ApplicationProfiles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by katlego on 1/7/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MainConfig.class }, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles( profiles = {ApplicationProfiles.UNIT_TEST} )
public class PersistenceTest {
    @Autowired
    private PartyRepository partyRepository;

    @Test
    public void testSaveCustomers()
    {
        // save a couple of people
        partyRepository.save(new Party(Calendar.getInstance().getTime(), "Jack", "1.0"));
        partyRepository.save(new Party(Calendar.getInstance().getTime(), "Chloe", "1.0"));
        partyRepository.save(new Party(Calendar.getInstance().getTime(), "Kim", "1.0"));
        partyRepository.save(new Party(Calendar.getInstance().getTime(), "David", "1.0"));
        partyRepository.save(new Party(Calendar.getInstance().getTime(), "Michelle", "1.0"));

        // fetch all people
        List<Party> people = partyRepository.findAll();
        assertEquals( "We should have 5 records in the database already.", 5, people.size() );
        Iterable<Party> iterable = people;
        System.out.println("People found with findAll():");
        System.out.println("-------------------------------");
        for (Party party : people) {
            System.out.println(party);
        }
        System.out.println();

        // fetch an individual customer by ID
        Party party = partyRepository.findOne(1L);
        assertNotNull("Where's Jack Bauer?", party);
        System.out.println("Party found with findOne(1L):");
        System.out.println("--------------------------------");
        System.out.println(party);
        System.out.println();

        // fetch people by last name
        people = partyRepository.findByModifiedBy("Kim");
        assertEquals( "Where is Kim?", 1, people.size() );
        System.out.println("Party found with findByModifiedBy('Kim'):");
        System.out.println("--------------------------------------------");
        for (Party bauer : people) {
            System.out.println(bauer);
        }
    }


}
