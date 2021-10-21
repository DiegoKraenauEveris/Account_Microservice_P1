package com.microservice.account.repositories;

import com.microservice.account.entities.ProductType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductTypeRepository extends MongoRepository<ProductType, ObjectId> {
}
