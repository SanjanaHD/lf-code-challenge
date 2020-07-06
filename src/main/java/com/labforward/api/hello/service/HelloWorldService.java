package com.labforward.api.hello.service;

import com.labforward.api.core.validation.EntityValidator;
import com.labforward.api.hello.dao.HelloDao;
import com.labforward.api.hello.domain.Greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class HelloWorldService {

	public static final String GREETING_NOT_FOUND = "Greeting Not Found";

	public static String DEFAULT_ID = "default";

	public static String DEFAULT_MESSAGE = "Hello World!";

	@Autowired
	private HelloDao helloDao;

	@Autowired
	private EntityValidator entityValidator;

	public HelloWorldService(HelloDao helloDao) {
		this.helloDao = helloDao;

	}

	public Greeting createGreeting(Greeting request) {
		entityValidator.validateCreate(request);
		if (StringUtils.isEmpty(request.getId())) {
			request.setId(UUID.randomUUID().toString());
		}
		return helloDao.saveGreeting(request);
	}

	public Optional<Greeting> getGreeting(String id) {
		return helloDao.selectGreeting(id);
	}

	public Optional<Greeting> getDefaultGreeting() {
		return getGreeting(DEFAULT_ID);
	}

	public Optional<Greeting> updateGreeting(String id, Greeting greetingToBeUpdated) {
		entityValidator.validateUpdate(id, greetingToBeUpdated);
		greetingToBeUpdated.setId(id);
		return helloDao.updateGreeting(id,greetingToBeUpdated);
	}

}
