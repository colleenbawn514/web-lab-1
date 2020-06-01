package com.colleen.letdown.dao;

import com.colleen.letdown.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends CrudRepository<User, Long> {
    User findByLogin (String login);
}
