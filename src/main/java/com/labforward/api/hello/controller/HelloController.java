package com.labforward.api.hello.controller;

import com.labforward.api.core.creation.EntityCreatedResponse;
import com.labforward.api.core.exception.ResourceNotFoundException;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("v1/hello")
@RestController
@Api(tags="Greeting Service")
public class HelloController {

	public static final String GREETING_NOT_FOUND = "Greeting Not Found";

	@Autowired
	private HelloWorldService helloWorldService;

	public HelloController(HelloWorldService helloWorldService) {
		this.helloWorldService = helloWorldService;
	}

	@GetMapping
	@ApiOperation(httpMethod = "GET", value = "getWorld", response = Greeting.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The default greeting is fetched successfully", response = Greeting.class),

	})
	public Greeting helloWorld() {
		return getHello(HelloWorldService.DEFAULT_ID);
	}

	@GetMapping(value = "{id}")
	@ApiOperation(httpMethod = "GET", value = "getHello", response = Greeting.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The greeting is fetched successfully", response = Greeting.class),
			@ApiResponse(code = 404, message = "Resource not found")

	})
	@ResponseBody
	public Greeting getHello(
			@PathVariable("id") @ApiParam(name = "id", value = "Id for fetching the greeting", required = true) String id) {

		return helloWorldService.getGreeting(id).orElseThrow(() -> new ResourceNotFoundException(GREETING_NOT_FOUND));
	}

	@PostMapping
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The greeting is successfully created", response = Greeting.class) })

	public EntityCreatedResponse<Greeting> createGreeting(@RequestBody Greeting request) {
		Greeting createdGreeting = helloWorldService.createGreeting(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(createdGreeting.getId()).toUri();
		return (new EntityCreatedResponse<Greeting>(createdGreeting, location));
	}

	@PutMapping(value = "{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The greeting is updated successfully ", response = Greeting.class),
			@ApiResponse(code = 404, message = "Resource not found") })
	public Greeting updateGreeting(
			@PathVariable(name = "id") @ApiParam(name = "id", value = "Id Required for updating the greeting", required = true) String id,
			@RequestBody Greeting greeting) {
		return helloWorldService.updateGreeting(id, greeting)
				.orElseThrow(() -> new ResourceNotFoundException(GREETING_NOT_FOUND));

	}
}
