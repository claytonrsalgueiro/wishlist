/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.customer;

import br.com.wishlist.domain.commons.exception.NotAllowException;
import br.com.wishlist.interfaces.dto.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author clayton.salgueiro
 */
@Service
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @Override
    public CustomerDTO create(final CustomerDTO customerDTO) {

        this.customerRepository.findByDocumentLike(customerDTO.getDocument()).ifPresent(cust -> {
            throw new NotAllowException("Ja existe um cliente com esse documento cadastrado na base de dados.");
        });
        final Customer customer = Customer.of(customerDTO.getName(), customerDTO.getDocument());
        final Customer customerSaved = this.customerRepository.save(customer);
        return this.objectMapper.convertValue(customerSaved, CustomerDTO.class);
    }

    @Override
    public Page<CustomerDTO> findBySearch(final String search, final Pageable pageable) {
        final String searchField = Optional.ofNullable(search).map(sea -> sea).orElse("");
        Page<Customer> resultPage = this.customerRepository.findByNameContainingIgnoreCase(searchField, pageable);
        List<CustomerDTO> resultList = resultPage.stream().map(cust -> this.objectMapper.convertValue(cust, CustomerDTO.class)).collect(Collectors.toList());

        return new PageImpl<>(resultList, pageable, resultPage.getTotalElements());
    }

}
