package com.microservice.account.repositories;

import com.microservice.account.entities.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends MongoRepository<Account, ObjectId> {
}
