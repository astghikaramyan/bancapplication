package am.bdgexame.bancapplication.mapper;

import am.bdgexame.bancapplication.dao.TransactionDao;
import am.bdgexame.bancapplication.entity.TransactionEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public interface TransactionMapper extends Mapper<TransactionEntity, TransactionDao> {
}
