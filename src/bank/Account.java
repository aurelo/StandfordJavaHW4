package bank;

public class Account implements Comparable<Account>{
	int id;
	int currentBalance;
	int numberOfTransactions;

    public Account(int accountId) {
    	id = accountId;
    	currentBalance = 1000;
    	numberOfTransactions = 0;
    }
    
    public int getId(){
    	return this.id;
    }
    
    public Integer getAccountNumber(){
    	return Integer.valueOf(getId());
    }
    
    public synchronized void deposit (int depositAmount){
    	this.currentBalance += depositAmount;
    	this.numberOfTransactions++;
    }
    
    public synchronized void withdrawal  (int withdrawalAmount){
    	this.currentBalance -= withdrawalAmount;
    	this.numberOfTransactions++;
    }

	@Override
	public String toString() {
		return "acct: " + id + " bal: " + currentBalance + " trans: " + numberOfTransactions;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		
		if (obj==null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		final Account accountObj = (Account) obj;
		
		return (id == accountObj.getId());
	}
	
	@Override
	public int compareTo(Account o) {
		return getAccountNumber().compareTo(o.getAccountNumber());
	}
}
