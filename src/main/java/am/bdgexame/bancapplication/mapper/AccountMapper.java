package am.bdgexame.bancapplication.mapper;

import am.bdgexame.bancapplication.dao.AccountDao;
import am.bdgexame.bancapplication.entity.AccountEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public interface AccountMapper extends Mapper<AccountEntity, AccountDao> {
}
