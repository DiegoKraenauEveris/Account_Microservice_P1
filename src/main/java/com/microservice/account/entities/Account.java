package com.microservice.account.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private ObjectId _id;

    private Double balance;

    @Field("account_number")
    private String accountNumber;

    @Field("account_type_id")
    private ObjectId accountTypeId;
}
