package com.microservice.account.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    private ObjectId _id;

    private Double balance;

    @Field("account_number")
    private String accountNumber;

    @Field("customer_ids")
    private List<ObjectId> customersIds;

    @Field("account_type")
    private AccountType accountType;
}
