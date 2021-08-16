package learn.field_agent.data;


import learn.field_agent.data.mappers.AliasMapper;
import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AliasJdbcTemplateRepository implements AliasRepository{

    private final JdbcTemplate jdbcTemplate;

    public AliasJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Alias> findAll() {
        // limit until we develop a paging solution
        final String sql = "select alias_id, name alias_name, persona, agent_id from alias";
        return jdbcTemplate.query(sql, new AliasMapper());
    }
    @Override
    public Alias findById(int aliasId) {
        final String sql = "select alias_id, name alias_name, persona, agent_id "
                + "from alias "
                + "where alias_id = ?;";

        return jdbcTemplate.query(sql, new AliasMapper(), aliasId)
                .stream()
                .findFirst().orElse(null);
    }


    public Alias add(Alias alias) {

        final String sql = "insert into alias (name, persona, agent_id) values (?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, alias.getName());
            ps.setString(2, alias.getPersona());
            ps.setInt(3, alias.getAgentId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        alias.setAliasId(keyHolder.getKey().intValue());
        return alias;
    }

   /* @Override
    public boolean add(Alias alias) {

        final String sql = "insert into alias (name, persona, agent_id) values (?,?,?);";

        return jdbcTemplate.update(sql,
                alias.getName(),
                alias.getPersona(),
                alias.getAgent().getAgentId()) > 0;
    }*/


    @Override
    public boolean update(Alias alias) {

        final String sql = "update alias set "
                + "name = ?, "
                + "persona = ?, "
                + "agent_id =? "
                + "where alias_id = ?;";


        return jdbcTemplate.update(sql,
                alias.getName(),
                alias.getPersona(),
                alias.getAgentId(),
                alias.getAliasId()) > 0;

    }


    @Override
    public boolean deleteByKey(int aliasId, int agentId) {

        final String sql = "delete from agency_agent "
                + "where alias_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql, aliasId, agentId) > 0;
    }

    @Override
    public boolean deleteById(int aliasId) {
        final String sql = "delete from alias where alias_id = ?;";
        return jdbcTemplate.update(sql, aliasId) > 0;
    }


    @Override
    public Alias findAgentId(int aliasId){
        final String sql = ("select agent_id "
                + "from alias "
                + "where alias_id = ?;");

        return jdbcTemplate.query(sql, new AliasMapper(), aliasId)
                .stream()
                .findFirst().orElse(null);
    }

}
