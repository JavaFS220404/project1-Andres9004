package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.RType;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

public class ReimbServlet extends HttpServlet{

	//private HomeService homeService = new HomeService();
	private ReimbursementService reimbService = new ReimbursementService();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException,IOException {
		
		//Check that the user is logged in
		HttpSession session = req.getSession(false);
		PrintWriter printWriter = resp.getWriter();
		
		if(session!=null) {
			String username = (String) session.getAttribute("username");
			User user = (User) session.getAttribute("user");
			System.out.println("logged in user is "+user);
			
			if(req.getMethod().equals("GET")) {
				String uri = req.getRequestURI();
				System.out.println(uri);
				String[] urlSections = uri.split("/"); 
				
				
				if(urlSections.length ==3) {
					
					List<Reimbursement> list;
					if (user.getRole()==Role.FINANCE_MANAGER) {
						list = reimbService.getAllReimbursements();
					}else {
						list = reimbService.getAllReimbursements(user);
					}
					
					String json = objectMapper.writeValueAsString(list);
					
					PrintWriter print = resp.getWriter();
					print.print(json);
					resp.setStatus(200);
					resp.setContentType("application/json");
				}else if (urlSections.length ==4) {
					if (user.getRole()==Role.FINANCE_MANAGER) {
						String spacedName = urlSections[3].replace("%20", " ");
						int reimb_id = Integer.parseInt(spacedName);
						Reimbursement reimb = reimbService.getReimbursementByID(reimb_id).get();
				
						String json = objectMapper.writeValueAsString(reimb);
						
						PrintWriter print = resp.getWriter();
						print.print(json);
						resp.setStatus(200);
						resp.setContentType("application/json");
					}else {
						resp.setStatus(406);
						printWriter.print("<h1>Invalid id</h1>");
						
					}
				}
			}else if(req.getMethod().equals("POST")) {
				
				
				resp.setStatus(406);
				BufferedReader reader = req.getReader();
				StringBuilder stringBuilder = new StringBuilder();
				String line = reader.readLine(); 
			
				while(line != null) {
					stringBuilder.append(line);
					line = reader.readLine(); 
				}
				
				String body = new String(stringBuilder);
				//System.out.println("body is"+body);
		    	int ticketTypeId = Character.getNumericValue(body.charAt(body.length() - 2));
				body = body.substring(0, body.length() - 18)+" }";
				System.out.println("new body is"+body);
				
				Reimbursement newTicket= objectMapper.readValue(body,  Reimbursement.class);
				
				rtypeswitch:
				switch(ticketTypeId) {
				case 1:
					newTicket.setReimbType(RType.LODGING);
					break rtypeswitch;
				case 2:
					newTicket.setReimbType(RType.TRAVEL);
					break rtypeswitch;
				case 3:
					newTicket.setReimbType(RType.FOOD);
					break rtypeswitch;
				case 4:
					newTicket.setReimbType(RType.OTHER);
					break rtypeswitch;
				}
				newTicket.setStatus(Status.PENDING);
				newTicket.setAuthor(user);
				Timestamp instant = Timestamp.from(Instant.now());
				newTicket.setCreationDate(instant);
				System.out.println(newTicket);
				
				if(reimbService.insertReimb(newTicket)!=null) {
					resp.setStatus(201);
				}else {
					resp.setStatus(406);
					printWriter.print("<h1>Cound not create ticket</h1>");
				}
				
			}else if(req.getMethod().equals("PUT")) {
				
				resp.setContentType("application/json");
				resp.setStatus(406);
				BufferedReader reader = req.getReader();
				StringBuilder stringBuilder = new StringBuilder();
				String line = reader.readLine(); 
				
				while(line != null) {
					stringBuilder.append(line);
					line = reader.readLine(); 
				}
				
				String body = new String(stringBuilder);
				//System.out.println("body is"+body);
				int newStatus = Character.getNumericValue(body.charAt(body.length() - 2));
				body = body.substring(0, body.length() - 15)+" }";
				System.out.println("new body is"+body);
				
				Reimbursement newTicket= objectMapper.readValue(body,  Reimbursement.class);
				Reimbursement ticketToUpdate = reimbService.getReimbursementByID(newTicket.getId()).get();
				
				Reimbursement processedTicket = ticketToUpdate;
				statusSwitch:
				switch(newStatus) {
				case 1:
					processedTicket = reimbService.process(ticketToUpdate, Status.APPROVED, user);
					break statusSwitch;
				case 2:
					processedTicket = reimbService.process(ticketToUpdate, Status.DENIED, user);
					break statusSwitch;
				}
				
				Reimbursement updatedTicket = reimbService.update(processedTicket);
				
				System.out.println(updatedTicket);
				
				if(reimbService.update(updatedTicket)!=null) {
					resp.setStatus(201);
				}else {
					resp.setStatus(406);
					printWriter.print("<h1>Cound not update ticket</h1>");
				}
			}
			
			
		} else {
			resp.setStatus(401);
			printWriter.print("<h1>YOU ARE NOT LOGGED IN</h1>");
		}
		
			
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		doGet(req,resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		doGet(req,resp);
	}
	
	
}
