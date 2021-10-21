package com.microservice.account.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "account_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountType {
    @Id
    private ObjectId _id;

    private Float commissions;

    @Field("max_transactions_per_month")
    private Integer maxTransactionsPerMonth;

    @Field("product_type_id")
    private ObjectId productType;

}
