package com.labforward.api.hello;

import com.labforward.api.common.MVCIntegrationTest;
import com.labforward.api.core.GlobalControllerAdvice;
import com.labforward.api.hello.dao.HelloDao;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest extends MVCIntegrationTest {

	private static final String HELLO_LUKE = "Hello Luke";
	
     @Autowired
     HelloDao helloDao;
     
	@Before
	public void init() {
		helloDao. saveGreeting(new Greeting("MOCKID", "A mock id for testing"));
	}
	
	@Test
	public void getHelloIsOKAndReturnsValidJSON() throws Exception {
		mockMvc.perform(get("/v1/hello"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.id", is(HelloWorldService.DEFAULT_ID)))
		       .andExpect(jsonPath("$.message", is(HelloWorldService.DEFAULT_MESSAGE)));
	}

	@Test
	public void returnsBadRequestWhenMessageMissing() throws Exception {
		String body = "{}";
		mockMvc.perform(post("/v1/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void returnsBadRequestWhenUnexpectedAttributeProvided() throws Exception {
		String body = "{ \"tacos\":\"value\" }}";
		mockMvc.perform(post("/v1/hello").content(body).contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.message", containsString(GlobalControllerAdvice.BAD_REQUEST)));
	}

	@Test
	public void returnsBadRequestWhenMessageEmptyString() throws Exception {
		Greeting emptyMessage = new Greeting("");
		final String body = getGreetingBody(emptyMessage);

		mockMvc.perform(post("/v1/hello").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnprocessableEntity())
		       .andExpect(jsonPath("$.validationErrors", hasSize(1)))
		       .andExpect(jsonPath("$.validationErrors[*].field", contains("message")));
	}

	@Test
	public void createOKWhenRequiredGreetingProvided() throws Exception {
		Greeting hello = new Greeting(HELLO_LUKE);
		final String body = getGreetingBody(hello);

		mockMvc.perform(post("/v1/hello").contentType(MediaType.APPLICATION_JSON)
		                              .content(body))
		       .andExpect(status().is2xxSuccessful())
		       .andExpect(jsonPath("$.message", is(hello.getMessage())));
	}
	
	@Test
	public void returnNotFoundWhenInvalidIdProvided() throws Exception
	{
		Greeting hello = new Greeting("1",HELLO_LUKE);
		final String body = getGreetingBody(hello);
		

		mockMvc.perform(put("/v1/hello/1").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
		     
	}
	
	@Test
	public void returnOkForUpdate() throws Exception
	{
		Greeting hello = new Greeting("MOCKID",HELLO_LUKE);
		final String body = getGreetingBody(hello);

		mockMvc.perform(put("/v1/hello/MOCKID").content(body)
		                              .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk());
		     
	}
	
	

private String getGreetingBody(Greeting greeting) throws JSONException {
		JSONObject json = new JSONObject().put("message", greeting.getMessage());

		if (greeting.getId() != null) {
			json.put("id", greeting.getId());
		}

		return json.toString();
	}

}
