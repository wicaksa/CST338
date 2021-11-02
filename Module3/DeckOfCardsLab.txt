/**************************************************************
 * Wicaksa Munajat, Cheuk On Yim, Matthew Stoney, Paola Torres 
 * CST 338
 * Assignment 3: Deck of Cards 
 **************************************************************/
import java.util.Random;
import java.util.Scanner;

class Assig3 
{
   public static void main(String[] args) 
   {

      /****************************************
      System.out.println(" --- Card Test --- ");

      Card card1 = new Card();
      Card card2 = new Card('L', Card.Suit.spades); // invalid
      Card card3 = new Card('J', Card.Suit.clubs);

      System.out.println(card1.toString());
      System.out.println(card2.toString());
      System.out.println(card3.toString());

      card1.set('L', Card.Suit.clubs);
      card2.set('Q', Card.Suit.spades);

      System.out.println();

      System.out.println(card1.toString());
      System.out.println(card2.toString());
      System.out.println(card3.toString());

      //****************************************
      System.out.println(" --- Hand Test --- ");

      Card card4 = new Card('3', Card.Suit.clubs);
      Card card5 = new Card('T', Card.Suit.clubs);
      Card card6 = new Card('9', Card.Suit.hearts);
      Hand testHand1 = new Hand();

      for(int i = 0; i < Hand.MAX_CARDS - 1; i = i + 3 )
      {
         testHand1.takeCard(card4);
         testHand1.takeCard(card5);
         testHand1.takeCard(card6);
      }

      testHand1.takeCard(card4);

      System.out.println("Hand full\n"+"After deal\n"+testHand1.toString());

      System.out.println("\nTesting inspectCard()");
      Card card7 = new Card('P', Card.Suit.hearts);
      Card card8 = new Card('9', Card.Suit.hearts);
      Hand testHand2 = new Hand();
      testHand2.takeCard(card7);
      testHand2.takeCard(card8);
      System.out.println(testHand2.inspectCard(0).toString());
      System.out.println(testHand2.inspectCard(1).toString());

      for(int i = testHand1.getNumCards(); i > 0; i--)
      {
         System.out.println(testHand1.playCard().toString());
      }

      System.out.println("\nAfter playing all cards");
      System.out.println(testHand1.toString());

      /**********   BEGIN PHASE 3 *********/
      // Instantiate a 2-pack deck and deal all cards to output
      Deck myDeck = new Deck(2);
      int deckSize = myDeck.getTopCard();
      for (int i = 0; i < deckSize; i++)
      {
         System.out.println(myDeck.dealCard());
      }
      System.out.println();

      // Reset the 2-pack deck above, shuffle the cards, and deal all
      // cards to the output again
      myDeck.init(2);
      myDeck.shuffle();
      for (int i = 0; i < deckSize; i++)
      {
         System.out.println(myDeck.dealCard());
      }
      System.out.println();

      //Instantiate a single-pack deck and deal all cards to output
      Deck myDeck2 = new Deck(1);
      deckSize = myDeck2.getTopCard();
      for (int i = 0; i < deckSize; i++)
      {
         System.out.println(myDeck2.dealCard());
      }
      System.out.println();

      // Reset the single-pack deck, shuffle it and deal all cards to output 
      // again
      myDeck2.init(1);
      myDeck2.shuffle();
      for (int i = 0; i < deckSize; i++)
      {
         System.out.println(myDeck2.dealCard());
      }
      System.out.println();
      /*********** END PHASE 3 ***********/

      //*********** Phase 4*****************************************
      Scanner userInput = new Scanner(System.in);
      int numberOfHands = 0;

      // validates input is between 1 and 10
      do 
      {
         System.out.print("How many hands? (Select 1 - 10) ");
         numberOfHands = userInput.nextInt();
      } 
      while(numberOfHands < 1 || numberOfHands > 10);

      Deck deck = new Deck(1);

      // creates an array of Hand objects the size of the user input
      // instantiates each hand via for loop
      Hand[] hand = new Hand[numberOfHands];
      for (int i = 0; i < numberOfHands; i++) 
      {
         hand[i] = new Hand();
      }

      // iterates through the Deck object created
      // deals one card to each hand at a time
      // if the currentHandPosition variable exceeds the number of hands
      // the variable is reset to the first hand
      int numberOfCards = deck.getTopCard();
      int currentHandPosition = 0;
      for (int i = 0; i <= numberOfCards; i++) 
      {
         if (currentHandPosition > numberOfHands - 1) 
         {
            currentHandPosition = 0;
         }
         hand[currentHandPosition].takeCard(deck.dealCard());
         currentHandPosition++;
      }


      // iterates through the Hand object array printing out each hand
      System.out.println("Hands from the unshuffled deck: ");
      for (int i = 0; i < numberOfHands; i++) 
      {
         System.out.println("Hand " + (i + 1) + ": (" + hand[i].toString() + ")");
         System.out.println();
      }


      // resets each hand in the Hand array

      for (int i = 0; i < numberOfHands; i++) 
      {
         hand[i].resetHand();
      }

      // returns deck to original states
      // then shuffles
      deck.init(1);
      deck.shuffle();

      // iterates through the shuffled Deck object
      // dealing one card to each hand at a time
      // points the currentHandPosition variable to the front of the Hand array 
      // at the beginning of the loop
      for (int i = 0; i <= numberOfCards; i++) 
      {
         if (i == 0) 
         {
            currentHandPosition = 0;
         }
         if (currentHandPosition > numberOfHands - 1) 
         {
            currentHandPosition = 0;
         }
         hand[currentHandPosition].takeCard(deck.dealCard());
         currentHandPosition++;
      }

      System.out.println();

      // iterates through the Hand object array displaying the shuffled hands
      System.out.println("Hands from the shuffled deck: ");
      for (int i = 0; i < numberOfHands; i++) 
      {
         System.out.println("Hand " + (i + 1) + ": (" + hand[i].toString() + ")");
         System.out.println();
      }

      userInput.close();

      //**************************end Phase4***************************
      //**************************************************************/
   }
} 

