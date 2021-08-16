package learn.field_agent.domain;


import learn.field_agent.data.AliasRepository;

import learn.field_agent.models.Alias;


import org.springframework.stereotype.Service;

//    If duplicate with persona allow if duplicate with no persona require persona
//
@Service
public class AliasService {
    private final AliasRepository aliasRepository;

    public AliasService(AliasRepository aliasRepository) {
        this.aliasRepository = aliasRepository;
    }

    public Alias findById(int aliasId) {
        return aliasRepository.findById(aliasId);
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() != 0) {
            result.addMessage("agentId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        alias = aliasRepository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("aliasId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!aliasRepository.update(alias)) {
            String msg = String.format("aliasId: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return aliasRepository.deleteById(aliasId);
    }


    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();

        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
            return result;
        }

        result = isDuplicate(alias.getName(), alias.getPersona());
        if (!(result.isSuccess())){
            return result;
        }

        return result;
    }

    public Result<Alias> isDuplicate(String name, String persona) {
        Result<Alias> result = new Result<>();
        for (Alias a : aliasRepository.findAll()) {
            if (a.getName().equals(name) && Validations.isNullOrBlank(persona)) {
                result.addMessage("Duplicate Name Persona required", ResultType.INVALID);
                return result;
            } else if(a.getName().equals(name) && persona.equals(a.getPersona())){
                result.addMessage("Duplicate Persona & Name", ResultType.INVALID);
                return result;
            }
        }
        return result;
    }
}
