package com.recordit.server.logger;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCLoggingFilter implements Filter {

	@Override
	public void doFilter(
			final ServletRequest request,
			final ServletResponse response,
			final FilterChain chain
	) throws IOException, ServletException {
		final UUID uuid = UUID.randomUUID();
		MDC.put("trace_id", uuid.toString().substring(0, uuid.toString().indexOf("-")));
		chain.doFilter(request, response);
		MDC.clear();
	}
}
