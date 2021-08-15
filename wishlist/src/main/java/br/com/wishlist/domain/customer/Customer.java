/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.customer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@Document(collection = "customer")
public class Customer implements Serializable {

    @Id
    private String id;
    private final String name;
    private final String document;

    public static Customer of(final String name, final String document) {
        checkNotNull(name, "Nome do cliente não pode ser nulo.");
        checkNotNull(document, "Documento não pode ser nulo.");
        return new Customer(name, document);
    }

    private Customer(final String name, final String document) {
        this.name = name;
        this.document = document;
    }

}
