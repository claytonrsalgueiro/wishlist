/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.wishlist;

import br.com.wishlist.domain.product.Product;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author clayton.salgueiro
 */
@Repository
public interface WishlistCustomerRepository extends MongoRepository<WishlistCustomer, String> {

    Optional<WishlistCustomer> findByCustomerId(final String id);
    
    Optional<Product> findByCustomerIdAndProductsId(final String idCustomer, final String id);
    
}
