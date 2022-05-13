package com.revature;

import java.util.List;

import com.revature.controllers.MenuController;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;


public class Driver {

    public static void main(String[] args) {
    	
  
    	new MenuController().homeMenu();
    }
}
