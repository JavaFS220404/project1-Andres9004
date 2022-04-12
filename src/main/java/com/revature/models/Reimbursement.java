package com.revature.models;

/**
 * This concrete Reimbursement class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 *     <li>Description</li>
 *     <li>Creation Date</li>
 *     <li>Resolution Date</li>
 *     <li>Receipt Image</li>
 * </ul>
 *
 */
public class Reimbursement extends AbstractReimbursement {

	private String description; 
    private String creationdate;
    private String resolutiondate;
    //receiptimage
    
	public Reimbursement() {
        super();
    }

    /**
     * This includes the minimum parameters needed for the {@link com.revature.models.AbstractReimbursement} class.
     * If other fields are needed, please create additional constructors.
     */
    public Reimbursement(int id, Status status, User author, User resolver, double amount) {
        super(id, status, author, resolver, amount);
    }
    //mycode
    public Reimbursement(int id, Status status, User author, User resolver, double amount,
    		String description, String creationdate, String resolutiondate) {
        super(id, status, author, resolver, amount);
        this.description = description;
        this.creationdate = creationdate;
        this.resolutiondate = resolutiondate;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCreationDate() {
        return creationdate;
    }
    public void setCreationDate(String creationdate) {
        this.creationdate = creationdate;
    }
    public String getResolutionDate() {
        return resolutiondate;
    }
    public void setResolutionDate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }
    
    
    
}
