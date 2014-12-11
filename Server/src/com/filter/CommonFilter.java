package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)arg0;
		/*HttpServletResponse res = (HttpServletResponse)arg1;
		if(req.getSession(false)== null)
		{
			System.out.println("Session has been invalidated!");
			String reqStr = req.getRequestURI();
			if(!(reqStr.contains("sendEmail") || reqStr.contains("register") || reqStr.contains("login") || reqStr.contains("verify"))){
				
				res.setStatus(301);
				return;
			}
		}
		*/
		String reqStr = req.getRequestURI();
		System.out.println("Session has been invalidated!" + reqStr);
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
