package hashCracker;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
	
	class PermutationElement {
		private int currentIndex;
		private char[] possibleLetters;
		
		public PermutationElement(char[] possibleLetterValues) {
			this.possibleLetters = possibleLetterValues;
			currentIndex = 0;
		}
		
		public char resetLetter(){
			currentIndex = 0;
			return possibleLetters[currentIndex];
		}
		
		public char currentElement(){
			return possibleLetters[currentIndex];
		}
		
		public char nextElement(){
			char element;
			if (isOnEnd()){
				currentIndex = 0;
				element = possibleLetters[currentIndex];
			}
			else {
				currentIndex++;
				element = possibleLetters[currentIndex];
			}
			return element;
		}
		
		public boolean isOnEnd(){
			return (currentIndex == possibleLetters.length - 1);
		}
		
	}
	
	private List<PermutationElement> letterList;
	private String hashedWord;
	
	public Permutation(int wordLength, String hashedWord) {
		letterList = new ArrayList<PermutationElement>(wordLength);
		this.hashedWord = hashedWord;
		
		for(int i = 0; i < wordLength; i++){
			letterList.add(i, new PermutationElement(Cracker.CHARS));
		}
	}
	
	public Permutation(int wordLength, String hashedWord, char[] firstLetterPossibleValues) {
		letterList = new ArrayList<PermutationElement>(wordLength);
		this.hashedWord = hashedWord;
		
		letterList.add(0, new PermutationElement(firstLetterPossibleValues));
		
		for(int i = 1; i < wordLength; i++){
			letterList.add(i, new PermutationElement(Cracker.CHARS));
		}
	}
	
	private String getFirstWord(){
		StringBuilder firstWord = new StringBuilder();
		
		for (int i = 0; i < letterList.size(); i++){
			firstWord.append(letterList.get(i).resetLetter());
		}
		
		return firstWord.toString();
	}
	
	private String getCurrentWord(){
		StringBuilder currentWord = new StringBuilder();
		
		for (int i = 0; i < letterList.size(); i++){
			currentWord.append(letterList.get(i).currentElement());
		}
		
		return currentWord.toString();
	}
	
	private String getNextWord(){
		
		if (!isLastWord())
			//if we are not dealing with last word, move the char elements to next word
		{
			
			if (letterList.get(0).isOnEnd()) {
				
				int j = 0;
				
				while(j < letterList.size() - 1 && letterList.get(j).isOnEnd()){
					
					letterList.get(j).resetLetter();
					
					// move next element to next value 
					if (j + 1 < letterList.size()) {
						letterList.get(j + 1).nextElement();
					}
					
					j++;
				}
			}
			else {
				letterList.get(0).nextElement();
			}
			
		}
				
		return getCurrentWord();
		
	}
	
	private boolean isLastWord(){
		// when last letter get's to it's last possible value we are on end
		return letterList.get(letterList.size() - 1).isOnEnd();
	}
	
	public String permute (){		
		boolean letterFound = false;
		boolean lastWord = false;
		
		String wordToCompare = getFirstWord();
		String letterWithSameHash = null;
		
		do {
			
		   //System.out.println("Comparing word: "+ wordToCompare);
			
		   if (Cracker.getHashValue(wordToCompare).equals(hashedWord)){
			   //System.out.println("FOUND LETTER: " + wordToCompare);
			   letterWithSameHash = wordToCompare;
			   letterFound = true;   
		   }
		   else if (isLastWord()){
			   lastWord = true;
		   }
		   else {
			   wordToCompare = getNextWord();
		   }
		} while (!letterFound && !lastWord);
			
		return letterWithSameHash;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String originalString;
		//Permutation perm_a = new Permutation(1, "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8");
		//originalString = perm_a.permute();
		
		//Permutation perm_fm = new Permutation(2, "adeb6f2a18fe33af368d91b09587b68e3abcb9a7");
		//originalString = perm_fm.permute();
		
		Permutation perm_fm_null = new Permutation(2, "adeb6f2a18fe33af368d91b09587b68e3abcb9a7", "a".toCharArray());
		originalString = perm_fm_null.permute();
		System.out.println("String with the same hash: "+ originalString);
		
		
		//Permutation perm_xyz = new Permutation(3, "66b27417d37e024c46526c2f6d358a754fc552f3");
		//originalString = perm_xyz.permute();
		//System.out.println("String with the same hash: "+ originalString);
		
		//Permutation perm_xyz_null = new Permutation(3, "66b27417d37e024c46526c2f6d358a754fc552f3", "a".toCharArray());
		//originalString = perm_xyz_null.permute();
		//System.out.println("String with the same hash: "+ originalString);
	}

}
