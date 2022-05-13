package com.revature.controllers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;



public class MenuController {

	private static Scanner scan = new Scanner(System.in);
	private AuthService authService = new AuthService();
	private ReimbursementService reimbService = new ReimbursementService();
	private UserService userService = new UserService();
	
	public void homeMenu() {
		
		printHomeMenu();
		String response = scan.nextLine();
		
		menuLoop:
		while(!response.equals("0")) {
			switchMenu:
			switch(response) {
			case "1": //Employee
				EmployeeMenu();
				break switchMenu;
			case "2": //Finance Manager
				ManagerMenu();
				break switchMenu;
			default:
				System.out.println("That is not a valid input");
				printHomeMenu();
				break switchMenu;
			}
		}
	}

	private void ManagerMenu() {
		
		User user = validateUser();
		if (user.getRole()== Role.FINANCE_MANAGER){
			System.out.println("Finance manager credentials confirmed");
		}
		printManagerMenu();
		String response = scan.nextLine();
		
		menuLoop:
		while(!response.equals("0")) {
			switchMenu:
			switch(response) {
			case "1"://View all reimbursements for all employees
				List<Reimbursement> list =reimbService.getAllReimbursements();
				for(Reimbursement rmb: list) {
					System.out.println(rmb);
				}
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
			case "2": //View reimbursements by status
				System.out.println("Select status to filter: \n"
						+ "1) PENDING \n"
						+ "2) APPROVED \n"
						+ "3) DENIED");
				String status = scan.nextLine();
				statusSwitch:
				switch(status){
				case "1":
					list = reimbService.getReimbursementsByStatus(Status.PENDING);
					for(Reimbursement rmb: list) {
						System.out.println(rmb);
					}
					break statusSwitch;
				case "2":
					list = reimbService.getReimbursementsByStatus(Status.APPROVED);
					for(Reimbursement rmb: list) {
						System.out.println(rmb);
					}
					break statusSwitch;
				case "3":
					list = reimbService.getReimbursementsByStatus(Status.DENIED);
					for(Reimbursement rmb: list) {
						System.out.println(rmb);
					}
					break statusSwitch;
				default:
					System.out.println("That is not a valid input");
					ManagerMenu();
					break statusSwitch;
				}
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
				
			case "3"://Approve/Deny reimbursements
				try {
					System.out.println("Enter id or reimbursement you want to process:");
					String reimb_idst = scan.nextLine();
					int reimb_id = Integer.parseInt(reimb_idst);
					if(reimbService.getReimbursementByID(reimb_id).isPresent()) {
						Reimbursement reimb = reimbService.getReimbursementByID(reimb_id).get();
						if(reimb.getStatus()==Status.PENDING) {
							System.out.println("This is the reimbursement to be processed:");
							System.out.println(reimb);
							System.out.println("Would you like to approve or deny it? \n"
									+ "1) Approve \n"
									+ "2) Deny ");
							String finalStatusstr = scan.nextLine();
							int finalStatus = Integer.parseInt(finalStatusstr);
							if(finalStatus==1 || finalStatus==2) {
								switchStatus:
								switch(finalStatus){
								case 1:
									reimbService.update(reimbService.process(reimb, Status.APPROVED, user));
									break switchStatus;
								case 2:
									reimbService.update(reimbService.process(reimb, Status.DENIED, user));
								}
							}else throw new RuntimeException("Not valid, please try again.");
						}else throw new RuntimeException("The reimbursement has already been processed.");
					}else throw new RuntimeException("The id provided is not valid.");
				}catch (RuntimeException e) {
					e.printStackTrace();
				}
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
			case "4"://Create new user
				User newUser = userService.createNewUser();
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
			case "5"://View all users
				List<User> ulist = userService.getAll();
				for(User u: ulist) {
					System.out.println(u);
				}
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
			case "6"://View user by user ID
				boolean validID =false;
				while(!validID) {
					try {
						System.out.println("What is the user's ID?");
						String user_idst = scan.nextLine();
						int user_id = Integer.parseInt(user_idst);
						Optional<User> idUser = userService.getByID(user_id);
						if(idUser.isPresent()) {
							System.out.println(idUser.get());
							validID = true;
						}else {
							throw new RuntimeException("ID not valid. Please try again.");
						}
					}catch (RuntimeException e){
						e.printStackTrace();
					}
				}
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
				
			case "7"://View user by username
				boolean validUsername =false;
				while(!validUsername) {
					try {
						System.out.println("What is the user's username?");
						String username = scan.nextLine();
						Optional<User> nameUser= userService.getByUsername(username);
						if(nameUser.isPresent()) {
							System.out.println(nameUser.get());
							validUsername = true;
						}else {
							throw new RuntimeException("Username not valid. Please try again.");
						}
					}catch (RuntimeException e){
						e.printStackTrace();
					}	
				}
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
			default:
				System.out.println("That is not a valid input");
				printManagerMenu();
				response = scan.nextLine();
				break switchMenu;
			}
		}
	}

