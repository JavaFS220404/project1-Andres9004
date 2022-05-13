package com.revature.repositories;


import com.revature.exceptions.NewUserHasNonZeroIdException;
import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserDAOImpl implements UserDAO {


	protected static Scanner scan = new Scanner(System.in);
	
	
    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
	@Override
    public Optional<User> getByUsername(String username) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_users usr "
					+ "JOIN ers_user_roles eur ON eur.user_role_id = usr.user_role_id_fk "
					+ "WHERE ers_username = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				User user = fillUserData(result);
				return Optional.of(user);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
        return Optional.empty();
    }



	/**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     *
     * Note: The userToBeRegistered will have an id=0, and username and password will not be null.
     * Additional fields may be null.
     */
	@Override
    public User create(User userToBeRegistered) {
		
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "INSERT INTO ers_users(ers_username, ers_password, "
					+"user_role_id_fk, user_first_name,  "
					+"user_last_name, user_email, user_phone_number, user_address) "
					+"VALUES (?,?,?,?,?,?,?,?)";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			if(userToBeRegistered.getId()!=0) {
				throw new NewUserHasNonZeroIdException();
			}else if(getByUsername(userToBeRegistered.getUsername()).isPresent()){
				throw new UsernameNotUniqueException();
			}else {
		
				int count = 0;
				statement.setString(++count,userToBeRegistered.getUsername()); 
				statement.setString(++count,userToBeRegistered.getPassword());
				int role_id = 0;
				switch(userToBeRegistered.getRole()) {
				case EMPLOYEE:
					role_id = 1;
					break;
				case FINANCE_MANAGER:
					role_id = 2;
					break;
				}
				statement.setInt(++count,role_id);
				statement.setString(++count,userToBeRegistered.getFirstName());
				statement.setString(++count,userToBeRegistered.getLastName());
				statement.setString(++count,userToBeRegistered.getEmail());
				statement.setString(++count,userToBeRegistered.getPhoneNumber());
				statement.setString(++count,userToBeRegistered.getAddress());
				
				statement.execute();
				
				Optional<User> registeredUser = getByUsername(userToBeRegistered.getUsername());
				
				if(registeredUser.isPresent()) {
					return registeredUser.get();
				}else throw new RegistrationUnsuccessfulException();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(NewUserHasNonZeroIdException e) {
			e.printStackTrace();
		}catch(UsernameNotUniqueException e) {
			e.printStackTrace();
		}
		return userToBeRegistered;
		
    }
	
	@Override
	public User createNewUser() {
		
		User userToBeRegistered = new User();
		userToBeRegistered.setId(0);
		
		System.out.println("What is the new user's username?");
		String response = scan.nextLine();
		
		while(getByUsername(response).isPresent() || response==null) {
			try{
				if(getByUsername(response).isPresent()) {
					throw new UsernameNotUniqueException("Username not valid, please choose another one.");
				}
			}catch (UsernameNotUniqueException e) {
				e.printStackTrace();
				System.out.println("What is the new user's username?");
				response = scan.nextLine();
			}
		}
		userToBeRegistered.setUsername(response);
		
		
		boolean validPwd = false;
		while(!validPwd) {
			try{
				System.out.println("What is the new user's password?");
				response = scan.nextLine();
				if(!response.isEmpty()) {
					userToBeRegistered.setPassword(response);
					validPwd =true;
				}else {
					throw new RuntimeException("Password not valid. Please try again");
				}	
			}catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		
		
		printCreateUserMenu();
		response = scan.nextLine();
		
		menuLoop:
		while(!response.equals("0")) {
			switchMenu:
			switch(response) {
			case "1":
				System.out.println("What is the new user's role? \n"
						+ "1) EMPLOYEE \n"
						+ "2) FINANCE MANAGER");
				response = scan.nextLine();
				roleSwitch:
				switch(response) {
				case "1":
					userToBeRegistered.setRole(Role.EMPLOYEE);
					break roleSwitch;
				case "2":
					userToBeRegistered.setRole(Role.FINANCE_MANAGER);
					break roleSwitch;
				}
				printCreateUserMenu();
				response = scan.nextLine();
				break switchMenu;
			case "2":
				System.out.println("What is the new user's first name?");
				response = scan.nextLine();
				userToBeRegistered.setFirstName(response);
				printCreateUserMenu();
				response = scan.nextLine();
				break switchMenu;
			case "3":
				System.out.println("What is the new user's last name?");
				response = scan.nextLine();
				userToBeRegistered.setLastName(response);
				printCreateUserMenu();
				response = scan.nextLine();
				break switchMenu;
			case "4":
				System.out.println("What is the new user's email?");
				response = scan.nextLine();
				userToBeRegistered.setEmail(response);
				printCreateUserMenu();
				response = scan.nextLine();
				break switchMenu;
			case "5":
				System.out.println("What is the new user's phone number?");
				response = scan.nextLine();
				userToBeRegistered.setPhoneNumber(response);
				printCreateUserMenu();
				response = scan.nextLine();
				break switchMenu;
			case "6":
				System.out.println("What is the new user's address?");
				response = scan.nextLine();
				userToBeRegistered.setAddress(response);
				printCreateUserMenu();
				response = scan.nextLine();
				break switchMenu;
			default:
				System.out.println("That is not a valid input");
				printCreateUserMenu();
				response = scan.nextLine();
			}
		
		
			if (userToBeRegistered.getUsername() == null || userToBeRegistered.getPassword() == null){
		        System.out.println("Please add user's username and/or password to continue");
		        printCreateUserMenu();
				response = scan.nextLine();
		    }
		}
		User registeredUser = create(userToBeRegistered);
		System.out.println("User added successfully:");
		System.out.println(registeredUser);
		return registeredUser;
		
	}
	

	
    private void printCreateUserMenu() {
		System.out.println("Please select the information you wish to add:\n"
				+ "0) Finish user creation \n"
				+ "1) Role \n"
				+ "2) First Name \n"
				+ "3) Last Name \n"
				+ "4) Email \n"
				+ "5) Phone Number \n"
				+ "6) Address \n");
	}



	private User fillUserData(ResultSet result) {
    	try {
    		User user = new User();
    		
    		user.setId(result.getInt("ers_users_id"));
    		
    		user.setUsername(result.getString("ers_username"));
    		
    		user.setPassword(result.getString("ers_password"));
    		
    		String rolestr = result.getString("user_role");
    		user.setRole(Role.valueOf(rolestr));
    		
    		user.setFirstName(result.getString("user_first_name"));
    		
    		user.setLastName(result.getString("user_last_name"));
    		
    		user.setEmail(result.getString("user_email"));
    		
    		user.setPhoneNumber(result.getString("user_phone_number"));
    		
    		user.setAddress(result.getString("user_address"));
    		
    		return user;
    	}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public Optional<User> getByID(int user_ID) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_users usr "
					+ "JOIN ers_user_roles eur ON eur.user_role_id = usr.user_role_id_fk "
					+ "WHERE ers_users_id = ?;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, user_ID);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				User user = fillUserData(result);
				return Optional.of(user);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
        return Optional.empty();
	}
	
	@Override
	public List<User> getAll() {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_users usr "
					+ "JOIN ers_user_roles eur ON eur.user_role_id = usr.user_role_id_fk  "
					+ "ORDER BY ers_users_id; ";
			
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			List<User> list = new ArrayList<>();
			while(result.next()) {
				User user = fillUserData(result);
				list.add(user);
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
