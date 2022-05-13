package com.revature.repositories;

import java.util.List;
import java.util.Optional;


import com.revature.models.User;

public interface UserDAO {

	public Optional<User> getByUsername(String username);
	public User create(User userToBeRegistered);
	public Optional<User> getByID(int user_ID);
	public List<User> getAll();
	public User createNewUser();
}
