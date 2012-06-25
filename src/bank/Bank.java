package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	
	private BlockingQueue<Transaction> transactions;
	private Map<Integer, Account> accounts;
	private final CountDownLatch stopLatch;
	
    class Worker implements Runnable {
		
		private boolean workDone;
		
		public Worker() {
			this.workDone = false;
		}
		
		private void processTransactions(Transaction t){
			//System.out.println(Thread.currentThread().getName() + " handling transaction: "+ t);
			if (t.equals(Transaction.nullTransaction())) {
				workDone = true;
			}
			else{
			   t.fromAccount.withdrawal(t.amount);
			   t.toAccount.deposit(t.amount);
			}
		}
		
		@Override
		public void run() {
			
			try {
				while (!workDone) {
					processTransactions(transactions.take());
				}
				
				stopLatch.countDown();
			}
			catch (InterruptedException e){
				System.err.println(e.getLocalizedMessage());
			}
			
		}
	}
	
	
	public Bank(int capacity) {
		transactions = new ArrayBlockingQueue<Transaction>(capacity);
		accounts = new  TreeMap<Integer, Account>();
		stopLatch = new CountDownLatch(capacity);
	}
	
	public void createWorkers(int numberOfWorkers){
	    for(int i = 0; i < numberOfWorkers; i++){
	    	new Thread(new Worker(), "worker"+i).start();
	    }
	}
	
	private void addAccount(Account account) {
		accounts.put(account.getAccountNumber(), account);
	}
	
	public void produceTransaction(Transaction t) throws InterruptedException{
		
		Account fromAcct;
		Account toAcct;
		
		if (!t.equals(Transaction.nullTransaction())){
			
			if (accounts.containsKey(t.getFromAccount().getAccountNumber())){
				fromAcct = accounts.get(t.getFromAccount().getAccountNumber());
			}
			else {
				fromAcct = t.fromAccount;
				addAccount(fromAcct);
			}
			
			if (accounts.containsKey(t.toAccount.getAccountNumber())){
				toAcct = accounts.get(t.toAccount.getAccountNumber());
			}
			else {
				toAcct = t.toAccount;
				addAccount(toAcct);
			}
			
			this.transactions.put(new Transaction(fromAcct, toAcct, t.getAmount()));
			
		}
		else this.transactions.put(t);
	}
	
	public void printAccountInfo(){
		for (Account a: accounts.values()){
			System.out.println(a.toString());
		}
	}
	
	public void waitForWorkersToFinish(){
		try {			
			stopLatch.await();
		}
		catch (InterruptedException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numberOfWorkers = 0;
		String fileName;
		BufferedReader reader;
		
		Bank bank;
		
		try {
			fileName = args[0];
		    numberOfWorkers = Integer.parseInt(args[1]);
		    
		    bank = new Bank(numberOfWorkers);
		    
		    //create workers
		    bank.createWorkers(numberOfWorkers);
		    
		    String line;
		    reader = new BufferedReader(new FileReader(fileName));
		    
		    while ((line = reader.readLine()) != null) {
		    	//System.out.println(line);
		    	try {		    		
		    		bank.produceTransaction(Transaction.transactionFromLine(line));
		    	}
		    	catch (InterruptedException e) {
		    		System.err.println(e.toString());
		    	}
		    	
		    }
		    
		    for (int i = 0; i < numberOfWorkers; i++){
		    	try {		    		
		    		bank.produceTransaction(Transaction.nullTransaction());
		    	}
		    	catch (InterruptedException e) {
		    		System.err.println(e.toString());
		    	}
		    }
		    
		    bank.waitForWorkersToFinish();
		    bank.printAccountInfo();
		    
		    
		}
		catch (NumberFormatException e) {
            System.err.println("Argument must be an integer.");
            System.exit(1);
        }
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		catch (Exception e) {
			System.err.println("Please enter valid text file and number of workers: " + e.getLocalizedMessage());
			System.exit(1);
		}
		finally {
			//if (reader != null) reader.close();
		}
		
	}

}