/*************************************************
 *   
 *   This Card class represents a playing card. 
 *   It holds the value of the card as a char type 
 *   and the suit as an enum type.
 *
 **************************************************/
class Card 
{ 
   public enum Suit 
   {
      clubs, diamonds, hearts, spades 
   }

   // Private member data
   private char value;
   private Suit suit;
   private boolean errorFlag; // Checks if values of value and suit are legal
   public final static char[] CARD_VALUES = new char[] {'A', '2', '3', '4', 
         '5', '6', '7', '8', '9', 'T', 'J', 'Q','K'};

   /*************************************************
    *   
    *   Card Class default constructor that sets value 
    *   to 'A' and sets suit to 'spade'.
    *   
    *   Parameters: None
    *   Returns: None
    *
    **************************************************/
   public Card()
   {
      this.set('A', Suit.spades);
   }

   /*************************************************
    *   
    *   Card class parameterized constructor that sets 
    *   value and suits to user specs.
    *
    *   Parameters: value of card (char), suit of card
    *   (Suit)
    *   Returns: Card object
    *   
    **************************************************/

   public Card(char value, Suit suit)
   {
      this.set(value, suit);
   }

   /*************************************************
    *   
    *   This method creates a string version of the card.
    *   Returns an error message if errorFlag is false 
    *   else it returns a string representation of the card.
    *
    *   Parameters: None
    *   Returns: String - representing the value and 
    *   suit of the card
    *
    **************************************************/

   public String toString()
   {
      if (this.errorFlag == true)
      {
         return ("[invalid card]");
      }
      else
      {
         return (this.value + " of " + this.suit);
      }
   }

