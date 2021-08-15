/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.customer;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.wishlist.interfaces.dto.CustomerDTO;

/**
 *
 * @author clayton.salgueiro
 */
public class CustomerFixture {

    public static final String DEFAULT_CLIENT_1 = "default_cliente_1";
    public static final String DEFAULT_CLIENT_2 = "default_cliente_2";

    public static void loadTemplates() {
        loadCustomers();
    }

    public static <T> T of(final String templateName, final Class<T> as) {
        return Fixture.from(as).gimme(templateName);
    }

    private static void loadCustomers() {

        Fixture.of(Customer.class).addTemplate(DEFAULT_CLIENT_1, new Rule() {
            {
                this.add("id", "123asdf");
                this.add("name", "CLiente 1");
                this.add("document", "123");
            }
        }).addTemplate(DEFAULT_CLIENT_2, new Rule() {
            {
                this.add("id", "456asdf");
                this.add("name", "CLiente 2");
                this.add("document", "456");
            }
        });

        Fixture.of(CustomerDTO.class).addTemplate(DEFAULT_CLIENT_1, new Rule() {
            {
                this.add("id", "123asdf");
                this.add("name", "CLiente 1");
                this.add("document", "123");
            }
        }).addTemplate(DEFAULT_CLIENT_2, new Rule() {
            {
                this.add("id", "456asdf");
                this.add("name", "CLiente 2");
                this.add("document", "456");
            }
        });

    }
}
