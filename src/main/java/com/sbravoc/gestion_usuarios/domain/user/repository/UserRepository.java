package com.sbravoc.gestion_usuarios.domain.user.repository;

import com.sbravoc.gestion_usuarios.domain.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
