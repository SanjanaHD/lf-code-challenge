package com.labforward.api.hello.domain;

import com.labforward.api.core.validation.Entity;
import com.labforward.api.core.validation.EntityUpdateValidatorGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * Simple greeting message for dev purposes
 */

@ApiModel(description="Details of the Greeting")
public class Greeting implements Entity {

	@ApiModelProperty(notes="User can pass the Id,if not a random id will be generated",example="2",required=false)
    private String id;

	@ApiModelProperty(notes="Message to be displaced in the greeting(can not be null/empty)",example="Hello", required=true)
	@NotEmpty
	private String message;

	public Greeting() {
		// needed for JSON deserialization
	}

	public Greeting(String id, String message) {
		this.message = message;
		this.id = id;
	}

	public Greeting(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
