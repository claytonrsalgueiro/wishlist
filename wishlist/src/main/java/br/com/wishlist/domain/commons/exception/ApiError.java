/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author clayton.salgueiro
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiError implements Serializable {
	private static final long serialVersionUID = 3565831364680996420L;

	private String code;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ApiInnerError> errors;

	public ApiError(final String code, final String message) {
		this(code, message, null);
	}

	public ApiError(final String code, final List<ApiInnerError> errors) {
		this(code, code, errors);
	}

	public ApiError(final String code, final String message, final List<ApiInnerError> errors) {
		this.code = code;
		this.message = message;
		this.errors = errors;
	}

}