   /*************************************************
    *   
    *   Checks if the value passed is valid. If so, it
    *   assigns it and sets errorFlag to false. 
    *   Otherwise returns a card with an errorFlag true.
    *
    *   Parameters: card value(char), card suit(Suit)
    *   Returns: boolean that represents whether a set
    *   card is valid or invalid 
    *
    **************************************************/
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit; 
         this.errorFlag = false;
         return true; 
      }
      else
      {
         this.value = 'A';
         this.suit = Suit.spades;
         this.errorFlag = true;
         return false; 
      }
   }

   /*************************************************
    *   
    *   Accessor method for value. 
    *   
    *   Parameters: None
    *   Returns: the value of the card (char). 
    *
    **************************************************/
   public char getValue()
   {
      return value; 
   }

   /*************************************************
    *   
    *   Accessor method for suit. 
    *   
    *   Parameters: None 
    *   Returns: the suit of the card (Suit). 
    *
    **************************************************/
   public Suit getSuit()
   {
      return suit; 
   }

   /*************************************************
    *   
    *   Accessor method for errorFlag. 
    *
    *   Parameters: None
    *   Returns: the errorFlag of the card (boolean). 
    *
    **************************************************/
   public boolean getErrorFlag()
   {
      return errorFlag; 
   }

   /*************************************************
    *   
    *   Compares whether two cards are equivalent by 
    *   checking if all the fields are identical
    *   
    *   Parameters: a card object 
    *   Returns: a boolean value 
    *   
    **************************************************/
   public boolean equals(Card card)
   {
      return (this.value == card.value && this.suit == card.suit);
   }

   /*************************************************
    *  
    *   Checks whether a card is a valid card
    *   
    *   Parameters: the value of the card (char), suit 
    *   of the card (Suit)
    *   Returns: a boolean value 
    *    
    **************************************************/   
   private boolean isValid(char value, Suit suit)
   {
      for (char val : CARD_VALUES)
      {
         if (val == value)
            return true;
      }   
      return false;                        
   }
}           

/*****************************************************
 * 
 * This class represents a hand of cards. It has two private data: a Card[]  
 * that store cards and an int numCards that keep track of number of cards
 * 
 ****************************************************/
class Hand
{  
   public static final int MAX_CARDS = 100;    
   private Card[] myCards; // the user's hand
   private int numCards; // number of cards in a user's hand


   Hand()
   /*
    * Default constructor for Hand.
    */
   {
      this.myCards = new Card[MAX_CARDS];
      this.numCards = 0;
   }  

   /* 
    * Removes all the cards from the hand by resetting the new myCards[] and 
    * numCards to 0.
    */
   public void resetHand()
   {
      this.myCards = new Card[MAX_CARDS];
      this.numCards = 0;
   }

   /*
    *   Adds a object copy card to the user's hand. 
    *
    *   Parameter: Card - the card object.
    *   Returns: True if card is stored successfully in myCards[], 
    *   false otherwise.
    */
   public boolean takeCard(Card card)
   {
      if(numCards <= MAX_CARDS)
      {
         Card newCard = new Card();
         newCard.set(card.getValue(), card.getSuit());
         this.myCards[this.numCards] = newCard;
         numCards++;
         return true;
      }   
      return false;
   }

   /*
    * Returns and removes the card in the top occupied position of the array.
    *
    *   Parameter: none
    *   Returns: top occupied card object if numCards>0, null otherwise.
    */
   public Card playCard()
   {  
      if(numCards>0)
      {
         numCards--;
         Card playCard = new Card();
         playCard.set(myCards[numCards].getValue(), 
               myCards[numCards].getSuit());
         myCards[numCards] = null;
         return playCard;  
      }
      return null;
   }

   /*
    * A stringizer that the client can use to display the entire hand.
    *
    *   Parameter: none
    *   Returns: A string 
    */
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

   /*
    *   Accessor for numCards.
    *
    *   Parameter: none
    *   Returns: An integer of number of cards. 
    */
   public int getNumCards()
   {
      return this.numCards;
   }

   /*
    *   Accessor for an individual card.
    *
    *   Parameter: none
    *   Returns: a card with errorFlag = true if k is bad, otherwise myCards[k]
    */
   public Card inspectCard(int k)
   {
      if(k < 0 || k > numCards) 
         return new Card('I', Card.Suit.spades); 
      return myCards[k];
   }
}

/*************************************************
 *   This class represents a deck of cards. It can hold up to
 *   MAX_CARDS number of cards. There are methods for initializing and
 *   shuffling the deck as well as dealing and inspecting cards.
 *   This class requires: import java.util.Random
 *
 *   Data members:
 *     MAX_CARDS - maximum number of cards allowed in the deck 
 *     masterPack[] - a single complete deck array that is used to create 
 *       multiple decks
 *     cards[] - array that holds one or more deck of Card objects
 *     topCard - integer, the array index of the topmost card in cards[]
 *
 **************************************************/


