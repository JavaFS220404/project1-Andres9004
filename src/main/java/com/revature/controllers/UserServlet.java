package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.UserService;

public class UserServlet extends HttpServlet{

	
	private UserService userService = new UserService();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException,IOException {
		
		//Check that the user is logged in
		HttpSession session = req.getSession(false);
		PrintWriter printWriter = resp.getWriter();
		
		if(session!=null) {
			if(req.getMethod().equals("GET")) {
				
				resp.setContentType("application/json");
				resp.setStatus(404);
				String username = (String) session.getAttribute("username");
				User user = (User) session.getAttribute("user");
				if (user.getRole()==Role.FINANCE_MANAGER) {
					String uri = req.getRequestURI();
					System.out.println(uri);
					
					
					String[] urlSections = uri.split("/"); 
					
					
					if(urlSections.length ==3) {
					List<User> list = userService.getAll();
					
					String json = objectMapper.writeValueAsString(list);
					
					PrintWriter print = resp.getWriter();
					print.print(json);
					resp.setStatus(200);
					resp.setContentType("application/json");
					}else if (urlSections.length ==4) {
						String spacedName = urlSections[3].replace("%20", " ");
						User userget = userService.getByUsername(spacedName).get();
				
						String json = objectMapper.writeValueAsString(userget);
						
						PrintWriter print = resp.getWriter();
						print.print(json);
						resp.setStatus(200);
						resp.setContentType("application/json");
					}
				}else {
					resp.setStatus(401);
					printWriter.print("<h1>Only Finance Managers can view other users</h1>");
				}
				
				
			}
			
		} else {
			printWriter.print("<h1>User is not logged in!</h1>");
			resp.setStatus(401);		}
		
			
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		
		resp.setStatus(406);
		BufferedReader reader = req.getReader();
		StringBuilder stringBuilder = new StringBuilder();
		String line = reader.readLine(); 
	
		while(line != null) {
			stringBuilder.append(line);
			line = reader.readLine(); 
		}
		
		String body = new String(stringBuilder);
		
		User user = objectMapper.readValue(body,  User.class);
		user.setRole(Role.EMPLOYEE);
		if(userService.create(user)!=null) {
			resp.setStatus(201);
		}else {
			resp.setStatus(406);
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		doGet(req,resp);
	}
	
	
	
}
