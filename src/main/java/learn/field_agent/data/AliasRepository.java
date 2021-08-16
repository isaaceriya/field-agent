package learn.field_agent.data;


import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;

import java.util.List;


public interface AliasRepository {
    Alias findById(int aliasId);

//    boolean add(Alias Alias);

    Alias add(Alias alias);
    Alias findAgentId(int aliasId);

    boolean deleteById(int aliasId);

    boolean deleteByKey(int aliasId, int agentId);

    boolean update(Alias alias);

    List<Alias> findAll();
}
