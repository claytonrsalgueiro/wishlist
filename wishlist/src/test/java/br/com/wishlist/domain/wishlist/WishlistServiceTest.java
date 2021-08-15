/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.wishlist;

import br.com.wishlist.domain.commons.exception.NotAllowException;
import br.com.wishlist.domain.customer.Customer;
import br.com.wishlist.domain.customer.CustomerFixture;
import br.com.wishlist.domain.customer.CustomerService;
import br.com.wishlist.domain.product.Product;
import br.com.wishlist.domain.product.ProductFixture;
import br.com.wishlist.domain.product.ProductService;
import br.com.wishlist.interfaces.dto.CustomerDTO;
import br.com.wishlist.interfaces.dto.ProductDTO;
import br.com.wishlist.interfaces.dto.ProductWishlistDTO;
import br.com.wishlist.interfaces.dto.WishlistCustomerDTO;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author clayton.salgueiro
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("tests")
@WithMockUser(username = "test", roles = "ADMIN", password = "first")
public class WishlistServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WishlistCustomerService wishlistCustomerService;

    @Autowired
    private MongoTemplate template;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeEach
    void initialSetup() {
        template.dropCollection(Product.class);
        template.dropCollection(Customer.class);
        ProductFixture.loadTemplates();
        CustomerFixture.loadTemplates();
    }

    @Test
    public void addProductSuccessTest() throws Exception {

        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();

        final WishlistCustomerDTO responseDTO = this.wishlistCustomerService.addProduct(productWishlistDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getProducts());
        assertTrue(responseDTO.getProducts().size() == 1);
    }

    @Test
    public void addProductFullListTest() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);

        for (int i = 0; i < 20; i++) {

            final ProductDTO productDTOCreated = this.createProduct(productDTO);
            this.createWishList(customerDTO, productDTOCreated);
        }

        productDTO = ProductFixture.of(ProductFixture.DEFAULT_MESA, ProductDTO.class);
        productDTO = this.createProduct(productDTO);

        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();
        Exception exception = assertThrows(NotAllowException.class, () -> {
            this.wishlistCustomerService.addProduct(productWishlistDTO);
        });
        assertEquals(exception.getMessage(), "Não é possível adicionar mais de 20 produtos na sua lista de desejos.");
    }

    @Test
    public void addProductProductNotFoundTest() throws Exception {

        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();

        Exception exception = assertThrows(NotAllowException.class, () -> {
            this.wishlistCustomerService.addProduct(productWishlistDTO);
        });
        assertEquals(exception.getMessage(), "Produto não encontrado na base de dados.");

    }

    @Test
    public void addProductCustomerNotFoundTest() throws Exception {

        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);

        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();

        Exception exception = assertThrows(NotAllowException.class, () -> {
            this.wishlistCustomerService.addProduct(productWishlistDTO);
        });
        assertEquals(exception.getMessage(), "Cliente não encontrado na base de dados.");

    }

    @Test
    public void removeProductProductNotFoundTest() throws Exception {

        ProductDTO productDTO = ProductDTO.builder().id("123").build();
        CustomerDTO customerDTO = CustomerDTO.builder().id("123").build();

        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();

        Exception exception = assertThrows(NotAllowException.class, () -> {
            this.wishlistCustomerService.removeProduct(productWishlistDTO);
        });
        assertEquals(exception.getMessage(), "Produto não encontrado na base de dados.");

    }

    @Test
    public void removeProductCustomerNotFoundTest() throws Exception {

        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerDTO.builder().id("123").build();

        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();

        Exception exception = assertThrows(NotAllowException.class, () -> {
            this.wishlistCustomerService.removeProduct(productWishlistDTO);
        });
        assertEquals(exception.getMessage(), "Cliente não encontrado na base de dados.");

    }

    @Test
    public void removeProductWishListNotFoundTest() throws Exception {

        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);

        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();
        Exception exception = assertThrows(NotAllowException.class, () -> {
            this.wishlistCustomerService.removeProduct(productWishlistDTO);
        });
        assertEquals(exception.getMessage(), "Nenhuma lista de desejos foi encontrada para esse cliente.");

    }

    @Test
    public void findByCustomerIdSucessTest() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        this.createWishList(customerDTO, productDTO);

        final WishlistCustomerDTO wishlistCustomerDTO = this.wishlistCustomerService.findByCustomerId(customerDTO.getId());
        assertNotNull(wishlistCustomerDTO);
        assertNotNull(!wishlistCustomerDTO.getProducts().isEmpty());
    }

    @Test
    public void findByCustomerNoContentTest() throws Exception {
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);

        final WishlistCustomerDTO wishlistCustomerDTO = this.wishlistCustomerService.findByCustomerId(customerDTO.getId());
        assertNull(wishlistCustomerDTO);

    }

    @Test
    public void checkProductInWishlistSuccessList() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        this.createWishList(customerDTO, productDTO);

        final ProductDTO productFoundDTO = this.wishlistCustomerService.checkProductInWishlist(customerDTO.getId(), productDTO.getId());

        assertNotNull(productFoundDTO);
        assertEquals(productFoundDTO.getId(), productDTO.getId());
        assertEquals(productFoundDTO.getName(), productDTO.getName());
    }

    @Test
    public void checkProductInWishlistNotFoundList() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        ProductDTO product2DTO = ProductFixture.of(ProductFixture.DEFAULT_MESA, ProductDTO.class);
        product2DTO = this.createProduct(product2DTO);

        this.createWishList(customerDTO, productDTO);

        final ProductDTO productFoundDTO = this.wishlistCustomerService.checkProductInWishlist(customerDTO.getId(), product2DTO.getId());

        assertNull(productFoundDTO);

    }

    private CustomerDTO createCustomer(CustomerDTO customerDTO) {
        customerDTO = this.customerService.create(customerDTO);
        return customerDTO;
    }

    private ProductDTO createProduct(ProductDTO productDTO) {
        productDTO = this.productService.create(productDTO);
        return productDTO;
    }

    private WishlistCustomerDTO createWishList(final CustomerDTO customerDTO, final ProductDTO productDTO) {
        final ProductWishlistDTO productWishlistDTO = ProductWishlistDTO.builder()
                .customer(customerDTO)
                .product(productDTO)
                .build();
        final WishlistCustomerDTO wishlistCustomerDTO = this.wishlistCustomerService.addProduct(productWishlistDTO);
        return wishlistCustomerDTO;
    }
}
