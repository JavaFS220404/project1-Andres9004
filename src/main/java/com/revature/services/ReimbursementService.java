package com.revature.services;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import com.revature.repositories.ReimbursementDAOImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The ReimbursementService should handle the submission, processing,
 * and retrieval of Reimbursements for the ERS application.
 *
 * {@code process} and {@code getReimbursementsByStatus} are the minimum methods required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create Reimbursement</li>
 *     <li>Update Reimbursement</li>
 *     <li>Get Reimbursements by ID</li>
 *     <li>Get Reimbursements by Author</li>
 *     <li>Get Reimbursements by Resolver</li>
 *     <li>Get All Reimbursements</li>
 * </ul>
 */
public class ReimbursementService {

	
	protected ReimbursementDAO reimbDAO = new ReimbursementDAOImpl();
	
	
	/*public List<Avenger> avengerAssemble(){
		return avengerDao.findAll();
	}*/
	
	public List<Reimbursement> getAllReimbursements(){
		return reimbDAO.findAll();
	}
	
	public List<Reimbursement> getAllReimbursements(User user){
		return reimbDAO.findAll(user);
	}
	

	
	public Optional<Reimbursement> getReimbursementByID(int id) {
        return reimbDAO.getById(id);
    }
	
    /**
     * <ul>
     *     <li>Should ensure that the user is logged in as a Finance Manager</li>
     *     <li>Must throw exception if user is not logged in as a Finance Manager</li>
     *     <li>Should ensure that the reimbursement request exists</li>
     *     <li>Must throw exception if the reimbursement request is not found</li>
     *     <li>Should persist the updated reimbursement status with resolver information</li>
     *     <li>Must throw exception if persistence is unsuccessful</li>
     * </ul>
     *
     * Note: unprocessedReimbursement will have a status of PENDING, a non-zero ID and amount, and a non-null Author.
     * The Resolver should be null. Additional fields may be null.
     * After processing, the reimbursement will have its status changed to either APPROVED or DENIED.
     */
    public Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, User resolver) {
        return reimbDAO.process(unprocessedReimbursement, finalStatus, resolver);
    }

    public void createNewReimbursement(User user) {
    	reimbDAO.createNewReimbursement(user);
    }
    
    public Reimbursement insertReimb(Reimbursement reimb) {
    	return reimbDAO.insertReimb(reimb);
    }
    
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
    	return reimbDAO.update(unprocessedReimbursement);
    }
    
    /**
     * Should retrieve all reimbursements with the correct status.
     */
    public List<Reimbursement> getReimbursementsByStatus(Status status) {
        return reimbDAO.getByStatus(status);
    }
    
    public List<Reimbursement> getReimbursementsByStatus(Status status, User user) {
        return reimbDAO.getByStatus(status, user);
    }
    
}
