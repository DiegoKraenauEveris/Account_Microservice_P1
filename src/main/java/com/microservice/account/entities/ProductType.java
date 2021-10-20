package com.microservice.account.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {
    @Id
    private ObjectId _id;

    private String type;
}
