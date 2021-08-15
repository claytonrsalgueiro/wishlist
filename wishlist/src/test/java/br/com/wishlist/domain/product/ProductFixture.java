/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.product;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.wishlist.interfaces.dto.ProductDTO;
import java.math.BigDecimal;

/**
 *
 * @author clayton.salgueiro
 */
public class ProductFixture {

    public static final String DEFAULT_COMPUTADOR = "default_computador";
    public static final String DEFAULT_MESA = "default_mesa";

    public static void loadTemplates() {
        loadProducts();
    }

    public static <T> T of(final String templateName, final Class<T> as) {
        return Fixture.from(as).gimme(templateName);
    }

    private static void loadProducts() {

        Fixture.of(Product.class).addTemplate(DEFAULT_COMPUTADOR, new Rule() {
            {
                this.add("id", "123asdf");
                this.add("name", "Computador");
                this.add("description", "Computador 4GB");
                this.add("amount", BigDecimal.valueOf(1000));
            }
        }).addTemplate(DEFAULT_MESA, new Rule() {
            {
                this.add("id", "456asdf");
                this.add("name", "Computador");
                this.add("description", "Computador 4GB");
                this.add("amount", BigDecimal.valueOf(1000));
            }
        });

        Fixture.of(ProductDTO.class).addTemplate(DEFAULT_COMPUTADOR, new Rule() {
            {
                this.add("id", "123asdf");
                this.add("name", "Computador");
                this.add("description", "Computador 4GB");
                this.add("amount", BigDecimal.valueOf(1000));
            }
        }).addTemplate(DEFAULT_MESA, new Rule() {
            {
                this.add("id", "456asdf");
                this.add("name", "Computador");
                this.add("description", "Computador 4GB");
                this.add("amount", BigDecimal.valueOf(1000));
            }
        });

    }
}
