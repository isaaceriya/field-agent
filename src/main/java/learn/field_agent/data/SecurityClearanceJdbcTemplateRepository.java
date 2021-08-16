package learn.field_agent.data;

import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SecurityClearanceJdbcTemplateRepository implements SecurityClearanceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AgencyAgentJdbcTemplateRepository agencyAgentRepo;

    public SecurityClearanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate, AgencyAgentJdbcTemplateRepository agencyAgentRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.agencyAgentRepo = agencyAgentRepo;
    }


    @Override
    public List<SecurityClearance> findAll() {
        // limit until we develop a paging solution
        final String sql = "select security_clearance_id, name security_clearance_name from security_clearance";
        return jdbcTemplate.query(sql, new SecurityClearanceMapper());
    }

    @Override
    public SecurityClearance findById(int securityClearanceId) {

        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance "
                + "where security_clearance_id = ?;";

        return jdbcTemplate.query(sql, new SecurityClearanceMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public SecurityClearance add(SecurityClearance securityClearance) {

        final String sql = "insert into security_clearance (name) values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, securityClearance.getName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        securityClearance.setSecurityClearanceId(keyHolder.getKey().intValue());
        return securityClearance;
    }

    @Override
    public boolean update(SecurityClearance securityClearance) {

        final String sql = "update security_clearance set "
                + "name = ? "
                + "where security_clearance_id  = ?;";

        int rowsUpdated = jdbcTemplate.update(sql,
                securityClearance.getName(), securityClearance.getSecurityClearanceId());

        return rowsUpdated > 0;
    }


//    Move this to the server
    @Override
    public boolean deleteById(int securityClearanceId) {
        if(!(agencyAgentRepo.checkSecurityIdExist(securityClearanceId))){
            final String sql = "delete from security_clearance where security_clearance_id = ?;";
            return jdbcTemplate.update(sql, securityClearanceId) > 0;
        }
        return false;
    }


}
