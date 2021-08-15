/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.interfaces;

import br.com.wishlist.domain.customer.Customer;
import br.com.wishlist.domain.customer.CustomerFixture;
import br.com.wishlist.domain.customer.CustomerService;
import br.com.wishlist.domain.product.Product;
import br.com.wishlist.domain.product.ProductFixture;
import br.com.wishlist.domain.product.ProductService;
import br.com.wishlist.domain.wishlist.WishlistCustomerService;
import br.com.wishlist.interfaces.dto.CustomerDTO;
import br.com.wishlist.interfaces.dto.ProductDTO;
import br.com.wishlist.interfaces.dto.ProductWishlistDTO;
import br.com.wishlist.interfaces.dto.WishlistCustomerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author clayton.salgueiro
 */
//@RunWith(SpringRunner.class)
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("tests")
@WithMockUser(username = "test", roles = "ADMIN", password = "first")
@AutoConfigureMockMvc
public class WishlistControllerTest {

    private final String BASE_URL = "/api/v1/wishlist";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

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

        final JSONObject json = new JSONObject();
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        json.put("product", new JSONObject().put("id", productDTO.getId()));
        json.put("customer", new JSONObject().put("id", customerDTO.getId()));

        final MockHttpServletResponse resp = this.mockMvc
                .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toString()))
                .andExpect(status().isOk()).andReturn().getResponse();
        final WishlistCustomerDTO responseDTO = this.mapper.readValue(resp.getContentAsString(), new TypeReference<WishlistCustomerDTO>() {
        });

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

        final JSONObject json = new JSONObject();
        productDTO = ProductFixture.of(ProductFixture.DEFAULT_MESA, ProductDTO.class);
        productDTO = this.createProduct(productDTO);

        json.put("product", new JSONObject().put("id", productDTO.getId()));
        json.put("customer", new JSONObject().put("id", customerDTO.getId()));

        this.mockMvc
                .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toString()))
                .andExpect(status().isInternalServerError()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Não é possível adicionar mais de 20 produtos na sua lista de desejos."));

    }

    @Test
    public void addProductProductNotFoundTest() throws Exception {

        final JSONObject json = new JSONObject();
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        json.put("product", new JSONObject().put("id", productDTO.getId()));
        json.put("customer", new JSONObject().put("id", customerDTO.getId()));

        this.mockMvc
                .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toString()))
                .andExpect(status().isInternalServerError()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Produto não encontrado na base de dados."));

    }

    @Test
    public void addProductCustomerNotFoundTest() throws Exception {

        final JSONObject json = new JSONObject();
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        json.put("product", new JSONObject().put("id", productDTO.getId()));
        json.put("customer", new JSONObject().put("id", customerDTO.getId()));

        this.mockMvc
                .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toString()))
                .andExpect(status().isInternalServerError()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cliente não encontrado na base de dados."));

    }

    @Test
    public void removeProductSuccessTest() throws Exception {

        final JSONObject json = new JSONObject();
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        json.put("product", new JSONObject().put("id", productDTO.getId()));
        json.put("customer", new JSONObject().put("id", customerDTO.getId()));

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(json.toString()))
                .andExpect(status().isOk()).andReturn().getResponse();
        final String uri = UriComponentsBuilder.fromUriString(BASE_URL.concat("/").concat(productDTO.getId()).concat("/").concat(customerDTO.getId())).toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(status().isOk());

    }

    @Test
    public void removeProductProductNotFoundTest() throws Exception {

        final String uri = UriComponentsBuilder.fromUriString(BASE_URL.concat("/").concat("123").concat("/").concat("123")).toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(status().isInternalServerError()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Produto não encontrado na base de dados."));

    }

    @Test
    public void removeProductCustomerNotFoundTest() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);

        final String uri = UriComponentsBuilder.fromUriString(BASE_URL.concat("/").concat(productDTO.getId()).concat("/").concat("123")).toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(status().isInternalServerError()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cliente não encontrado na base de dados."));

    }

    @Test
    public void removeProductWishListNotFoundTest() throws Exception {

        final JSONObject json = new JSONObject();
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);
        json.put("product", new JSONObject().put("id", productDTO.getId()));
        json.put("customer", new JSONObject().put("id", customerDTO.getId()));

        final String uri = UriComponentsBuilder.fromUriString(BASE_URL.concat("/").concat(productDTO.getId()).concat("/").concat(customerDTO.getId())).toUriString();

        this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(status().isInternalServerError()).andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Nenhuma lista de desejos foi encontrada para esse cliente."));;

    }

    @Test
    public void findByCustomerIdSucessTest() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);

        this.createWishList(customerDTO, productDTO);
        final String uri = BASE_URL.concat("?idCustomer=").concat(customerDTO.getId());
        this.mockMvc.perform(get(uri)).andExpect(status().isOk());

    }

    @Test
    public void findByCustomerNotFoundTest() throws Exception {
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);

        final String uri = BASE_URL.concat("?idCustomer=").concat(customerDTO.getId());
        this.mockMvc.perform(get(uri)).andExpect(status().isNoContent());

    }

    @Test
    public void checkProductInWishlistSuccessList() throws Exception {
        ProductDTO productDTO = ProductFixture.of(ProductFixture.DEFAULT_COMPUTADOR, ProductDTO.class);
        productDTO = this.createProduct(productDTO);
        CustomerDTO customerDTO = CustomerFixture.of(CustomerFixture.DEFAULT_CLIENT_1, CustomerDTO.class);
        customerDTO = this.createCustomer(customerDTO);

        this.createWishList(customerDTO, productDTO);

        final String uri = BASE_URL.concat("/check-product?idCustomer=").concat(customerDTO.getId()).concat("&idProduct=").concat(productDTO.getId());
        this.mockMvc.perform(get(uri)).andExpect(status().isOk());

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

        final String uri = BASE_URL.concat("/check-product?idCustomer=").concat(customerDTO.getId()).concat("&idProduct=").concat(product2DTO.getId());
        this.mockMvc.perform(get(uri)).andExpect(status().isNoContent());

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
