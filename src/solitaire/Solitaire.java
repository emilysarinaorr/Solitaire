package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

// This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
public class Solitaire {
	
	 // Circular linked list that is the deck of cards for encryption
	CardNode deckRear;
	
	/* Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffles the array that represents the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	
	// Makes a circular linked list deck out of values read from scanner.
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/** 
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		CardNode curr;
		int tmp;
		// JOKER A IS DECKREAR
		if(deckRear.cardValue == 27){
			tmp = deckRear.cardValue;
			deckRear.cardValue = deckRear.next.cardValue;
			deckRear.next.cardValue = tmp;
			return;
		}
		// JOKER A IS NOT DECKREAR
		for(curr = deckRear.next; curr.next != deckRear.next; curr = curr.next){
			if(curr.cardValue == 27){
				tmp = curr.next.cardValue;
				curr.next.cardValue = curr.cardValue;
				curr.cardValue = tmp;
				return;
			}
		}	
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		CardNode curr;
		int tmp;
		//checks to see if joker B (28) is the last card in the deck
		if(deckRear.cardValue == 28){
			tmp = deckRear.next.next.cardValue;
			deckRear.next.next.cardValue = deckRear.cardValue;
			deckRear.cardValue = deckRear.next.cardValue;
			deckRear.next.cardValue = tmp;
			return;
		}
		//locates joker B (28) in the deck if it's not the last card
		for(curr = deckRear.next; curr.next != deckRear.next; curr = curr.next){
			if(curr.cardValue == 28){
				tmp = curr.next.next.cardValue;
				curr.next.next.cardValue = curr.cardValue;
				curr.cardValue = curr.next.cardValue;
				curr.next.cardValue = tmp;
				return;
			}
		}
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		CardNode curr;
		CardNode firstJoker = null;
		CardNode secondJoker = null;
		CardNode deckFront = null;
		CardNode firstPrev = null;
		CardNode secondNext = null;
		CardNode oldBack = null;
		// DECKREAR IS JOKER
		if(deckRear.cardValue == 27 || deckRear.cardValue == 28){
			secondJoker = deckRear;
			secondNext = deckRear.next;
		}
		// FINDS JOKERS
		for(curr = deckRear; curr.next != deckRear; curr = curr.next){
			if (curr == deckRear){
				deckFront = curr.next;
				oldBack = curr;
			}
			if(curr.next.cardValue == 27 || curr.next.cardValue == 28){
				// IF FIRST JOKER STILL NOT FOUND
				if(firstJoker == null){
					firstPrev = curr;
					firstJoker = curr.next;
				} 
				// FIRST JOKER FOUND
				else if(secondJoker == null){
					secondJoker = curr.next;
					secondNext = curr.next.next;
				}
			}
		}
		// JOKERS IN MIDDLE OF DECK
		if(firstJoker != deckRear.next && secondJoker != deckRear){
			secondJoker.next = deckFront;
			deckRear = firstPrev;
			deckRear.next = secondNext;
			oldBack.next = firstJoker;
			return;
		}
		// SECOND JOKER AT DECKREAR
		else if(firstJoker != deckRear.next && secondJoker == deckRear){
			deckRear = firstPrev;
			deckRear.next = firstJoker;
		}
		//FIRST JOKER AT HEAD
		else if(firstJoker == deckRear.next && secondJoker != deckRear){
			deckRear.next = secondNext;
			oldBack.next = firstJoker;
			deckRear = secondJoker;
		}
		//JOKERS AT HEAD AND DECKREAR
		else if(firstJoker == deckRear.next && secondJoker == deckRear){
			return;
		}
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {	
		int value;
		int count = 0;
		CardNode curr;
		CardNode last = null;
		CardNode rearPrev = null;
		CardNode front = null;
		CardNode newFront = null;
		// JOKER B IS DECKREAR, 28-->27
		if(deckRear.cardValue == 28){
			value = 27;
		}else{
			value = deckRear.cardValue;
		}
		// JOKER A, NOTHING CHANGES
		if(value == 27){
			return;
		}
		// COUNTS UP FROM HEAD
		for(curr = deckRear.next; curr.next != deckRear.next; curr = curr.next){
			count++;
			if(count == value){
				last = curr;
				newFront = last.next;
			}
			if(curr.next == deckRear){
				rearPrev = curr;
			}
			if(curr == deckRear.next){
				front = curr;			
			}
		}
		// CHANGES POINTERS
		last.next = deckRear;
		rearPrev.next = front;
		deckRear.next = newFront;
		return;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		int count = 0;
		int key = 0;
		int value;
		CardNode curr;
		do{
			count = 0;
			jokerA();
			jokerB();
			tripleCut();
			countCut();
			if(deckRear.next.cardValue == 28){
				value = 27;
			}else{
				value = deckRear.next.cardValue;
			}
			for(curr = deckRear.next; curr.next != deckRear.next; curr = curr.next){
				count++;
				if(count == value){
					key = curr.next.cardValue;
				}
			}
		}while(key == 27 || key == 28);	
	    return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		String encrypted = "";
		Character [] abc = new Character[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		int num;
		int letterVal = 0;
		for(int i = 0; i < message.length(); i++){
			Character letter = message.charAt(i);
			if(Character.isLetter(letter)){
				letter = Character.toUpperCase(letter);
				letterVal = letter - 'A' + 1;
				num = getKey();
				num += letterVal;
				if(num > 26){
					num -= 26;
				}
				encrypted += abc[num - 1];
			}
		}
	    return encrypted;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String decrypted = "";
		Character [] abc = new Character[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		int num;
		int letterVal = 0;
		for(int i = 0; i < message.length(); i++){
			Character letter = message.charAt(i);
			if(Character.isLetter(letter)){
				letter = Character.toUpperCase(letter);
				letterVal = letter - 'A' + 1;
				num = letterVal;
				num -= getKey();
				if(num < 1){
					num += 26;
				}
				decrypted += abc[num - 1];
			}
		}
	    return decrypted;
	}
}