package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SecurityClearanceServiceTest {

    @MockBean
    SecurityClearanceRepository repository;

    @Autowired
    SecurityClearanceService service;

    @Test
    void shouldAdd() {
        // Arrange
        SecurityClearance clearanceOne = new SecurityClearance(0, "Clearance");
        SecurityClearance clearanceTwo = new SecurityClearance(1, "Clearance");

        // 4. Stub a specific behavior.
        when(repository.add(clearanceOne)).thenReturn(clearanceTwo);

        // Act
        Result<SecurityClearance> result = service.add(clearanceOne);

        // Assert
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(clearanceTwo, result.getPayload());
    }

    @Test
    void ShouldNotAddDuplicate() {
        SecurityClearance one = new SecurityClearance(1, "Clearance");
        SecurityClearance two = new SecurityClearance(2, "Top");

        List<SecurityClearance> clearanceOne = new ArrayList<>();
        clearanceOne.add(one);
        clearanceOne.add(two);


        SecurityClearance duplicate = new SecurityClearance();
        when(repository.findAll()).thenReturn(clearanceOne);
        duplicate.setName("Clearance");
        Result<SecurityClearance> actual = service.add(duplicate);
        assertEquals(ResultType.INVALID, actual.getType());
    }
}
