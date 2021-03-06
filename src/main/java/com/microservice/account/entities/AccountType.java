package com.microservice.account.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountType {

    private String type;

    private Double commissions;

    @Field("max_transactions_per_month")
    private Integer maxTransactionsPerMonth;

    @Field("product_type")
    private String productType;

}
