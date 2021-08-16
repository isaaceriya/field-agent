package learn.field_agent.data;

import learn.field_agent.models.Agency;
import learn.field_agent.models.Location;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    final static int NEXT_SECURITY_CLEARANCE_ID = 4;

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<SecurityClearance> securityClearances = repository.findAll();
        assertNotNull(securityClearances);
        assertTrue(securityClearances.size() > 0);
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(3, "Test");
//        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(3);
        assertEquals(secret, actual);

//        actual = repository.findById(2);
//        assertEquals(topSecret, actual);

        actual = repository.findById(3);
        assertEquals(secret, actual);
    }

    @Test
    void shouldUpdate() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(2);
        assertTrue(repository.update(securityClearance));
        securityClearance.setSecurityClearanceId(16);
        assertFalse(repository.update(securityClearance));
    }

    @Test
    void shouldUpdateExisting() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setSecurityClearanceId(3);
        securityClearance.setName("Test");

        assertTrue(repository.update(securityClearance));
        assertEquals(securityClearance, repository.findById(3));
    }

    @Test
    void shouldAdd() {
        SecurityClearance securityClearance = makeSecurityClearance();
        SecurityClearance actual = repository.add(securityClearance);
        assertNotNull(actual);
        assertEquals(NEXT_SECURITY_CLEARANCE_ID, actual.getSecurityClearanceId());
    }


    SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Test");
        securityClearance.setSecurityClearanceId(4);
        return securityClearance;
    }

    @Test
    void deleteById() {

        assertTrue(repository.deleteById(2));
    }

    @Test
    void shouldNotDeleteByIdMissing() {

        assertFalse(repository.deleteById(150000));
    }

    @Test
    void shouldNotIfInAgencyAgent(){
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        boolean result = repository.deleteById(1);
        assertFalse(result);

        SecurityClearance find = repository.findById(1);
        assertEquals(secret, find);
    }

}