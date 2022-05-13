package com.revature.services;

import java.util.List;
import java.util.Optional;

import com.revature.models.User;
import com.revature.repositories.UserDAO;
import com.revature.repositories.UserDAOImpl;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 *
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {

	protected UserDAO userDAO = new UserDAOImpl();
	
	
	/**
	 *     Should retrieve a User with the corresponding username or an empty optional if there is no match.
     */
	public Optional<User> getByUsername(String username) {
		return userDAO.getByUsername(username);
	}
	
	public User create(User userToBeRegistered) {
		return userDAO.create(userToBeRegistered);
	}
	
	public Optional<User> getByID(int user_ID) {
		return userDAO.getByID(user_ID);
	}
	
	public List<User> getAll(){
		return userDAO.getAll();
	}
	
	public User createNewUser() {
		return userDAO.createNewUser();
	}
	
	
}
