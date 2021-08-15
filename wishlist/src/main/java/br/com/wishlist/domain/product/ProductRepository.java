/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author clayton.salgueiro
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

//    @Query("{'name': {$regex: ?0 }})")
    Page<Product> findByNameContainingIgnoreCase(String userName, final Pageable pageable);

}
