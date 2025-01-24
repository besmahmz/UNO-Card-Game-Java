package UNO;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    private ArrayList<Card> playercards; 
    private String name; //player name
    private boolean isAutomated;

    public Player(String name) {
        this.name = name;
        this.playercards = new ArrayList<>();
        this.isAutomated = false; // By default, players are human
    }

    public boolean isAutomated() {
        return isAutomated;
    }

    public void setAutomated(boolean isAutomated) {
        this.isAutomated = isAutomated;
    }

    public int numberOfCards() {
        /*
         * returns number of cards player has in hand
         */
        return playercards.size();
    }

    public ArrayList<Card> PlayerCards() {
        /*
         * returns all the cards player has in hand as an ArrayList
         * This is used mainly to check if player has any valid cards to play.(Check the Uno class)
         */
        return playercards;
    }

    public void pickCards(Card c) {
        /*
         * Add a card to the player's hand
         */
        playercards.add(c);
    }

    public Card throwCard(int index) {
        /*
         * Player throws a card from their hand at position 'index'. The card is removed from the hand.
         */
        return playercards.remove(index);
    }

    public void sayUno() {
        /*
         * Player says uno if they only have 1 card in the hand.
         */
        Scanner s = new Scanner(System.in);
        
        if (playercards.size() == 1) {
            System.out.println("Uno");
            System.out.println("Press Enter...");
            s.nextLine();
        }
    }

    public void showCards() {
        /*
         * This is a graphical representation of a card
         * Just to make cards look more like cards, used in showboard() method in Uno class
         */
        
        String[] card = {" ------ ", "|      |", "|      |", " ------ "};
        String c = "";

        for (int i = 0; i < card.length; i++) {
            for (int j = 0; j < playercards.size(); j++) {
                if (!playercards.get(j).isSpecial()) {
                    if (i == 1) {
                        c = c + "|  " + playercards.get(j).getColor() + " |" + " ";
                    } else if (i == 2) {
                        c = c + "|   " + playercards.get(j).getValue() + "  |" + " ";
                    } else {
                        c = c + card[i] + " "; 
                    }
                } else {
                    if (i == 1) {
                        c = c + "|  " + playercards.get(j).getColor() + " |" + " ";
                    } else if (i == 2) {
                        c = c + "|  " + playercards.get(j).getSpecialCardText() + " |" + " ";
                    } else {
                        c = c + card[i] + " ";
                    }
                }
            }
            c += "\n";
        }

        System.out.print(c);
    }




    public void hideCards() {
        /*
         * To hide player cards
         * Used in showboard() method in Uno class
         */
        
        String[] card = {" ----- ", "|     |", "|     |", " ----- "};
        String c = "";

        for (int i = 0; i < card.length; i++) {
            for (int j = 0; j < playercards.size(); j++) {
                c = c + card[i] + " ";
            }
            c += "\n";
        }

        System.out.print(c);
    }

    public boolean hasWon() {
        /*
         * Checks if player has won or not
         */
        return playercards.size() == 0;
    }

    public String toString() {
        /*
         * Text representation of player
         */
        return this.name;
    }

    // New method to simulate drawing a card
    public void drawCard(Card drawnCard) {
        /*
         * This method adds the drawn card to the player's hand
         */
        playercards.add(drawnCard);
    }
}
