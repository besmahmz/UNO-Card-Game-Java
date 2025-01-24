package UNO;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private ArrayList<Card> deck;
	private ArrayList<Card> discardPile;
	public Deck() {
	    /*
	     * The constructor creates a new deck
	     * There are 4 colors: red, blue, green, yellow
	     * Each suit has two of the same card except 0 (it only appears once in each suit).
	     * For example: green suit has two 1s but only 1 zero
	     */
	    
	    deck = new ArrayList<Card>();
	    String[] colors = {"red","blu","grn","ylw"};
	    int[] numbers = {1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,0}; // regular cards
	    int[] specialnumbers = {2,2,4,4}; // special cards +2, +2, +4 and +4
	    
	    // Adding regular cards to the deck
	    for (String c : colors) {
	        for (int i : numbers) {
	            deck.add(new Card(i, c)); // adding new regular cards to the deck
	        }
	    }
	    
	    // Adding special cards to the deck
	    for (String color : colors) {
	        deck.add(new Card(Card.SpecialType.SKIP, color));  // Adding 2 SKIP cards per color
	        deck.add(new Card(Card.SpecialType.REVERSE, color)); // Adding 2 REVERSE cards per color
	        deck.add(new Card(Card.SpecialType.DRAW_TWO, color)); // Adding 2 +2 cards per color
	    }

	    // Adding one WILD and one WILD_DRAW_FOUR card to the deck
	    deck.add(new Card(Card.SpecialType.WILD)); // Adding 1 WILD card
	    deck.add(new Card(Card.SpecialType.DRAW_FOUR)); // Adding 1 +4 card
	}

	
	public Deck(ArrayList<Card> c) { //overloaded constructor
		/*
		 * in case the current deck becomes empty, all the thrown cards are collected and it becomes the new deck;
		 * 
		 */
		deck = c;
	}
	public void reviveDeck(ArrayList<Card> cardpile) {
        if (discardPile.size() <= 1) {
            System.out.println("Not enough cards in the discard pile to revive the deck!");
            return; // You can't revive if there's not enough cards
        }

        // Preserve the top card
        Card topCard = discardPile.remove(discardPile.size() - 1);

        // Add all other cards from the discard pile back to the deck
        deck.addAll(discardPile);

        // Clear the discard pile
        discardPile.clear();

        // Re-add the top card to the discard pile
        discardPile.add(topCard);

        // Shuffle the deck
        Collections.shuffle(deck);

        // Optionally, update the cardpile to be the discard pile
        cardpile.add(topCard);
    }
	
	public boolean isEmpty() { //is deck empty?
		/*
		 * Checks the size of the deck. If it is greater than 0 then returns false. If not, returns true 
		 */
		
		if(deck.size()>0) {
			return false;
		}
		return true;
	}
	
	public void shuffle() {
		
		/*
		 *  Shuffles the deck
		 */
		
		Collections.shuffle(deck);
				
	}
	
	public Card getTopCard() {
		/*
		 * gets the topmost card from a inverted deck
		 */
		return deck.remove(deck.size()-1);
	}
	
	public Card peek() {
		
		return deck.get(deck.size()-1);
	}
	
	
	public String toString() {
		
		String deck="";
		
		for(Card c:this.deck) {
			
			deck = deck +c+ " ";
		}
		
		return deck;
		
	}
	
	
	
	
}
