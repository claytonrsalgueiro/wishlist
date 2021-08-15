/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons.exception;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author clayton.salgueiro
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiInnerError implements Serializable {

    private static final long serialVersionUID = -3777785656438836598L;
    private String field;
    private String code;
    private String message;

    public ApiInnerError(final String field, final String message) {
        this(field, null, message);
    }

}
