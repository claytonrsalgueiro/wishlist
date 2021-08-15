/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.wishlist;

import br.com.wishlist.domain.commons.exception.NotAllowException;
import br.com.wishlist.domain.customer.Customer;
import br.com.wishlist.domain.customer.CustomerRepository;
import br.com.wishlist.domain.product.Product;
import br.com.wishlist.domain.product.ProductRepository;
import br.com.wishlist.interfaces.dto.ProductDTO;
import br.com.wishlist.interfaces.dto.ProductWishlistDTO;
import br.com.wishlist.interfaces.dto.WishlistCustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Sets;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author clayton.salgueiro
 */
@Service
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistCustomerServiceImpl implements WishlistCustomerService {

    private final WishlistCustomerRepository wishlistCustomerRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    @Override
    public WishlistCustomerDTO addProduct(final ProductWishlistDTO productWishlistDTO) {
        checkNotNull(productWishlistDTO, "Dados do novo produto não podem ser nulos.");
        checkNotNull(productWishlistDTO.getProduct(), "Dados do novo produto não podem ser nulos.");
        checkNotNull(productWishlistDTO.getProduct().getId(), "ID do novo produto não pode ser nulo.");
        checkNotNull(productWishlistDTO.getCustomer(), "Dados do cliente não podem ser nulos.");
        checkNotNull(productWishlistDTO.getCustomer().getId(), "ID do Cliente não pode ser nulo.");

        final Product product = this.productRepository.findById(productWishlistDTO.getProduct().getId()).orElseThrow(() -> new NotAllowException("Produto não encontrado na base de dados."));
        final Customer customer = this.customerRepository.findById(productWishlistDTO.getCustomer().getId()).orElseThrow(() -> new NotAllowException("Cliente não encontrado na base de dados."));

        final WishlistCustomer wishlistCustomer = this.wishlistCustomerRepository.findByCustomerId(customer.getId()).map(wish -> {
            wish.addProduct(product);
            return wish;
        }).orElseGet(() -> {
            final Set<Product> products = Sets.newHashSet();
            products.add(product);
            return WishlistCustomer.of(products, customer);
        });
        WishlistCustomer wishSaved = this.wishlistCustomerRepository.save(wishlistCustomer);
        return this.objectMapper.convertValue(wishSaved, WishlistCustomerDTO.class);
    }

    @Override
    public WishlistCustomerDTO removeProduct(final ProductWishlistDTO productWishlistDTO) {
        checkNotNull(productWishlistDTO, "Dados do novo produto não podem ser nulos.");
        checkNotNull(productWishlistDTO.getProduct(), "Dados do novo produto não podem ser nulos.");
        checkNotNull(productWishlistDTO.getProduct().getId(), "ID do novo produto não pode ser nulo.");
        checkNotNull(productWishlistDTO.getCustomer(), "Dados do cliente não podem ser nulos.");
        checkNotNull(productWishlistDTO.getCustomer().getId(), "ID do Cliente não pode ser nulo.");

        final Product product = this.productRepository.findById(productWishlistDTO.getProduct().getId()).orElseThrow(() -> new NotAllowException("Produto não encontrado na base de dados."));
        final Customer customer = this.customerRepository.findById(productWishlistDTO.getCustomer().getId()).orElseThrow(() -> new NotAllowException("Cliente não encontrado na base de dados."));

        final WishlistCustomer wishlistCustomer = this.wishlistCustomerRepository.findByCustomerId(customer.getId()).map(wish -> {
            wish.removeProduct(product);
            return wish;
        }).orElseThrow(() -> new NotAllowException("Nenhuma lista de desejos foi encontrada para esse cliente."));
        WishlistCustomer wishSaved = this.wishlistCustomerRepository.save(wishlistCustomer);
        return this.objectMapper.convertValue(wishSaved, WishlistCustomerDTO.class);
    }

    @Override
    public WishlistCustomerDTO findByCustomerId(final String idCustomer) {
        checkNotNull(idCustomer, "ID do Cliente não pode ser nulo.");

        final Customer customer = this.customerRepository.findById(idCustomer).orElseThrow(() -> new NotAllowException("Cliente não encontrado na base de dados."));

        final WishlistCustomer wishlistCustomer = this.wishlistCustomerRepository.findByCustomerId(customer.getId()).orElse(null);

        return this.objectMapper.convertValue(wishlistCustomer, WishlistCustomerDTO.class);
    }

    @Override
    public ProductDTO checkProductInWishlist(final String idCustomer, final String idProduct) {

        final Product product = this.productRepository.findById(idProduct).orElseThrow(() -> new NotAllowException("Produto não encontrado na base de dados."));
        final Customer customer = this.customerRepository.findById(idCustomer).orElseThrow(() -> new NotAllowException("Cliente não encontrado na base de dados."));

        final WishlistCustomer wishlistCustomer = this.wishlistCustomerRepository.findByCustomerId(customer.getId()).orElse(null);
        final Product productFound = wishlistCustomer.getProducts().stream().filter(prod -> prod.getId().equals(product.getId())).findFirst().orElse(null);

        return this.objectMapper.convertValue(productFound, ProductDTO.class);
    }

}
