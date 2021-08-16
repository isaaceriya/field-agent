package learn.field_agent.data;

import learn.field_agent.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasJdbcTemplateRepositoryTest {


    final static int NEXT_ALIAS_ID = 4;

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    AgentJdbcTemplateRepository agentJdbcTemplateRepository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        Alias actual = repository.add(alias);
        assertNotNull(actual);
        assertEquals(NEXT_ALIAS_ID, actual.getAliasId());
    }


   /* @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        assertTrue(repository.add(alias));
    }*/

    @Test
    void shouldUpdate() {
        Alias alias = makeAlias();
        alias.setAliasId(2);
        assertTrue(repository.update(alias));
        alias.setAliasId(16);
        assertFalse(repository.update(alias));
    }

    @Test
    void shouldFindById() {
        Alias alias = repository.findById(2);
        assertEquals(2, alias.getAliasId());
        assertEquals("AliasTwo", alias.getName());
        assertEquals("PersonaTwo", alias.getPersona());

//        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");
    }



    @Test
    void deleteById() {
        assertTrue(repository.deleteById(1));
    }

    private Alias makeAlias(){
        Alias alias = new Alias();
        alias.setAliasId(1);
        alias.setName("John");
        alias.setPersona("Persona");
        alias.setAgentId(4);

        return alias;
    }

    private Agent makeAgent() {
        Agent agent = new Agent();
        agent.setFirstName("Test");
        agent.setLastName("Last Name");
        agent.setDob(LocalDate.of(1985, 8, 15));
        agent.setHeightInInches(66);
        return agent;
    }
}
