package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.AbstractUser;
import com.revature.models.User;
import com.revature.services.AuthService;

public class LoginServlet extends HttpServlet{
	
	private ObjectMapper mapper = new ObjectMapper();
	private AuthService authService = new AuthService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		resp.setContentType("application/json");
		resp.setStatus(404);
		
		BufferedReader reader = req.getReader();
		StringBuilder stBuilder = new StringBuilder();
		String line = reader.readLine();
		
		while(line!=null) {
			stBuilder.append(line);
			line = reader.readLine();
		}
		
		String body = new String(stBuilder);
		System.out.println(body);
		User user = mapper.readValue(body, User.class);
		user = authService.login(user.getUsername(), user.getPassword());
		
		if (user!=null) {
			HttpSession session = req.getSession();
			session.setAttribute("username", user.getUsername());
			session.setAttribute("role", user.getRole().toString());
			session.setAttribute("user", user);
			resp.setStatus(200); //login successful
		}else {
			resp.setStatus(401);
			
		}
	}
}
