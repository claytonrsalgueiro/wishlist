/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.application.impl;

import br.com.wishlist.application.CustomerFacade;
import br.com.wishlist.domain.customer.CustomerService;
import br.com.wishlist.interfaces.dto.CustomerDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author clayton.salgueiro
 */
@Component
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerFacadeImpl implements CustomerFacade {

    private final CustomerService customerService;

    @Override
    public CustomerDTO create(final CustomerDTO customerDTO) {
        return this.customerService.create(customerDTO);
    }

    @Override
    public Page<CustomerDTO> findBySearch(final String search, final Pageable pageable) {
        return this.customerService.findBySearch(search, pageable);
    }

}
