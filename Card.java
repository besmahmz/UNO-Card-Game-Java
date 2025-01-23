package UNO;

public class Card {
    private String color;
    private int value;
    private SpecialType specialType;

    public enum SpecialType {
        DRAW_TWO, DRAW_FOUR, SKIP, REVERSE, WILD, WILD_DRAW_FOUR, NONE
    }

    // Constructor for non-special cards (normal number cards)
    public Card(int value, String color) {
        this.color = color;
        this.value = value;
        this.specialType = SpecialType.NONE;
    }

    // Constructor for special cards (e.g., +2, +4)
    public Card(SpecialType specialType) {
        this.color = "";
        this.value = 0;
        this.specialType = specialType;
    }

    public String getColor() {
        return this.color;
    }

    public int getValue() {
        return this.value;
    }

    public SpecialType getSpecialType() {
        return this.specialType;
    }

    public String toString() {
        // Creates the visual representation of the card
        String[] card = {" ----- ", "|     |", "|     |", " ----- "};
        String cardDetails = "";

        for (int i = 0; i < card.length; i++) {
            for (int j = 0; j < 1; j++) {
                if (this.specialType == SpecialType.NONE) {
                    if (i == 1) {
                        cardDetails += "| " + this.getColor() + " |" + " ";
                    } else if (i == 2) {
                        cardDetails += "|  " + this.getValue() + "  |" + " ";
                    } else {
                        cardDetails += card[i] + " ";
                    }
                } else {
                    if (i == 1) {
                        cardDetails += "| " + getSpecialCardText() + " |" + " ";
                    } else {
                        cardDetails += card[i] + " ";
                    }
                }
            }
            cardDetails += "\n";
        }

        return cardDetails;
    }

    public boolean isSpecial() {
        return this.specialType != SpecialType.NONE;
    }

    private String getSpecialCardText() {
        // Returns the string representation for special cards
        switch (this.specialType) {
            case DRAW_TWO:
                return "+2";
            case DRAW_FOUR:
                return "+4";
            case SKIP:
                return "Skip";
            case REVERSE:
                return "Reverse";
            case WILD:
                return "Wild";
            case WILD_DRAW_FOUR:
                return "Wild +4";
            default:
                return "Unknown";
        }
    }
}