class Deck
{
   public final int MAX_CARDS = 6 * 52;
   private static Card[] masterPack = new Card[52];

   private Card[] cards;
   private int topCard;
   /*********************************************
    * 
    *   Default constructor. Creates a master pack and instantiates a single
    *   deck.
    *
    *********************************************/
   Deck()
   {
      allocateMasterPack();
      cards = new Card[52];
      init(1);
   }
   /*********************************************
    * 
    *   Constructor with number of packs specified. Creates a master pack
    *   and a deck with one or more packs of cards in it.
    *    
    *   Parameter: numPacks - number of packs to go in the deck
    *
    *********************************************/
   Deck(int numPacks)
   {
      allocateMasterPack();
      cards = new Card[numPacks * 52];
      init(numPacks);
   }
   /*********************************************
    * public void init(int numPacks)
    * 
    *   This method initializes a deck by inserting each card from the master
    *   pack. It does this numPacks number of times to create a multi-pack
    *   deck. topCard is incremented to always hold the array index of the
    *   top card.
    *    
    *   Parameters: numPacks - number of card packs in the deck
    *   Returns: none
    *
    *********************************************/
   public void init(int numPacks)
   {
      topCard = 0;
      for(int i = 0; i < numPacks; i++)
      {
         for(Card card : masterPack)
         {
            cards[topCard] = card;
            topCard++;
         }
      }
   }
   /*********************************************
    * public static void allocateMasterPack()
    * 
    *   This method creates the master pack with one of every card. The deck
    *   is initialized using this pack. The class should never call this 
    *   method more than once.
    *    
    *   Parameters: none
    *   Returns: none
    *
    *********************************************/
   private static void allocateMasterPack()
   {
      // Only ever instantiate masterPack[] once
      if (masterPack[0] != null)
      {
         return;
      }
      int masterCardCount = 0;
      for(Card.Suit suit : Card.Suit.values())
      {
         for(char value : Card.CARD_VALUES)
         {
            masterPack[masterCardCount] = new Card(value, suit);
            masterCardCount++;
         }
      }
   }
   /*********************************************
    * public void shuffle()
    * 
    *   This method uses a Random object to shuffle the deck of cards.
    *   
    *   Parameters: none
    *   Returns: none
    * 
    *   Reference: https://www.geeksforgeeks.org/shuffle-a-deck-of-cards-3/
    *********************************************/
   public void shuffle()
   {
      Random rand = new Random();
      int numCards = cards.length;
      Card tempCard = new Card();
      for (int i = 0; i < numCards; i++)
      {
         // This chooses a random number from only remaining cards 
         // And uses a temporary object to swap them
         int randomCard = 1 + rand.nextInt(numCards - 1);
         tempCard = cards[randomCard];
         cards[randomCard] = cards[i];
         cards[i] = tempCard;
      }
   }
   /*********************************************
    * public Card dealCard()
    * 
    *   This method removes the top card from the deck and returns it. If 
    *   the deck is empty, returns an invalid card, 'X' of spades.
    *    
    *   Parameters: none
    *   Returns: Card object
    *********************************************/
   public Card dealCard()
   {
      Card newCard = new Card();
      if (topCard > 0)
      {
         // copy the top card, remove the top card and decrement counter,
         // return the copy
         newCard.set(cards[topCard - 1].getValue(), 
               cards[topCard - 1].getSuit());
         cards[topCard - 1] = null;
         topCard--;
      }
      else
      {
         // Returns an invalid card if deck is empty.
         newCard.set('X', Card.Suit.spades);
      }
      return newCard;
   }
   /*********************************************
    * public int getTopCard()
    * 
    *   Accessor for the instance variable topCard.
    *    
    *   Parameters: none
    *   Returns: topCard - int representing the array index of the top of deck
    * 
    *********************************************/
   public int getTopCard()
   {
      return topCard;
   }

   /*********************************************
    * public Card inspectCard(int k)
    *    
    *   This method returns the card located at index k of the cards array, if
    *   k is a valid index. Otherwise returns an invalid card, 'X' of spades.
    *********************************************/

