package am.bdgexame.bancapplication.mapper;

import am.bdgexame.bancapplication.dao.UserDao;
import am.bdgexame.bancapplication.entity.UserEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public interface UserMapper extends Mapper<UserEntity, UserDao> {
}
