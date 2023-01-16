package com.recordit.server.logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HTTPLogFilter implements Filter {

	@Override
	public void doFilter(
			ServletRequest request,
			ServletResponse response,
			FilterChain chain
	) throws IOException, ServletException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(
				(HttpServletRequest)request
		);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
				(HttpServletResponse)response
		);

		long start = System.currentTimeMillis();
		chain.doFilter(requestWrapper, responseWrapper);
		long end = System.currentTimeMillis();

		if (isInExcludeURIForLog(requestWrapper)) {
			responseWrapper.copyBodyToResponse();
			return;
		}

		log.info("\n" +
						"[REQUEST] {} - {} {} - {}ms\n" +
						"Headers : {}\n" +
						"RequestParameter : {}\n" +
						"RequestBody : {}\n" +
						"Response : {}\n",
				((HttpServletRequest)request).getMethod(),
				((HttpServletRequest)request).getRequestURI(),
				responseWrapper.getStatus(),
				(end - start),
				getHeaders(requestWrapper),
				getRequestParameter(requestWrapper),
				getRequestBody(requestWrapper),
				getResponseBody(responseWrapper));
	}

	private boolean isInExcludeURIForLog(ContentCachingRequestWrapper requestWrapper) {
		if ((requestWrapper.getRequestURI().contains("/api/swagger"))) {
			return true;
		}
		if ("/v2/api-docs".equals(requestWrapper.getRequestURI())) {
			return true;
		}
		return false;
	}

	private Map getHeaders(ContentCachingRequestWrapper requestWrapper) {
		Map headerMap = new HashMap<>();

		Enumeration headerArray = requestWrapper.getHeaderNames();
		while (headerArray.hasMoreElements()) {
			String headerName = (String)headerArray.nextElement();
			headerMap.put(headerName, requestWrapper.getHeader(headerName));
		}
		return headerMap;
	}

	private String getRequestParameter(ContentCachingRequestWrapper requestWrapper) {
		Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
		if (parameterMap.size() == 0) {
			return "-";
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			sb.append(entry.getKey() + "=");
			for (String value : entry.getValue()) {
				sb.append(value + ", ");
			}
			sb.replace(sb.length() - 2, sb.length(), " | ");
		}
		sb.replace(sb.length() - 2, sb.length(), "");
		return sb.toString();
	}

	private String getRequestBody(ContentCachingRequestWrapper requestWrapper) {
		if (requestWrapper != null) {
			byte[] buf = requestWrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					return new String(buf, 0, buf.length, requestWrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {
					return " - ";
				}
			}
		}
		return " - ";
	}

	private String getResponseBody(ContentCachingResponseWrapper responseWrapper) throws IOException {
		String payload = null;
		if (responseWrapper != null) {
			responseWrapper.setCharacterEncoding("UTF-8");
			byte[] buf = responseWrapper.getContentAsByteArray();
			if (buf.length > 0) {
				payload = new String(buf, 0, buf.length, responseWrapper.getCharacterEncoding());
				responseWrapper.copyBodyToResponse();
			}
		}
		return null == payload ? " - " : payload;
	}
}
