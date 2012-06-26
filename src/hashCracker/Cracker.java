package hashCracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	
	private static final int MAX_THREADS = 40;
	
	private final CountDownLatch stopLatch;
	private final int originalWordLength;
	private final String hashedString;
	
	
	public Cracker(String hexString, int originalWordLength, CountDownLatch stopLatch) {
		this.hashedString = hexString;
		this.originalWordLength = originalWordLength;
		this.stopLatch = stopLatch;
		
		System.out.println("Hashed String: "+hashedString);
		System.out.println("original word length: " + originalWordLength);
	}
	
	public void createThreads(int count){
		int threadCount = Math.min(count, MAX_THREADS);
		int fromIndex = 0;
		int toIndex = 0;
		char[] firstLetterPossibleValues;
		
		for (int i = 0; i < threadCount; i++){
			fromIndex = i * CHARS.length/threadCount;
			toIndex = (i + 1)*CHARS.length/threadCount;
			firstLetterPossibleValues = Arrays.copyOfRange(CHARS, fromIndex , toIndex);
			new Thread(new CrackerWorker(firstLetterPossibleValues), "Thread"+i).start();
		}
	}		
	
	class CrackerWorker implements Runnable {
		
		private final char[] firstLetterWordsToCheck;
		private String wordWithTheSameHash;
		
		public CrackerWorker(char[] partToCheck) {
			this.firstLetterWordsToCheck = partToCheck;
		}
		
		@Override
		public void run() {
			Permutation perm = new Permutation(originalWordLength, hashedString, firstLetterWordsToCheck);
			wordWithTheSameHash = perm.permute();
			
			if (wordWithTheSameHash != null) System.out.println("WORD with the SAME HASH: "+ wordWithTheSameHash);
			
			System.out.println("Thread "+ Thread.currentThread().getName() + " done!");
			stopLatch.countDown();	
			}
	}
	
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	public static String getHashValue(String input) {
		String hexString = null;
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA");
			algorithm.reset();
			algorithm.update(input.getBytes());
			byte messageDigest[] = algorithm.digest();
		    hexString = Cracker.hexToString(messageDigest);
		}
		catch (NoSuchAlgorithmException e) {
			System.err.println(e.toString());
			//System.exit(1);
		}
		
		return hexString;
		
	}
	
	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
	
	public static void main(String[] args) {
		if (args.length == 1){
			//hash generation
			System.out.println(Cracker.getHashValue(args[0]));			
		}
		else if (args.length == 3) {
			CountDownLatch cdl = new CountDownLatch(Integer.parseInt(args[2]));
			Cracker cracker = new Cracker(args[0], Integer.parseInt(args[1]), cdl);
			
			cracker.createThreads(Integer.parseInt(args[2]));
			
			try{
				cdl.await();		
				System.out.println("All threads done!");
			}
			catch (InterruptedException e) {
				System.err.println(e.toString());
			}
			
		}
		else {
			System.out.println("Invalid number of arguments, for hash generation use 1, for hash cracking use 3!");
			System.exit(1);
		}
			
			
	}

}
