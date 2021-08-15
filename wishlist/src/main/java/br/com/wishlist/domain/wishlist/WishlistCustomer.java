/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.wishlist;

import br.com.wishlist.domain.commons.exception.NotAllowException;
import br.com.wishlist.domain.customer.Customer;
import br.com.wishlist.domain.product.Product;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@Document
public class WishlistCustomer implements Serializable {

    @Id
    private String id;
    private BigDecimal totalAmount;
    @DBRef
    private Set<Product> products;
    @DBRef
    private Customer customer;

    public static WishlistCustomer of(final Set<Product> products, final Customer customer) {
        checkNotNull(products, "Products List cannot be null");
        return new WishlistCustomer(products, customer);
    }

    private WishlistCustomer(final Set<Product> products, final Customer customer) {
        this.products = products;
        this.customer = customer;
        this.updateTotalAmount();
    }

    public void addProduct(final Product product) {
        if (Objects.isNull(this.products)) {
            this.products = Sets.newHashSet();
        }
        if (this.products.size() == 20) {
            throw new NotAllowException("Não é possível adicionar mais de 20 produtos na sua lista de desejos.");
        }
        if (this.products.stream().filter(prod -> prod.getId().equals(product.getId())).count() == 0) {
            this.products.add(product);
            this.updateTotalAmount();
        }
    }

    public void removeProduct(final Product product) {
        this.products.removeIf(prod -> prod.getId().equals(product.getId()));
        this.updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = this.products.stream().map(Product::getAmount).collect(Collectors.toList())
                .stream()
                .reduce(BigDecimal.valueOf(0),
                        (acc, current) -> acc.add(current));
    }

}
