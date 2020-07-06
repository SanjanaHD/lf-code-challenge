package com.labforward.api.hello;

import com.labforward.api.core.exception.EntityValidationException;
import com.labforward.api.hello.dao.HelloDao;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldServiceTest {

	@Autowired
	private HelloWorldService helloService;
	
	@Autowired
	private HelloDao helloDao;

	public HelloWorldServiceTest() {
	}
	
	@Before
	public void init() {
		helloDao. saveGreeting(new Greeting("MOCKID", "A mock id for testing"));
	}

	@Test
	public void getDefaultGreetingIsOK() {
		Optional<Greeting> greeting = helloService.getDefaultGreeting();
		Assert.assertTrue(greeting.isPresent());
		Assert.assertEquals(HelloWorldService.DEFAULT_ID, greeting.get().getId());
		Assert.assertEquals(HelloWorldService.DEFAULT_MESSAGE, greeting.get().getMessage());
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithEmptyMessageThrowsException() throws IOException {
		final String EMPTY_MESSAGE = "";
		helloService.createGreeting(new Greeting(EMPTY_MESSAGE));
	}

	@Test(expected = EntityValidationException.class)
	public void createGreetingWithNullMessageThrowsException() throws IOException {
		helloService.createGreeting(new Greeting(null));
	}

	@Test
	public void createGreetingOKWhenValidRequest() throws IOException {
		final String HELLO_LUKE = "Hello Luke";
		Greeting request = new Greeting(HELLO_LUKE);

		Greeting created = helloService.createGreeting(request);
		Assert.assertEquals(HELLO_LUKE, created.getMessage());
	}
	
	@Test
	public void UpdateGreetingOKWhenValidRequest() throws IOException {
		final String HELLO_LUKE = "Hello Luke";
		Greeting request = new Greeting(HELLO_LUKE);

		Optional<Greeting> created = helloService.updateGreeting("MOCKID",request);
		Assert.assertEquals(HELLO_LUKE, created.get().getMessage());
	}
	
	@Test(expected = EntityValidationException.class)
	public void UpdateGreetingIdMismatchErrort() throws IOException {
		final String HELLO_LUKE = "Hello Luke";
		
		
		Greeting request = new Greeting("1",HELLO_LUKE);

		//Id's in the path variable and request entity do not match(1,MOCKID)
		Optional<Greeting> created = helloService.updateGreeting("MOCKID",request);
		Assert.assertEquals(HELLO_LUKE, created.get().getMessage());
	}
	
}
