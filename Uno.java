package UNO;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Uno {
	private Card current; // the current card or previously played card or starting card
	private Deck deck; // the deck of the game
	private ArrayList<Card> cardpile; //when players throw card, it piles up here. Also, creates a new deck if the current deck is empty
	private int penalty; // when wildcards stack up the penalty stacks up. If a player is unable to counter the current wildcard on play, player is penalised "penalty" number of cards
	private Scanner choice;
	private ArrayList<Player> players;

	private int pick; // players pick
	
	public Uno() {
		/*constructor
		 * constructs the game
		 * prepares the game to play
		 */
		deck = new Deck();
		deck.shuffle();
		penalty = 0;
		current = getStartingCard();
		cardpile = new ArrayList<Card>();
		cardpile.add(current);
		choice = new Scanner(System.in);
		// Ask for the number of players
        System.out.println("Enter the number of players (2-4): ");
        int numPlayers = choice.nextInt();
        choice.nextLine(); // consume the newline
        
        // Create the players list
        players = new ArrayList<Player>();
        for (int i = 0; i < numPlayers; i++) {
            String playerName = (i == 0) ? "Player 1" : "Player " + (i + 1);
            Player player = new Player(playerName);
            players.add(player);
        }
        // Ask if the players should be automated
        for (int i = 1; i < numPlayers; i++) { // Start from player 2
            if (i == 1) { // You can make this more flexible if needed
                System.out.println("Do you want Player " + (i + 1) + " to be automated? (yes/no)");
                String response = choice.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    players.get(i).setAutomated(true); // Set Player i to automated
                }
            }
        }
 
		
	
	}
	
	public void dealCards() {
	    int cardsPerPlayer = 7;
	    for (Player player : players) { 
	        for (int i = 0; i < cardsPerPlayer; i++) {
	            player.pickCards(deck.getTopCard()); // Give a card to the player
	        }
	    }
	}
	
	public void game() {
	    dealCards();
	    int turn = 0;
	    boolean reverseOrder = false;

	    while (!gameOver()) {
	        Player currentPlayer = players.get(turn % players.size());

	        if (currentPlayer.isAutomated()) {
	            playAutomatedGame(currentPlayer);
	        } else {
	            playGame(currentPlayer);
	        }

	        // Handle special cards after each player's turn
	        if (current.getSpecialType() == Card.SpecialType.SKIP) {
	            turn = (turn + 1) % players.size();  // Skip next player
	        }

	        if (current.getSpecialType() == Card.SpecialType.REVERSE) {
	            reverseOrder = !reverseOrder;  // Reverse the turn order
	        }

	        if (current.getSpecialType() == Card.SpecialType.DRAW_TWO) {
	            drawCard(players.get((turn + 1) % players.size()));
	            drawCard(players.get((turn + 1) % players.size()));
	        }

	        if (current.getSpecialType() == Card.SpecialType.DRAW_FOUR) {
	            drawCard(players.get((turn + 1) % players.size()));
	            drawCard(players.get((turn + 1) % players.size()));
	            drawCard(players.get((turn + 1) % players.size()));
	            drawCard(players.get((turn + 1) % players.size()));
	        }

	        // Adjust turn direction based on reverseOrder
	        if (reverseOrder) {
	            turn = (turn - 1 + players.size()) % players.size();  // Reverse order
	        } else {
	            turn = (turn + 1) % players.size();  // Normal order
	        }
	    }
	}


	
	
	 public void drawCard(Player p) {
		    // Draw a card from the deck
		    Card drawnCard = deck.getTopCard(); 
		    p.pickCards(drawnCard);  // Add the card to player's hand
		    System.out.println(p + " drew a card: " + drawnCard);
		    pause();
		}

	


	
	
	
	 private void playGame(Player p) {
		    decorate();
		    System.out.println(p + ", It is your turn\nThe current card on play is:\n" + current);
		    decorate();
		    showBoard(p);
		    decorate();

		    // Check if the player has any playable cards
		    if (!hasPlayableCard(p)) {
		        System.out.println("You don't have any valid cards to play, drawing a card...");
		        drawCard(p);
		        System.out.println("Your new hand:");
		        p.showCards();

		        // After drawing, check if the new card is playable
		        if (!hasPlayableCard(p)) {
		            System.out.println("The drawn card is also not playable. Your turn is skipped.");
		            return;
		        } else {
		            System.out.println("You now have a playable card.");
		        }
		    }

		    // Continue the player's turn to select a valid card
		    System.out.println("Please pick a card:");
		    int pick = choice.nextInt() - 1;

		    while (!isValidChoice(p, pick)) {
		        System.out.println("Invalid pick. Please pick a valid card.");
		        pick = choice.nextInt() - 1;
		    }

		    Card play = p.throwCard(pick);
		    p.sayUno();
		    current = play;
		    cardpile.add(current);
		    reviveDeck();
		}



	private void playAutomatedGame(Player p) {
	    System.out.println(p + " (AI), It is your turn\nThe current card on play is:\n" + current);
	    pause();
	    decorate();
	    showBoard(p);
	    decorate();

	    // Separate valid number and special cards
	    ArrayList<Card> validNumberCards = new ArrayList<>();
	    ArrayList<Card> validSpecialCards = new ArrayList<>();

	    for (Card card : p.PlayerCards()) {
	        if (card.getColor().equals(current.getColor()) || card.getValue() == current.getValue()) {
	            validNumberCards.add(card);  // Prefer number cards
	        } else if (card.isSpecial()) {
	            validSpecialCards.add(card);  // Consider special cards only if needed
	        }
	    }

	    if (!validNumberCards.isEmpty()) {
	        // Prefer playing a number card if possible
	        Card play = validNumberCards.get(new Random().nextInt(validNumberCards.size()));
	        p.throwCard(p.PlayerCards().indexOf(play));
	        System.out.println(p + " (AI) played: " + play);
	        current = play;
	    } else if (!validSpecialCards.isEmpty()) {
	        // Play a special card only if no number card is available
	        Card play = validSpecialCards.get(new Random().nextInt(validSpecialCards.size()));
	        p.throwCard(p.PlayerCards().indexOf(play));
	        System.out.println(p + " (AI) played: " + play);
	        current = play;
	    } else {
	        // No valid card, draw one
	        drawCard(p);
	    }

	    cardpile.add(current);
	    reviveDeck();
	}

	
	
	
	public boolean hasPlayableCard(Player p) {
	    for (Card c : p.PlayerCards()) {
	        if (c.getColor().equals(current.getColor()) || 
	            c.getValue() == current.getValue() || 
	            c.isSpecial() || 
	            (c.getSpecialType() == Card.SpecialType.WILD || c.getSpecialType() == Card.SpecialType.WILD_DRAW_FOUR)) {
	            return true;
	        }
	    }
	    return false;
	}



	
	public void reviveDeck() {
	    // If the deck is empty, revive it by shuffling the discard pile back into the deck.
	    if (deck.isEmpty()) {
	        System.out.println("The deck is empty! Reviving the deck...");

	        // Add all the cards from the discard pile back into the deck
	        deck.reviveDeck(cardpile); // Ensure the Deck class has this method
	        System.out.println("The deck has been revived and shuffled.");
	    }
	}
	
	private boolean hasSpecial(Player p) {
		// TODO Auto-generated method stub
		
		for(Card c:p.PlayerCards()) {
			
			if(c.isSpecial()) {
				return true;
			}
			
		}
		
		
		return false;
	}

	private boolean isValidChoice(Player p, int choice) {
	    if (choice < 0 || choice >= p.PlayerCards().size()) {
	        System.out.println("Invalid choice: " + choice);
	        return false;
	    }

	    Card selectedCard = p.PlayerCards().get(choice);
	    

	    if (selectedCard.isSpecial()) {
	        System.out.println("Checking special card validity...");
	        if (selectedCard.getColor().equals(current.getColor()) || selectedCard.getSpecialCardText().equals(current.getSpecialCardText())) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	    return selectedCard.getColor().equals(current.getColor()) || 
	           selectedCard.getValue() == current.getValue();
	}


	private void pause() {
		/*
		 * creates a pause
		 * asks the user to press enter
		 * purpose is simply to get user engagement
		 */
		System.out.println("Press enter to continue......");
		choice.nextLine();
		
	}
	
		
	private boolean hasColor(Player p) {
		/*
		 * checks if player has card of the same color as the current card that is being played
		 */
		for(Card c:p.PlayerCards()) {
			
			if(c.getColor().equals(current.getColor())) {
				return true;
			}
			
			
		}
		
		return false;
	}
	
	private boolean hasValue(Player p) {  
		/*
		 * checks if the player has the card of the same value as the current that is being played. 
		 */
		
		for(Card c:p.PlayerCards()) {
			
			if(c.getValue()==current.getValue()) {
				
				return true;
				
			}
		}
		
		return false;
	}
	
	
	private boolean canOverride(Player p) {
		
		/*
		 * checks if player has a wild card (special card)
		 * special cards can be played when you don't have a card with the same color or the value of the card that is being currently played
		 * if the current card is a special card, then player must have a special card with the same or greater value to play.
		 */
		for(Card c:p.PlayerCards()) {
			
			if(c.isSpecial()) {
				if(c.getValue() >= current.getValue()) {
					return true;
				}
			}
		}
		
		
		return false;
	}
	
	private void decorate() {
		/*
		 * draws asterik lines
		 */
		
		
		System.out.println("***********************************************************************************");
	}
	
	
	
	
	private Card getStartingCard() {
		
		/*
		 * gets a valid starting card.
		 * starting card cannot be a special card
		 */
		
		Card temp = deck.peek();
		while(temp.isSpecial()) {
			deck.shuffle();
			temp = deck.peek();
		}
		
		temp = deck.getTopCard();
		return temp;
	}
	
	
	


	private boolean gameOver() {
        // Check if any player has won
        for (Player p : players) {
            if (p.hasWon()) {
                System.out.println("**************************************************");
                System.out.println(p + " has won");
                System.out.println("**************************************************");
                return true;
            }
        }
        return false;
    }

	public void showBoard(Player currentPlayer) {
	    for (int i = 0; i < players.size(); i++) {
	        Player p = players.get(i);

	        // Print player's name
	        System.out.println("                " + p);

	        // Show cards for the current player, hide for others
	        if (p.equals(currentPlayer)) {
	            p.showCards();
	        } else {
	            p.hideCards();
	        }

	        System.out.println("");
	    }
	}

}
