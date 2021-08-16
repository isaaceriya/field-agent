package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasServiceTest {

    @MockBean
    AliasRepository repository;

    @Autowired
    AliasService service;


    @Test
    void shouldAdd() {
        // Arrange
        Alias clearanceOne = new Alias(0, "Clearance", "dd", 4);
        Alias clearanceTwo = new Alias(1, "Clearance","ddd", 4);

        // 4. Stub a specific behavior.
        when(repository.add(clearanceOne)).thenReturn(clearanceTwo);

        // Act
        Result<Alias> result = service.add(clearanceOne);

        // Assert
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(clearanceTwo, result.getPayload());
    }

    @Test
    void ShouldNotAddDuplicateWhenNoPersona() {
        Alias one = new Alias(1, "Clearance","Tim", 4);
        Alias two = new Alias(2, "Top","",4);

        List<Alias> clearanceOne = new ArrayList<>();
        clearanceOne.add(one);
        clearanceOne.add(two);


        Alias duplicate = new Alias();
        when(repository.findAll()).thenReturn(clearanceOne);
        duplicate.setName("Clearance");
        Result<Alias> actual = service.add(duplicate);
        assertEquals(ResultType.INVALID, actual.getType());
    }


    @Test
    void ShouldAddDuplicateWhenPersona() {
        Alias one = new Alias(1, "Clearance","Tim", 4);
        Alias two = new Alias(2, "Top","",4);

        List<Alias> clearanceOne = new ArrayList<>();
        clearanceOne.add(one);
        clearanceOne.add(two);


        Alias duplicate = new Alias();
        when(repository.findAll()).thenReturn(clearanceOne);
        duplicate.setName("Clearance");
        duplicate.setPersona("Perrr");
        Result<Alias> actual = service.add(duplicate);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

}
