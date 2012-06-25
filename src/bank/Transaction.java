package bank;

import java.util.Scanner;

public class Transaction {

	Account fromAccount;
	Account toAccount;
	int amount;
	
	
	public Transaction(Account from, Account to, int amount) {
		fromAccount = from;
		toAccount = to;
		this.amount = amount;
	}


	public Account getFromAccount() {
		return fromAccount;
	}

	public Account getToAccount() {
		return toAccount;
	}


	public int getAmount() {
		return amount;
	}
	
	public static final Transaction nullTransaction(){
		return new Transaction(new Account(-1), new Account(0), 0);
	}
	
	public static final Transaction transactionFromLine(String line){
		Scanner s = new Scanner(line);
		int parsedInt;
		int fromAccount = -1;
		int toAccount = 0;
		int amount = 0;
		for (int i = 0; s.hasNext() && i < 3; i++) {
			parsedInt = s.nextInt();
			switch (i) {
			case 0: fromAccount = parsedInt;
			        break;
			case 1: toAccount = parsedInt;
			        break;
			case 2: amount = parsedInt;
			        break;
			};
		}
		
		if (fromAccount == -1) {
			return Transaction.nullTransaction();
		}
		else {			
			return new Transaction(new Account(fromAccount), new Account(toAccount), amount);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		
		if (obj==null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		final Transaction transactionObj = (Transaction) obj;
		
		return (fromAccount.equals(transactionObj.getFromAccount()) && toAccount.equals(transactionObj.getToAccount()) && amount == transactionObj.getAmount());
	}
	
	@Override
	public String toString() {
		return "from account: " + getFromAccount() + " to account: "+ getToAccount() + " amount: "+ getAmount();
	}


}
