package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;

public interface ReimbursementDAO {

	public Optional<Reimbursement> getById(int id);
	public List<Reimbursement> getByStatus(Status status);
	public List<Reimbursement> getByStatus(Status status, User user);
	public Reimbursement update(Reimbursement unprocessedReimbursement);
	public List<Reimbursement> findAll();
	public List<Reimbursement> findAll(User user);
	public Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, User resolver);
	public void createNewReimbursement(User user);
	public Reimbursement insertReimb(Reimbursement reimb);
	
	
}
