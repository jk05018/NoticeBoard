package com.example.notice.controller;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BasicControllerTest {

	@Autowired
	protected MockMvc mockMvc;


	protected static String getBaseUrl(final Class<?> clazz) {
		return Stream.of(Optional.ofNullable(AnnotationUtils.findAnnotation(clazz, RequestMapping.class))
				.map(RequestMapping::value)
				.orElseThrow(NullPointerException::new))
			.findFirst()
			.orElseThrow(NullPointerException::new);
	}
}
