/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.application;

import br.com.wishlist.interfaces.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author clayton.salgueiro
 */
public interface CustomerFacade {

    public CustomerDTO create(final CustomerDTO customerDTO);

    public Page<CustomerDTO> findBySearch(final String search, final Pageable pageable);
}
