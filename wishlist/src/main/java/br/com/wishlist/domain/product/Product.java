/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.product;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@Document
public class Product implements Serializable {

    @Id
    private String id;
    private BigDecimal amount;
    private String name;
    private String description;

    public static Product of(final BigDecimal amount, final String name, final String description) {
        checkNotNull(amount, "Amount cannot be null");
        checkNotNull(name, "Name cannot be null");
        checkNotNull(description, "Description cannot be null");
        return new Product(amount, name, description);
    }

    private Product(final BigDecimal amount, final String name, final String description) {
        this.amount = amount;
        this.name = name;
        this.description = description;
    }

}
