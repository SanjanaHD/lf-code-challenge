package com.labforward.api.hello.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.labforward.api.core.creation.EntityCreatedResponse;
import com.labforward.api.hello.domain.Greeting;

@Repository
public class HelloDao {

	public static String DEFAULT_ID = "default";

	public static String DEFAULT_MESSAGE = "Hello World!";

	private Map<String, Greeting> greetings;

	public HelloDao() {
		this.greetings = new HashMap<>(1);
		saveGreeting(getDefault());

	}

	public Greeting saveGreeting(Greeting greeting) {
		greetings.put(greeting.getId(), greeting);
		return greeting;
	}

	public Optional<Greeting> selectGreeting(String id) {
		Greeting greeting = greetings.get(id);
		if (greeting == null) {
			return Optional.empty();
		}

		return Optional.of(greeting);
	}

	public Optional<Greeting> updateGreeting(String id, Greeting greetingToBeUpdated) {
		return greetings.containsKey(id) ? Optional.of(saveGreeting(greetingToBeUpdated)) : Optional.empty();
	}

	private static Greeting getDefault() {
		return new Greeting(DEFAULT_ID, DEFAULT_MESSAGE);
	}
	// TODO implement delete

}
