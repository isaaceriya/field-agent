package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;


public class Validations {

    private final SecurityClearanceRepository securityClearanceRepository;

    public Validations(SecurityClearanceRepository securityClearanceRepository) {
        this.securityClearanceRepository = securityClearanceRepository;
    }

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

}
