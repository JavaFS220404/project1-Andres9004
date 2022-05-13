package com.revature.models;

import java.sql.Timestamp;
import java.util.Objects;

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

	private RType reimbType;
	private String description; 
    private Timestamp creationDate;
    private Timestamp resolutionDate;
    private String receiptImage;
    
    
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
    
	public Reimbursement(String description, Timestamp creationDate, Timestamp resolutionDate, String receiptImage,
			RType reimbType) {
		super();
		this.reimbType = reimbType;
		this.description = description;
		this.creationDate = creationDate;
		this.resolutionDate = resolutionDate;
		this.receiptImage = receiptImage;
		
	}
    
    public Reimbursement(int id, Status status, User author, User resolver, double amount,
    		String description, Timestamp creationDate, Timestamp resolutionDate, String receiptImage,
			RType reimbType) {
        super(id, status, author, resolver, amount);
        this.reimbType = reimbType;
        this.description = description;
		this.creationDate = creationDate;
		this.resolutionDate = resolutionDate;
		this.receiptImage = receiptImage;
		
    }
    
    public RType getReimbType() {
		return reimbType;
	}

	public void setReimbType(RType reimbType) {
		this.reimbType = reimbType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Timestamp instant) {
		this.resolutionDate = instant;
	}

	public String getReceiptImage() {
		return receiptImage;
	}

	public void setReceiptImage(String receiptImage) {
		this.receiptImage = receiptImage;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(reimbType, creationDate, description, receiptImage, resolutionDate);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		return Objects.equals(creationDate, other.creationDate) && Objects.equals(description, other.description)
				&& Objects.equals(receiptImage, other.receiptImage) && Objects.equals(reimbType, other.reimbType)
				&& Objects.equals(resolutionDate, other.resolutionDate);
	}

	@Override
	public String toString() {
		return "Reimbursement ["+"id="+this.getId() + ", status=" +this.getStatus()
				//+ String.valueOf(this.getAuthor().getId())
				//+ ", resolver_id=" + String.valueOf(this.getResolver().getId())
				+ ", amount=" + this.getAmount()
				+ ", reimbType=" + reimbType
				+", description=" + description + ", creationDate=" + creationDate + ", resolutionDate="
				+ resolutionDate + ", receiptImage=" + receiptImage + "]";
	}
    
	
    
    
    
}
