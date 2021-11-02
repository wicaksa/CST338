/***********************************************************************************
*
*   Phase 2: Encapsulating Layount and Icons into CardTable and GUICard Classes
*   The second part creates a separate CardTable class that extends JFrame. This 
*   class will control the positioning of the panels and cards of the GUI. We also 
*   create a new GUICard class that manages the reading and building of the card image 
*   Icons. As a result, some of the machinery and statics that we debugged in the
*   first phase of the main will be moved into one or the other of these two new classes.  
*
*   Paola Torres, Matthew Stoney, Cheuk On Yim, Wicaksa Munajat
*   CSUMB CST338 Software Design Fall '21 
*  
************************************************************************************/

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Main
{
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
   static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
   
   /***********************************************************************************
   *
   *   This method returns a new random card for the main to use in its tests. This 
   *   should include jokers because it will only be used in this phase and not in phase 3.
   *
   *   Param: None
   *   Returns: Card 
   *
   ***********************************************************************************/
   static Card randomCardGenerator()
   {
      Random rand = new Random();
      char randValue = Card.valuRanks[rand.nextInt(13)];
      Card.Suit randSuit = Card.Suit.values()[rand.nextInt(Card.Suit.values().length)];
      return new Card(randValue, randSuit);
   }
   
   public static void main(String[] args) // Only public class
   {
      int k; //unused
      Icon tempIcon; // unused
      // load gifs from images/ folder
      GUICard.loadCardIcons();
      
      // Instantiate main game window and set attributes
      CardTable window = new CardTable("Game Window", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      window.setSize(800, 600);
      window.setLocationRelativeTo(null);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setVisible(true);
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {   
         // populate computer hand panel with labels of card back icons
         computerLabels[i] = new JLabel();
         computerLabels[i].setIcon(GUICard.getCardBackIcon());
         window.pnlComputerHand.add(computerLabels[i]);
         
         //populate human hand panel with labels of card fronts
         humanLabels[i] = new JLabel();
         humanLabels[i].setIcon(GUICard.getIcon(randomCardGenerator()));
         window.pnlHumanHand.add(humanLabels[i]);
      }

      // put 2 random cards into play area, with labels
      for (int i = 0; i < NUM_PLAYERS; i++)
      {
         playedCardLabels[i] = new JLabel("", JLabel.CENTER);
         playedCardLabels[i].setIcon(GUICard.getIcon(randomCardGenerator()));
         window.pnlPlayArea.add(playedCardLabels[i]);
      }
      // labels into Play Area
      playLabelText[0] = new JLabel("Computer", JLabel.CENTER);
      playLabelText[1] = new JLabel("You", JLabel.CENTER);
      window.pnlPlayArea.add(playLabelText[0]);
      window.pnlPlayArea.add(playLabelText[1]);

      window.setVisible(true);
   }
}

class CardTable extends JFrame // changed class to private 
{
   private static final long serialVersionUID = 1L;
   static int MAX_CARS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;
   
   private int numCardsPerHand;
   private int numPlayers;
   
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
   
   CardTable(String title, int numCardsPerHand, int numPlayers)
   {  
      // Set members
      this.setTitle(title); // super(title) 
      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;
      
      // Set a border layout to the CardTable
      setLayout(new BorderLayout());
      
      // Create a panel for player, comp, and play area
      // pnlComputerHand = new JPanel(); 
      // pnlHumanHand = new JPanel();

      pnlComputerHand = new JPanel(new GridLayout(1, numCardsPerHand));
      pnlPlayArea = new JPanel(new GridLayout(2, numPlayers));
      pnlHumanHand = new JPanel(new GridLayout(1, numCardsPerHand)); 
      
      // Create titles for each border area 
      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlHumanHand.setBorder(new TitledBorder("Your Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));
      
      // Organize so that computer is north, play area is center, and player is south
      this.add(pnlComputerHand, BorderLayout.NORTH);
      this.add(pnlHumanHand, BorderLayout.SOUTH);
      this.add(pnlPlayArea, BorderLayout.CENTER);
   }
   
    /***********************************************************************************
   *
   *   This method returns a the number of cards per hand.
   *
   *   Param: None
   *   Returns: int 
   *
   ***********************************************************************************/
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   /***********************************************************************************
   *
   *   This method returns the number of players. 
   *
   *   Param: None
   *   Returns: int 
   *
   ***********************************************************************************/
   public int getNumPlayers()
   {
      return numPlayers;
   }
}

/***********************************************************************************
*
*   This class is the benefactor of most of the GUI machinery we tested in Phase 1. 
*   It will read the image files and store them in a static Icon array. This will 
*   be a 2-D array to facilitate addressing the value and suit of a Card in order get 
*   its Icon. We have to be able to convert from chars and suits to ints, and back 
*   again, in order to find the Icon for any given Card object. 
*
***********************************************************************************/
class GUICard // changed to private
{
   // Data members
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   
   /***********************************************************************************
   *
   *   This method loads all of the Icon card objects into the Icons [][] iconCards array.
   *
   *   Param: None
   *   Returns: None 
   *
   ***********************************************************************************/
   public static void loadCardIcons() // changed to public
   {
      if(iconsLoaded)
         return;
      
      String values = "A23456789TJQKX";
      String suits = "CHSD";              

      for (int i = 0; i < suits.length(); i++)
      {
         for (int j = 0; j < values.length(); j++)
         {
            String path = "images/" + values.charAt(j) + suits.charAt(i) + ".gif";
            iconCards[j][i] = new ImageIcon(path);
         }
      }
      // Add the back of the card
      iconBack = new ImageIcon("images/BK.gif");
      iconsLoaded = true;
   }
   
   /***********************************************************************************
   *
   *   This method takes a Card object from the client, and returns the Icon for that 
   *   card. It would be used when the client needs to instantiate or change a JLabel.
   *
   *   Param: Card
   *   Returns: Icon 
   *
   ***********************************************************************************/
   public static Icon getIcon(Card card)
   {
      // loadCardIcons();
      // debugging
      // System.out.println(card + "\t" + card.getValueAsInt() + " " + card.getSuitAsInt() + " " + card.getCardAsInt()); 
      // debugging
      return iconCards[card.getValueAsInt()][card.getSuitAsInt()]; 
   }

   /***********************************************************************************
   *
   *   This method gets the back of the Card Icon. 
   *
   *   Param: None
   *   Returns: Icon 
   *
   ***********************************************************************************/
   public static Icon getCardBackIcon()
   {
      loadCardIcons();
      return iconBack;
   }
   
}

class Card // changed to private
{
   public enum Suit {clubs, hearts, spades, diamonds}
   private char value;
   private Suit suit;
   private boolean errorFlag = false;
   public final static char[] valuRanks = new char[] {'A', '2', '3', '4', 
         '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
   
   public Card()
   {
      this.set('A', Suit.spades);
   }
   
   public Card(char value, Suit suit)
   {
      this.set(value, suit);
   }
   
   public Card(Card other)
   {
      this.set(other.getValue(), other.getSuit());
   }
   public String toString()
   {
      if (this.errorFlag == true)
      {
         return "[Invalid Card]";
      }
      else
      {
         return this.value + " of " + this.suit;
      }
   }
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         this.errorFlag = false;
         return true;
      }
      else {
         {
            this.value = 'A';
            this.suit = Suit.spades;
            this.errorFlag = true;
            return false;
         }
      }
   }
   
   public char getValue()
   {
      return this.value;
   }
   public Suit getSuit()
   {
      return this.suit;
   }
   public boolean getErrorFlag()
   {
      return this.errorFlag;
   }
   public boolean equals(Card card)
   {
      return (this.value == card.value && 
              this.suit == card.suit &&
              this.errorFlag == card.errorFlag);
   }
   private boolean isValid(char value, Suit suit)
   {
      for (char val : valuRanks)
      {
         if (val == value)
         {
            return true;
         }
      }
      return false;
   }
   
   public static void arraySort(Card[] cards, int arraySize)
   {
      // bubble sort
      Card temp;
      for (int i = 0; i < arraySize; i++)
      {
         for (int j = 1; j < (arraySize - i); j++)
         {
            if (cards[j - 1].getCardAsInt() > cards[j].getCardAsInt())
            {
               temp = new Card(cards[j - 1]);
               cards[j - 1] = new Card(cards[j]);
               cards[j] = new Card(temp);
            }
         }
      }
   }

   public boolean cardGreaterThan(Card other)
   {
      if(this.getCardAsInt() > other.getCardAsInt())
         return true;
      else return false;
   }

   public int getValueAsInt()
   {
      for (int i = 0; i < valuRanks.length; i++)
      {
         if (this.getValue() == valuRanks[i])
            return i;
      }
      return -1; //error
   }

   public int getSuitAsInt()
   {
      for (int i = 0; i < Card.Suit.values().length; i++)
      {
         if (this.getSuit() == Card.Suit.values()[i])
            return i;
      }
      return -1; //error
   }

   public int getCardAsInt()
   {
      // return (   (14 * this.getSuitAsInt()) + this.getValueAsInt()   ) ?? (CY)
      return ((4 * this.getValueAsInt()) + this.getSuitAsInt());
   }

}

/*************************************************************************
 * 
 * 
 * 
 *          Hand Class
 * 
 * 
 * 
 **************************************************************************/
class Hand // changed to private
{
   public static final int MAX_CARDS = 100;
   private Card[] myCards;
   private int numCards;
   
   Hand()
   {
      this.myCards = new Card[MAX_CARDS];
      this.numCards = 0;
   }
      
   public void resetHand()
   {
      this.myCards = new Card[MAX_CARDS];
      this.numCards = 0;
   }
   
   public boolean takeCard(Card card)
   {
      if (numCards <= MAX_CARDS)
      {
         this.myCards[this.numCards]= new Card(card);
         numCards++;
         return true;
      }
      return false;
   }
   public Card playCard()
   {
      if (numCards > 0)
      {
         numCards--;
         Card playCard = new Card(myCards[numCards]);
         myCards[numCards] = null;
         return playCard;
      }
      return null;
   }
   
   public Card playCard(int cardIndex)
   {
      if (this.numCards == 0)
      {
         return new Card('I', Card.Suit.spades);
      }
      Card card = myCards[cardIndex];
      numCards--;
      for (int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }
      myCards[numCards] = null;
      return card;
   }
   
   public String toString()
   {
      if(numCards == 0)
         return "Hand = ( )";
      String fullHand = "Hand = (";

      for(int i = 0; i < MAX_CARDS - 1; i++)
      {
         if(myCards[i] != null)
            fullHand += " " + myCards[i].toString() + "," ;
      }

      fullHand += " " + myCards[numCards - 1].toString() + " )" ;

      return fullHand;
   }
   
   public int getNumCards()
   {
      return this.numCards;
   }
   
   public Card inspectCard(int k)
   {
      if (k < 0 || k > numCards)
      {
         return new Card('I', Card.Suit.spades);
      }
      return myCards[k];
   }
   
}

/*****************************************************************
 * 
 * 
 *       DECK CLASS
 *       
 *       
 ****************************************************************/

class Deck // change to private
{
   public final int MAX_CARDS = 6 * 56;
   private static Card[] masterPack = new Card[52]; 
   private int numPacks;
   private Card[] cards;
   private int topCard;
   
   Deck()
   {
      allocateMasterPack();
      cards = new Card[56];
      this.numPacks = 1;
      init();
   }
   Deck(int numPacks)
   {
      allocateMasterPack();
      cards = new Card[56 * numPacks];
      this.numPacks = numPacks;
      init();
   }
   
   public void init()
   {
      topCard = 0;
      for (int i = 0; i < numPacks; i++)
      {
         for (Card card : masterPack)
         {
            cards[topCard] = new Card(card);
            topCard++;
         }
      }
   }

   private static void allocateMasterPack()
   {
      if(masterPack[0] != null)
      {
         return;
      }
      int masterCardCount = 0;
      for (Card.Suit suit : Card.Suit.values())
      {
         for (char value : Card.valuRanks)
         {
            // Jokers do not go into masterPack
            if (value != 'X')
            {
               masterPack[masterCardCount] = new Card(value, suit);
               masterCardCount++;
            }
         }
      }
   }
   
   public void shuffle()
   {
      Random rand = new Random();
      int numCards = topCard;
      for (int i = 0; i < numCards; i++)
      {
         int randomCard = 1 + rand.nextInt(numCards - 1);
         Card temp = new Card(cards[randomCard]);
         cards[randomCard] = new Card(cards[i]);
         cards[i] = new Card(temp);
      }
   }
   
   public Card dealCard()
   {
      Card newCard;
      if (topCard > 0 && cards[topCard - 1] != null)
      {
         newCard = new Card(cards[topCard - 1]);
         cards[topCard - 1] = null;
         topCard--;
         return newCard;
      }
      else 
      {
         return new Card('I', Card.Suit.spades);
      }
   }
   
   public int getTopCard()
   {
      return this.topCard;
   }

   public Card inspectCard(int k)
   {
      if (k < cards.length && k >= 0)
      {
         return new Card(cards[k]);
      }
      else return new Card('I', Card.Suit.spades);
   }

   /**************************************************************************
   * 
   *   This method adds a card to the deck. make sure that there are
   *   not too many instances of the card in the deck if you add it. 
   *   Return false if there will be too many. 
   *   It should put the card on the top of the deck.
   *
   *   Parameter: Card 
   *   Return: boolean
   * 
   ***************************************************************************/
   public boolean addCard(Card card)
   {
      // First check to see how many instances of the card are present
      int cardInstances = 0;

      for (int i = 0; i < cards.length; i++)
      {
         if (cards[i] != null && cards[i].equals(card))
         {
            cardInstances++;
         }
      }

      // if there are fewer instances than there should be in numPacks decks,
      // add card to top of deck
      if (cardInstances < numPacks)
      {
         cards[topCard] = new Card(card); 
         topCard++;
         return true;
      }
      else
         System.out.println("Too many of that card!");
         return false;
   }

   /***************************************************************************
    * 
    *   This method removes a certain card from the deck.
    *   Put the current top card into its place.  
    *   Be sure the card you need is actually still in the deck, if not return false.
    *
    *   Parameter: Card 
    *   Return: boolean
    * 
    ***************************************************************************/
   public boolean removeCard(Card card)
   {
    // Check if the card is in the deck first 
      for (int i = 0; i < topCard; i++)
      {
         if (cards[i].equals(card))
         {
            // Set the top card in the position of the card to be removed
            cards[i].set(cards[topCard].getValue(), cards[topCard].getSuit());
            cards[topCard - 1] = null;
            topCard--;
            return true;
         }
      }
      // If no instance of the card return false
      return false;
   }

    /***************************************************************************
    * 
    *   This method sorts cards in the deck back into the right order. 
    *
    *   Parameter: None 
    *   Return: None
    * 
    ***************************************************************************/
   void sort()
   {
      Card.arraySort(cards, this.topCard); 
   }
   
    /***************************************************************************
    * 
    * This method returns the number of cards remaining in the deck. 
    *
    * Parameter: None Return: int
    * 
    ***************************************************************************/
   int getNumCards()
   {
      // Just return the topCard + 1 to adjust for the index
      return this.topCard + 1; 
   }
   
}
