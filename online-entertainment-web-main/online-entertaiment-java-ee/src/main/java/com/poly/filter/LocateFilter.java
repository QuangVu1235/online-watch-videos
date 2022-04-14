package com.poly.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "locate-filter", urlPatterns = "/*")
public class LocateFilter implements HttpFilter {

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String lang = request.getParameter("lang");
		if (lang != null) {
			request.getSession().setAttribute("lang", lang);
		}
		chain.doFilter(req, resp);
	}

}