	private void EmployeeMenu() {
		User user = validateUser();
		System.out.println(user.getId());
		printEmployeeMenu();
		String response = scan.nextLine();
		
		menuLoop:
		while(!response.equals("0")) {
			switchMenu:
			switch(response) {
			case "1"://View all tickets
				List<Reimbursement> list =reimbService.getAllReimbursements(user);
				for(Reimbursement rmb: list) {
					System.out.println(rmb);
				}
				printEmployeeMenu();
				response = scan.nextLine();
				break switchMenu;
			case "2"://View tickets by status
				System.out.println("Select status to filter: \n"
						+ "1) PENDING \n"
						+ "2) APPROVED \n"
						+ "3) DENIED");
				String status = scan.nextLine();
				statusSwitch:
				switch(status){
				case "1":
					list = reimbService.getReimbursementsByStatus(Status.PENDING,user);
					for(Reimbursement rmb: list) {
						System.out.println(rmb);
					}
					break statusSwitch;
				case "2":
					list = reimbService.getReimbursementsByStatus(Status.APPROVED,user);
					for(Reimbursement rmb: list) {
						System.out.println(rmb);
					}
					break statusSwitch;
				case "3":
					list = reimbService.getReimbursementsByStatus(Status.DENIED,user);
					for(Reimbursement rmb: list) {
						System.out.println(rmb);
					}
					break statusSwitch;
				default:
					System.out.println("That is not a valid input");
					ManagerMenu();
					break statusSwitch;
				}
				printEmployeeMenu();
				response = scan.nextLine();
				break switchMenu;
			case "3"://Add reimbursement request
				reimbService.createNewReimbursement(user);
				printEmployeeMenu();
				response = scan.nextLine();
				break switchMenu;
			default:
				System.out.println("That is not a valid input");
				printEmployeeMenu();
				response = scan.nextLine();
				break switchMenu;
			}
		}

		}
		
	



	private User validateUser() {
		System.out.println("Please enter your employee username:");
		String username = scan.nextLine();
		System.out.println("Please enter your password:");
		String password = scan.nextLine();
		User user = authService.login(username, password);
		System.out.println("Credentials confirmed \n");
		return user;
	}

	

	private void printManagerMenu() {
		System.out.println("\nWhat would you like to do? \n"
				+ "0) Go back to previous menu \n"
				+ "1) View all reimbursements for all employees \n"
				+ "2) View reimbursements by status \n"
				+ "3) Approve/Deny reimbursements \n"
				+ "4) Create new user \n"
				+ "5) View all users \n"
				+ "6) View user by user ID \n"
				+ "7) View user by username " );
		
	}

	private void printHomeMenu() {
		System.out.println("Welcome to the Employee Reimbursement System. \n"
				+ "Please select your role: \n"
				+ "1) Employee \n"
				+ "2) Finance Manager");	
	}
	
	private void printEmployeeMenu() {
		System.out.println("\nWhat would you like to do? \n"
				+ "0) Go back to previous menu \n"
				+ "1) View all tickets \n"
				+ "2) View tickets by status \n"
				+ "3) Add reimbursement request");
		}
		
		
	}

