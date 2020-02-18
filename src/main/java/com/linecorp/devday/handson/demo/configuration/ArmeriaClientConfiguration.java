package com.linecorp.devday.handson.demo.configuration;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.logging.ContentPreviewingClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.spring.web.reactive.ArmeriaClientConfigurator;
import com.linecorp.devday.handson.demo.decorator.PrettyJsonPreviewClientDecorator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ArmeriaClientConfiguration {

	private final PrettyJsonPreviewClientDecorator prettyJsonPreviewClientDecorator;

	@Bean
	public ClientFactory clientFactory() {

		return ClientFactory.builder().tlsNoVerify().build();

	}

	@Bean
	public ArmeriaClientConfigurator armeriaClientConfigurator(ClientFactory clientFactory) {

		return clientBuilder -> {

			clientBuilder.decorator(LoggingClient.newDecorator());
			clientBuilder.decorator(ContentPreviewingClient.newDecorator(Integer.MAX_VALUE, StandardCharsets.UTF_8));
			clientBuilder.decorator(prettyJsonPreviewClientDecorator);
			clientBuilder.factory(clientFactory);

		};

	}

	@Bean
	public WebClient webClient(WebClient.Builder armeriaWebClientBuilder) {

		return armeriaWebClientBuilder.build();

	}

}