   public Card inspectCard(int k)
   {
      // Check if k is a valid array index
      if (k < cards.length && k >= 0)
      {
         return cards[k];
      }
      else return new Card('X', Card.Suit.hearts);
   }
}

/*********SAMPLE OUTPUT****************************

K of spades
Q of spades
J of spades
T of spades
9 of spades
8 of spades
7 of spades
6 of spades
5 of spades
4 of spades
3 of spades
2 of spades
A of spades
K of hearts
Q of hearts
J of hearts
T of hearts
9 of hearts
8 of hearts
7 of hearts
6 of hearts
5 of hearts
4 of hearts
3 of hearts
2 of hearts
A of hearts
K of diamonds
Q of diamonds
J of diamonds
T of diamonds
9 of diamonds
8 of diamonds
7 of diamonds
6 of diamonds
5 of diamonds
4 of diamonds
3 of diamonds
2 of diamonds
A of diamonds
K of clubs
Q of clubs
J of clubs
T of clubs
9 of clubs
8 of clubs
7 of clubs
6 of clubs
5 of clubs
4 of clubs
3 of clubs
2 of clubs
A of clubs
K of spades
Q of spades
J of spades
T of spades
9 of spades
8 of spades
7 of spades
6 of spades
5 of spades
4 of spades
3 of spades
2 of spades
A of spades
K of hearts
Q of hearts
J of hearts
T of hearts
9 of hearts
8 of hearts
7 of hearts
6 of hearts
5 of hearts
4 of hearts
3 of hearts
2 of hearts
A of hearts
K of diamonds
Q of diamonds
J of diamonds
T of diamonds
9 of diamonds
8 of diamonds
7 of diamonds
6 of diamonds
5 of diamonds
4 of diamonds
3 of diamonds
2 of diamonds
A of diamonds
K of clubs
Q of clubs
J of clubs
T of clubs
9 of clubs
8 of clubs
7 of clubs
6 of clubs
5 of clubs
4 of clubs
3 of clubs
2 of clubs
A of clubs

7 of hearts
J of diamonds
7 of clubs
6 of hearts
A of hearts
2 of clubs
8 of diamonds
6 of spades
7 of diamonds
3 of hearts
J of clubs
7 of clubs
7 of diamonds
6 of spades
2 of diamonds
K of clubs
T of diamonds
J of clubs
6 of clubs
2 of spades
T of clubs
4 of diamonds
K of clubs
3 of clubs
6 of clubs
A of spades
A of hearts
T of hearts
Q of clubs
4 of clubs
Q of diamonds
8 of spades
3 of spades
A of diamonds
T of clubs
Q of clubs
8 of clubs
A of spades
T of diamonds
3 of hearts
4 of hearts
3 of clubs
Q of spades
7 of spades
A of clubs
9 of clubs
9 of spades
5 of hearts
Q of diamonds
9 of clubs
6 of hearts
4 of spades
J of hearts
5 of diamonds
3 of spades
4 of clubs
T of spades
5 of clubs
8 of hearts
8 of hearts
Q of hearts
2 of clubs
K of spades
3 of diamonds
K of diamonds
A of clubs
9 of spades
9 of hearts
6 of diamonds
5 of spades
2 of spades
2 of diamonds
9 of diamonds
J of hearts
6 of diamonds
2 of hearts
K of diamonds
4 of hearts
7 of hearts
5 of spades
T of hearts
J of diamonds
A of diamonds
J of spades
K of hearts
4 of diamonds
5 of diamonds
T of spades
3 of diamonds
5 of hearts
9 of hearts
K of spades
5 of clubs
2 of hearts
8 of spades
Q of spades
8 of diamonds
8 of clubs
J of spades
K of hearts
4 of spades
Q of hearts
9 of diamonds
7 of spades

K of spades
Q of spades
J of spades
T of spades
9 of spades
8 of spades
7 of spades
6 of spades
5 of spades
4 of spades
3 of spades
2 of spades
A of spades
K of hearts
Q of hearts
J of hearts
T of hearts
9 of hearts
8 of hearts
7 of hearts
6 of hearts
5 of hearts
4 of hearts
3 of hearts
2 of hearts
A of hearts
K of diamonds
Q of diamonds
J of diamonds
T of diamonds
9 of diamonds
8 of diamonds
7 of diamonds
6 of diamonds
5 of diamonds
4 of diamonds
3 of diamonds
2 of diamonds
A of diamonds
K of clubs
Q of clubs
J of clubs
T of clubs
9 of clubs
8 of clubs
7 of clubs
6 of clubs
5 of clubs
4 of clubs
3 of clubs
2 of clubs
A of clubs

5 of clubs
6 of diamonds
5 of spades
A of spades
4 of diamonds
5 of hearts
K of diamonds
Q of clubs
T of clubs
7 of spades
5 of diamonds
Q of spades
9 of clubs
Q of hearts
9 of spades
9 of hearts
J of spades
3 of spades
2 of clubs
9 of diamonds
J of diamonds
4 of spades
K of hearts
2 of spades
K of spades
2 of hearts
A of diamonds
8 of clubs
A of hearts
3 of hearts
K of clubs
8 of hearts
T of spades
4 of hearts
3 of clubs
A of clubs
Q of diamonds
3 of diamonds
7 of clubs
J of clubs
J of hearts
4 of clubs
8 of spades
T of hearts
8 of diamonds
7 of diamonds
7 of hearts
6 of clubs
2 of diamonds
6 of spades
6 of hearts
T of diamonds

How many hands? (Select 1 - 10) 6
Hands from the unshuffled deck: 
Hand 1: (Hand = ( K of spades, 7 of spades, A of spades, 8 of hearts, 2 of hearts, 9 of diamonds, 3 of diamonds, T of clubs, 4 of clubs, 4 of clubs ))

Hand 2: (Hand = ( Q of spades, 6 of spades, K of hearts, 7 of hearts, A of hearts, 8 of diamonds, 2 of diamonds, 9 of clubs, 3 of clubs, 3 of clubs ))

Hand 3: (Hand = ( J of spades, 5 of spades, Q of hearts, 6 of hearts, K of diamonds, 7 of diamonds, A of diamonds, 8 of clubs, 2 of clubs, 2 of clubs ))

Hand 4: (Hand = ( T of spades, 4 of spades, J of hearts, 5 of hearts, Q of diamonds, 6 of diamonds, K of clubs, 7 of clubs, A of clubs, A of clubs ))

Hand 5: (Hand = ( 9 of spades, 3 of spades, T of hearts, 4 of hearts, J of diamonds, 5 of diamonds, Q of clubs, 6 of clubs, A of spades, A of spades ))

Hand 6: (Hand = ( 8 of spades, 2 of spades, 9 of hearts, 3 of hearts, T of diamonds, 4 of diamonds, J of clubs, 5 of clubs, 5 of clubs ))


Hands from the shuffled deck: 
Hand 1: (Hand = ( 5 of clubs, 5 of hearts, 9 of spades, Q of hearts, 5 of diamonds, 2 of spades, A of spades, 7 of clubs, 6 of spades, 6 of spades ))

Hand 2: (Hand = ( 6 of hearts, 8 of spades, A of diamonds, 8 of diamonds, 4 of spades, 2 of clubs, K of clubs, 9 of diamonds, T of hearts, T of hearts ))

Hand 3: (Hand = ( Q of spades, 7 of spades, T of clubs, 4 of clubs, 3 of clubs, K of diamonds, 3 of hearts, 6 of diamonds, 3 of diamonds, 3 of diamonds ))

Hand 4: (Hand = ( K of hearts, T of spades, 4 of diamonds, 7 of hearts, 6 of clubs, 7 of diamonds, 9 of hearts, 2 of diamonds, 8 of clubs, 8 of clubs ))

Hand 5: (Hand = ( A of hearts, Q of clubs, T of diamonds, J of hearts, 4 of hearts, 9 of clubs, 2 of hearts, A of clubs, A of spades, A of spades ))

Hand 6: (Hand = ( Q of diamonds, 8 of hearts, 5 of spades, J of clubs, J of diamonds, J of spades, 3 of spades, K of spades, K of spades ))

*/
