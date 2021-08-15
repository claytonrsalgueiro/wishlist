/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.customer;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author clayton.salgueiro
 */
@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByDocumentLike(final String document);

    Page<Customer> findByNameContainingIgnoreCase(final String search, final Pageable pageable);

}
