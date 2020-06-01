package com.colleen.letdown.dao;

import com.colleen.letdown.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDAO extends CrudRepository<Token, Long> {
    Token findByToken (String token);
}
