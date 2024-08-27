package dev.notenger.apigw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		httpSecurity
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(authorize -> authorize
						.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.pathMatchers("/ws-api/**").permitAll()
						.anyExchange().authenticated()
				)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(withDefaults()));

		return httpSecurity.build();
	}
}