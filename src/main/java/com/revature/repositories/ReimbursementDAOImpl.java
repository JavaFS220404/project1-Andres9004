package com.revature.repositories;


import com.revature.models.RType;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ReimbursementDAOImpl implements ReimbursementDAO {

	UserService userService = new UserService();
    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
	@Override
    public Optional<Reimbursement> getById(int id) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_reimbursement rmb "
					+ "JOIN ers_reimbursement_status rs ON rs.reimb_status_id = rmb.reimb_status_id_fk "
					+ "JOIN ers_reimbursement_type rt ON rt.reimb_type_id  = rmb.reimb_type_id_fk "
					+ "WHERE reimb_id = ? ;";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Reimbursement reimb = fillReimbData(result);
				return Optional.of(reimb);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
        return Optional.empty();
    }

    private Reimbursement fillReimbData(ResultSet result) {
    	
    	try {
    		Reimbursement reimb = new Reimbursement();
    		
    		reimb.setId(result.getInt("reimb_id"));
    		
    		Status status = Status.valueOf(result.getString("reimb_status"));				
    		reimb.setStatus(status);
    		
    		User author = userService.getByID(result.getInt("reimb_author_fk")).get();
    		reimb.setAuthor(author);
    		
    		if(result.getInt("reimb_resolver_fk")!=0) {
    			User resolver = userService.getByID(result.getInt("reimb_resolver_fk")).get();
    			reimb.setResolver(resolver);
    		}
    		
    		reimb.setAmount(result.getDouble("reimb_amount"));
    		reimb.setDescription(result.getString("reimb_description"));
    		reimb.setCreationDate(result.getTimestamp("reimb_creation_date"));
    		reimb.setResolutionDate(result.getTimestamp("reimb_resolution_date"));
    		reimb.setReceiptImage(result.getString("reimb_receipt"));
    		
    		RType reimbType = RType.valueOf(result.getString("reimb_type"));	
    		reimb.setReimbType(reimbType);
    		
    		return reimb;
    	}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
	@Override
    public List<Reimbursement> getByStatus(Status status) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_reimbursement rmb "
					+ "JOIN ers_reimbursement_status rs ON rs.reimb_status_id = rmb.reimb_status_id_fk "
					+ "JOIN ers_reimbursement_type rt ON rt.reimb_type_id  = rmb.reimb_type_id_fk "
					+ "WHERE reimb_status = ? "
					+ "ORDER BY reimb_id; ";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, status.toString());
			ResultSet result = statement.executeQuery();
			
			List<Reimbursement> list = new ArrayList<>();
			
			while(result.next()) {
				Reimbursement reimb = fillReimbData(result);
				list.add(reimb);
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}
        return Collections.emptyList();
    }
	
	@Override
    public List<Reimbursement> getByStatus(Status status, User user) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_reimbursement rmb "
					+ "JOIN ers_reimbursement_status rs ON rs.reimb_status_id = rmb.reimb_status_id_fk "
					+ "JOIN ers_reimbursement_type rt ON rt.reimb_type_id  = rmb.reimb_type_id_fk "
					+ "WHERE reimb_status = ? AND reimb_author_fk =? "
					+ "ORDER BY reimb_id; ";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, status.toString());
			statement.setInt(2, user.getId());
			ResultSet result = statement.executeQuery();
			
			List<Reimbursement> list = new ArrayList<>();
			
			while(result.next()) {
				Reimbursement reimb = fillReimbData(result);
				list.add(reimb);
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}
        return Collections.emptyList();
    }

	
	@Override
	public void createNewReimbursement(User user) {
		Scanner scan = new Scanner(System.in);
		Reimbursement reimb = new Reimbursement();
		reimb.setAuthor(user);
		reimb.setStatus(Status.PENDING);
		
		try {
			boolean validnum=false;
			while(!validnum) {
				System.out.println("Please select reimbursement type: \n"
						+ "1) LODGING \n"
						+ "2) TRAVEL \n"
						+ "3) FOOD \n"
						+ "4) OTHER");
				String response = scan.nextLine();
				int type_id = Integer.parseInt(response);
				if (Arrays.asList(1,2,3,4).contains(type_id)) {
					rtypeswitch:
					switch(type_id) {
					case 1:
						reimb.setReimbType(RType.LODGING);
						validnum=true;
						break rtypeswitch;
					case 2:
						reimb.setReimbType(RType.TRAVEL);
						validnum=true;
						break rtypeswitch;
					case 3:
						reimb.setReimbType(RType.FOOD);
						validnum=true;
						break rtypeswitch;
					case 4:
						reimb.setReimbType(RType.OTHER);
						validnum=true;
						break rtypeswitch;
					}
					
				}else throw new RuntimeException("Reimbursement type not valid. Please try again.");
			}
			
			System.out.println("Please enter reimbursement amount:");
			String response = scan.nextLine();
			double amount =Double.parseDouble(response); 
			reimb.setAmount(amount);
			
			System.out.println("Please enter reimbursement description:");
			String desc = scan.nextLine();
			reimb.setDescription(desc);
			
			Timestamp instant = Timestamp.from(Instant.now());
			reimb.setCreationDate(instant);
			System.out.println("Creating reimbursement.");
			Reimbursement createdReimb = insertReimb(reimb);
			System.out.println(createdReimb);
		}catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
    public Reimbursement insertReimb(Reimbursement reimb) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			
			String sql = "INSERT INTO ers_reimbursement "
					+ "(reimb_status_id_fk, "
					+ "reimb_author_fk, "
					//+ "reimb_resolver_fk, "
					+ "reimb_amount, "
					+ "reimb_description, "
					+ "reimb_creation_date, "
					//+ "reimb_resolution_date, "
					+ "reimb_receipt, "
					+ "reimb_type_id_fk) VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?) ;"; 
			
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		int count = 0;
			statement.setInt(++count,reimb.getStatus().ordinal()+1);
			statement.setInt(++count,reimb.getAuthor().getId());
			//statement.setString(++count,null);//reimb.getResolver().getId());
			statement.setDouble(++count,reimb.getAmount());
			statement.setString(++count,reimb.getDescription());
			statement.setTimestamp(++count,reimb.getCreationDate());
			//statement.setTimestamp(++count,reimb.getResolutionDate());
			statement.setString(++count,reimb.getReceiptImage());
			statement.setInt(++count,reimb.getReimbType().ordinal()+1);
			
    		statement.execute();
    		
    		sql = "SELECT * FROM ers_reimbursement rmb \n"
    				+ "JOIN ers_reimbursement_status rs ON rs.reimb_status_id = rmb.reimb_status_id_fk \n"
    				+ "JOIN ers_reimbursement_type rt ON rt.reimb_type_id  = rmb.reimb_type_id_fk \n"
    				+ "WHERE reimb_author_fk =? AND rs.reimb_status = 'PENDING'\n"
    				+ "ORDER BY reimb_id DESC\n"
    				+ "LIMIT 1;";
    		statement = conn.prepareStatement(sql);
			statement.setInt(1, reimb.getAuthor().getId());
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				Reimbursement createdReimb = fillReimbData(result);
				System.out.println("Reimbursement created successfully.");
    			return createdReimb;
			}else throw new RuntimeException("Reimbursement update unsuccessful");
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(RuntimeException e) {
			e.printStackTrace();
		}
		
		return null;
    }
	
	
	
	
	
    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
	@Override
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			
			String sql = "UPDATE ers_reimbursement "
					+ "SET reimb_status_id_fk=?, "
					//+ "reimb_author_fk=?, "
					+ "reimb_resolver_fk=?,"
					+ "reimb_amount=?, "
					+ "reimb_description=?,"
					+ "reimb_creation_date=?,"
					+ "reimb_resolution_date=?, "
					+ "reimb_receipt=?, "
					+ "reimb_type_id_fk=? "
					+ "WHERE reimb_id = ? ;"; 
			
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		int count = 0;
			statement.setInt(++count,unprocessedReimbursement.getStatus().ordinal()+1);
			//statement.setInt(++count,unprocessedReimbursement.getAuthor().getId());
			statement.setInt(++count,unprocessedReimbursement.getResolver().getId());
			statement.setDouble(++count,unprocessedReimbursement.getAmount());
			statement.setString(++count,unprocessedReimbursement.getDescription());
			statement.setTimestamp(++count,unprocessedReimbursement.getCreationDate());
			statement.setTimestamp(++count,unprocessedReimbursement.getResolutionDate());
			statement.setString(++count,unprocessedReimbursement.getReceiptImage());
			statement.setInt(++count,unprocessedReimbursement.getReimbType().ordinal()+1);
			statement.setInt(++count,unprocessedReimbursement.getId());
			
    		statement.execute();
    		
    		Optional<Reimbursement> updatedReimb = getById(unprocessedReimbursement.getId());
    		if(updatedReimb.isPresent() && updatedReimb.get().getStatus().equals(unprocessedReimbursement.getStatus())){
    			System.out.println("Reimbursement updated successfully:");
    			System.out.println(updatedReimb.get());
    			return updatedReimb.get();
    		}else throw new RuntimeException("Reimbursement update unsuccessful");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(RuntimeException e) {
			e.printStackTrace();
		}
		
		return null;
    }

	@Override
	public List<Reimbursement> findAll() {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_reimbursement rmb "
					+ "JOIN ers_reimbursement_status rs ON rs.reimb_status_id = rmb.reimb_status_id_fk "
					+ "JOIN ers_reimbursement_type rt ON rt.reimb_type_id  = rmb.reimb_type_id_fk "
					+ "ORDER BY reimb_id; "; 
			
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			List<Reimbursement> list = new ArrayList<>();
			while(result.next()) {
				Reimbursement reimb = fillReimbData(result);
				list.add(reimb);
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Reimbursement> findAll(User user) {
		try(Connection conn = ConnectionFactory.getInstance().getConnection()){
			String sql = "SELECT * FROM ers_reimbursement rmb "
					+ "JOIN ers_reimbursement_status rs ON rs.reimb_status_id = rmb.reimb_status_id_fk "
					+ "JOIN ers_reimbursement_type rt ON rt.reimb_type_id  = rmb.reimb_type_id_fk "
					+ "WHERE reimb_author_fk = ? "
					+ "ORDER BY reimb_id; "; 
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, user.getId());
			ResultSet result = statement.executeQuery();
			
			List<Reimbursement> list = new ArrayList<>();
			while(result.next()) {
				Reimbursement reimb = fillReimbData(result);
				list.add(reimb);
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	
	
	@Override
	public Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, User resolver) {
		//Check resolver is finance manager
		try {
			if(resolver.getRole().equals(Role.FINANCE_MANAGER)) {
				unprocessedReimbursement.setStatus(finalStatus);
				Timestamp instant= Timestamp.from(Instant.now());  
				unprocessedReimbursement.setResolutionDate(instant);
				unprocessedReimbursement.setResolver(resolver);
				return unprocessedReimbursement;
			}else {
				throw new RuntimeException("Resolver does not have permission to process ticket.");
			}
		}catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
