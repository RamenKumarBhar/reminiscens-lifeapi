package models;
import java.util.List;

import javax.persistence.EntityManager;

import static org.fest.assertions.Assertions.*;
import play.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.LifeStory;

import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.db.jpa.Transactional;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;



/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class LifeStoryTest  {
	private EntityManager em;
	
	@Before
	public void setUp() {
		FakeApplication app = Helpers.fakeApplication();
		Helpers.start(app);
		Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
		em = jpaPlugin.get().em("default");
	    JPA.bindForCurrentThread(em);
	}
	
    @Test 
    @Transactional
    public void getDecadesTest() {
//    	 running(fakeApplication(), new Runnable() {
//    		    @Transactional(readOnly=true)
//    		    public void run() {
    		        Long personId = new Long(3);
    		        
    		        List<Long> decades = LifeStory.getDecadesByPerson(personId);
    		        Logger.debug("decades: "+decades.toString());
//    		        List<LifeStory> s = LifeStory.readByPerson(personId);
//    		        List<Long> decades = LifeStory.getDecades(s);
    		        assertThat(decades).isNotEmpty();
    		        assertThat(decades).contains(new Long(1970));
    		    }
//    		  });
//    }
    
    @After
    public void tearDown() {
        JPA.bindForCurrentThread(null);
        em.close();
    }
}
