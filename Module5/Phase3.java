/***************************************************************************
 *  Paola Torres, Matthew Stoney, Cheuk On Yim, Wicaksa Munajat
 *   CSUMB CST338 Software Design Fall '21 
 * 
 * Phase 3: Suit Match Game. Player and computer take turn to match other suit.
 * Get 2 point where suit is matched, if not, other gets 2 points. 
 ****************************************************************************/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

   static JButton[] humanButtons = new JButton[NUM_CARDS_PER_HAND];

   // get icon dimensions to resize buttons for cards
   public static int buttonHeight = GUICard.getCardBackIcon().getIconHeight();
   public static int buttonWidth = GUICard.getCardBackIcon().getIconWidth();

   static Icon tempComputerIcon; // holds icon of computer's played card each round
   static Icon tempHumanIcon; // holds icon of human's played card each round

   // returns a random card 
   static Card randomCardGenerator()
   {
      Random rand = new Random();
      char randValue = Card.valuRanks[rand.nextInt(13)];
      Card.Suit randSuit = 
            Card.Suit.values()[rand.nextInt(Card.Suit.values().length)];
      return new Card(randValue, randSuit);
   }


   public static void main(String[] args)
   {
      // setup the game
      GUICard.loadCardIcons();
      CardGameOutline SuitMatchGame = 
         new CardGameOutline(1, 2, 0, null, NUM_PLAYERS, NUM_CARDS_PER_HAND);
      SuitMatchGame.deal(); // create numPlayer hands
      CardTable window = 
         new CardTable("Game Window", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      GameControl newGame = new GameControl(SuitMatchGame, window);
   }
}

/***********************************************************************
 * This class sets up the control of the game and includes two
 * constructors. 
 *
 **********************************************************************/

class GameControl extends Main implements ActionListener
{
   CardGameOutline thisGame;
   CardTable window;
   boolean playerTurn;
   Card winnings[][];
   int computerScore, humanScore, turnCounter;
   int positionCountPlayer, positionCountComputer;
   boolean firstDraw, matchedSuits;
   
   GameControl(CardGameOutline thisGame, CardTable window)
   {
      this.thisGame = thisGame;
      this.window = window;
      this.playerTurn = false;
      this.computerScore = 0;
      this.humanScore = 0;
      this.turnCounter = 0;
      this.positionCountPlayer = 0;
      this.positionCountComputer = 0;
      this.firstDraw = true;
      this.matchedSuits = true;

      drawGame(thisGame, window);
      winnings = new Card[thisGame.
         getNumPlayers()][thisGame.getNumCardsRemainingInDeck()];
   }
   
   GameControl()
   {
      this.thisGame = new CardGameOutline();
      this.window = new CardTable();
      this.playerTurn = false;
      this.computerScore = 0;
      this.humanScore = 0;
      this.turnCounter = 0;
      this.positionCountPlayer = 0;
      this.positionCountComputer = 0;
      this.firstDraw = true;
      this.matchedSuits = true;
      
      drawGame(thisGame, window);
      winnings = new Card[thisGame.
         getNumPlayers()][thisGame.getNumCardsRemainingInDeck()];

   }
   
   /*****************************************************************
    * 
    *    This method is used to draw and redraw the GUI.
    * 
    * Parameters: CardGameOutline - the specs for this game
    *             CardTable window - the main game window
    ****************************************************************/
   
   private void drawGame(CardGameOutline thisGame, CardTable window)
   {
      window.clearGUI();
      for (int i = 0; i < thisGame.getHand(0).getNumCards(); i++)
      {
         // populate computer hand with iconBacks
         computerLabels[i] = new JLabel();
         computerLabels[i].setIcon(GUICard.getCardBackIcon());
         window.pnlComputerHand.add(computerLabels[i]);
      }
      
      for (int j = 0; j < thisGame.getHand(1).getNumCards(); j++)
      {
         // create player buttons and add the icons from the cards in their hand
         humanButtons[j] = new JButton();
         Card humanCard = new Card(thisGame.getHand(1).inspectCard(j));
         humanButtons[j].setIcon(GUICard.getIcon(humanCard));
         humanButtons[j].
            setPreferredSize(new Dimension(buttonWidth, buttonHeight));
         window.pnlHumanHand.add(humanButtons[j]);
      }
      
      drawPlayArea();
      drawScoreArea();
      createHumanButtons(this);
      window.revalidate();
      window.repaint();
   }

   /*********************************************************************
    * 
    *    This method adds event listeners to card buttons. Clicking a card button
    *    removes it from the hand, stores the icon in a static Icon variable. 
    *    Then it redraws the game. If the hand is empty, deals new hands from 
    *    the deck.
    * 
    **********************************************************************/

   private void createHumanButtons(ActionListener buttonListener)
   {
      window.pnlHumanHand.removeAll();
      for (int i = 0; i < thisGame.getHand(1).getNumCards(); i++)
      {
         Card tempCard = new Card(thisGame.getHand(1).inspectCard(i));
         Icon tempIcon = new ImageIcon();
         tempIcon = GUICard.getIcon(tempCard);
         
         humanButtons[i] = new JButton(tempIcon);
         humanButtons[i].
            setPreferredSize(new Dimension(Main.buttonWidth, Main.buttonHeight));
         humanButtons[i].setActionCommand(Integer.toString(i));
         humanButtons[i].addActionListener(buttonListener);
         window.pnlHumanHand.add(humanButtons[i]);
      }
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      String cardString = e.getActionCommand();
      int cardIndex = Integer.parseInt(cardString);
      Card humanCard = new Card(humanPlayCard(cardIndex));
      Card computerCard = new Card(computerPlayCard());


      if (thisGame.getHand(0).getNumCards() == 0)
      {
         thisGame.deal();
      }
      
      matchedSuits = playerMatchedSuits(computerCard, humanCard);
      drawGame(thisGame, window);

      if (thisGame.getNumCardsRemainingInDeck() == 0 && 
         thisGame.getHand(1).getNumCards() == 0) 
      {
         gameEndDisplay();
      }

   }
   /*************************************************************************
   * This method adds the labels to the play area.
   * Param: None
   * Returns: void 
   *************************************************************************/
   
   private void drawPlayArea()
   {
      window.pnlPlayArea.removeAll();
      if (firstDraw)
      {
         playLabelText[0] = new JLabel("Computer", JLabel.CENTER);
         playLabelText[1] = new JLabel("You", JLabel.CENTER);
         playedCardLabels[0] = new JLabel(GUICard.getCardBackIcon());
         playedCardLabels[1] = new JLabel(GUICard.getCardBackIcon());
         firstDraw = false;
      }
      if (!firstDraw && matchedSuits)
      {
         playLabelText[0].removeAll();
         playLabelText[1].removeAll();
         playLabelText[0] = new JLabel("", JLabel.CENTER);
         playLabelText[1] = new JLabel("You Win!", JLabel.CENTER);
      }
      else if (!firstDraw && !matchedSuits)
      {
         playLabelText[0].removeAll();
         playLabelText[1].removeAll();
         playLabelText[0] = new JLabel("Computer Wins!", JLabel.CENTER);
         playLabelText[1] = new JLabel("", JLabel.CENTER);
      }

      if (tempComputerIcon != null)
         playedCardLabels[0] = new JLabel(tempComputerIcon, JLabel.CENTER);
      if (tempHumanIcon != null)
         playedCardLabels[1] = new JLabel(tempHumanIcon, JLabel.CENTER);
        
      window.pnlPlayArea.add(playedCardLabels[0]);
      window.pnlPlayArea.add(playedCardLabels[1]);
      window.pnlPlayArea.add(playLabelText[0]);
      window.pnlPlayArea.add(playLabelText[1]);
   }
     
   /********************************************************************
   * This method checks if the cards in the play area are matched. 
   * If it is the player's turn and the suits are matched, 
   * the player takes the cards and receives the points. otherwise, 
   * the computer takes the cards and points. 
   * It functions the same when it is the computer's turn except the roles
   * are reversed. 

   * Param: Card, Card
   * Returns: boolean 
  *************************************************************************/
   
   private boolean playerMatchedSuits(Card computerCard, Card humanCard) 
   {
      if (playersTurn() == true) 
      {
         if (computerCard.getSuit() == humanCard.getSuit()) 
         {
            winnings[1][positionCountPlayer] = humanCard;
            positionCountPlayer++;
            winnings[1][positionCountPlayer] = computerCard;
            positionCountPlayer++;
            humanScore += 2;
            
            return true;
         }
         else 
         {
            winnings[0][positionCountComputer] = computerCard;
            positionCountComputer++;
            winnings[0][positionCountComputer] = humanCard;
            positionCountComputer++;
            computerScore += 2;
            
            return false;
         }
         
      }
      else 
      {
         if (computerCard.getSuit() == humanCard.getSuit()) 
         {
            winnings[0][positionCountComputer] = computerCard;
            positionCountComputer++;
            winnings[0][positionCountComputer] = humanCard;
            positionCountComputer++;
            computerScore += 2;
            
            return false;
         }
         else 
         {
            winnings[1][positionCountPlayer] = humanCard;
            positionCountPlayer++;
            winnings[1][positionCountPlayer] = computerCard;
            positionCountPlayer++;
            humanScore += 2;
            
            return true;
         }
         
      }

   }

   /****************************************************************************
   * This method displays the final score and winner once the cards in the deck
   * run out. 
   * 
   * Param: None
   * Returns: Void 
   ***************************************************************************/
   
   private void gameEndDisplay()
   {
      if (positionCountPlayer > positionCountComputer) 
      {
         
         window.pnlPlayArea.remove(playedCardLabels[0]);
         window.pnlPlayArea.remove(playedCardLabels[1]);
         window.pnlPlayArea.remove(playLabelText[0]);
         window.pnlPlayArea.remove(playLabelText[1]);
         
         playedCardLabels[0] = new JLabel("", JLabel.CENTER);
         playedCardLabels[1] = new JLabel("", JLabel.CENTER);
         playLabelText[0] = new JLabel("", JLabel.CENTER);
         playLabelText[1] = new JLabel("You win the game! Score: " 
            + positionCountPlayer, JLabel.CENTER);
         
         window.pnlPlayArea.add(playLabelText[0]);
         window.pnlPlayArea.add(playLabelText[1]);
         window.revalidate();
         window.repaint();
      }
      else 
      {
         
         window.pnlPlayArea.remove(playedCardLabels[0]);
         window.pnlPlayArea.remove(playedCardLabels[1]);
         window.pnlPlayArea.remove(playLabelText[0]);
         window.pnlPlayArea.remove(playLabelText[1]);
         
         playedCardLabels[0] = new JLabel("", JLabel.CENTER);
         playedCardLabels[1] = new JLabel("", JLabel.CENTER);
         playLabelText[0] = new JLabel("", JLabel.CENTER);
         playLabelText[1] = new JLabel("The computer wins the game. Score: " 
            + positionCountComputer, JLabel.CENTER);
         
         window.pnlPlayArea.add(playLabelText[0]);
         window.pnlPlayArea.add(playLabelText[1]);
         window.revalidate();
         window.repaint();
      }
   }

 /*****************************************************************************
 * This method sets the condition for turns
 * The player always goes first 
 *
 * Param: None
 * Returns: boolean 
 *****************************************************************************/
   
   private boolean playersTurn() 
   {
      if (turnCounter % 2 == 0) 
      {
         turnCounter++;
         return true;
      }
      turnCounter++;
      return false;
   }
   
   /*****************************************************************
   * This method draws the play area 
   * 
   * Param: None
   * Returns: Void 
   ******************************************************************/
   
   private void drawScoreArea()
   {
      JLabel cpuScoreLabel = new JLabel("Computer Score: " 
         + computerScore, JLabel.CENTER);
      JLabel humanScoreLabel = new JLabel("Your Score: " 
         + humanScore, JLabel.CENTER);
      window.pnlScoreArea.add(cpuScoreLabel);
      window.pnlScoreArea.add(humanScoreLabel);
   }

   /***************************************************************
   * This method creates and assigns the computer's card. 
   *
   * Param: None 
   * Returns: Card 
   ****************************************************************/
   
   private Card computerPlayCard()
   {
      Card computerCard = new Card(thisGame.getHand(0).playCard());
      Main.tempComputerIcon = GUICard.getIcon(computerCard);
      return computerCard;
   }

   /****************************************************************
   * This method creates and assigns the player's card 
   * 
   * Param: Integer 
   * Returns: Card 
   ****************************************************************/
   
   private Card humanPlayCard(Integer innerI)
   {
      Card humanCard = new Card(thisGame.getHand(1).playCard(innerI));
      Main.tempHumanIcon = GUICard.getIcon(humanCard);
      return humanCard;

   }


}
//class CardGameOutline  ----------------------------------------------------
class CardGameOutline
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
   // in the game.  e.g. pinochle does not
   // use cards 2-8 of any suit

   public CardGameOutline( int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameOutline()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() { return deck.dealCard(); }

   public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
            cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);      
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }


   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }

   int getnumPacks()
   {
      return this.numPacks;
   }
   int getNumJokers()
   {
      return this.numJokersPerPack;
   }
   int getNumPlayers()
   {
      return this.numPlayers;
   }
}



/**********************************************************************
 * 
 * 
 *    END PHASE 3
 * 
 * 
 *
 ***********************************************************************/
class CardTable extends JFrame
{
   private static final long serialVersionUID = 1L;
   static int MAX_CARS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand;
   public JPanel pnlPlayArea, pnlScoreArea, pnlPreviousHand;

   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      this.setTitle(title);
      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;
      this.setSize(800, 650);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);
      this.setLayout(new BorderLayout());
      

      
      pnlComputerHand = new JPanel();
      pnlPlayArea = new JPanel(new GridLayout(2, numPlayers));
      pnlHumanHand = new JPanel();
      pnlScoreArea = new JPanel(new GridLayout(2, 1, 20, 10));
      
      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlHumanHand.setBorder(new TitledBorder("Your Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));
      pnlScoreArea.setBorder(new TitledBorder("Score"));
      
      pnlScoreArea.setPreferredSize(new Dimension(150, 200));
      
      this.add(pnlComputerHand, BorderLayout.NORTH);
      this.add(pnlHumanHand, BorderLayout.SOUTH);
      this.add(pnlPlayArea, BorderLayout.CENTER);
      this.add(pnlScoreArea, BorderLayout.EAST);
   }
   CardTable()
   {
      this("Game", 7, 2);
   }

   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }
   public int getNumPlayers()
   {
      return numPlayers;
   }
   public void clearGUI()
   {
      pnlComputerHand.removeAll();
      pnlHumanHand.removeAll();
      pnlPlayArea.removeAll();
      pnlScoreArea.removeAll();
   }


}

class GUICard
{
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;

   static void loadCardIcons()
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
      iconBack = new ImageIcon("images/BK.gif");
      iconsLoaded = true;
   }

   public static Icon getIcon(Card card)
   {
      loadCardIcons();
      return iconCards[card.getValueAsInt()][card.getSuitAsInt()]; 
   }
   public static Icon getCardBackIcon()
   {
      loadCardIcons();
      return iconBack;
   }

}

class Card
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
      return (this.value == card.value && this.suit == card.suit);
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

   static void arraySort(Card[] cards, int arraySize)
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
class Hand
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

      for(int i = 0; i < numCards - 1; i++)
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

   public void sort()
   {
      Card.arraySort(myCards, myCards.length);
   }
}

/*****************************************************************
 * 
 * 
 * 
 *       DECK CLASS
 *       
 *       
 ****************************************************************/

class Deck
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
      init(numPacks);
   }
   Deck(int numPacks)
   {
      allocateMasterPack();
      cards = new Card[56 * numPacks];
      this.numPacks = numPacks;
      init(numPacks);
   }

   public void init(int numPacks)
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

         cards[topCard] = new Card(card); // not sure about topCard - 1
         topCard++;
         return true;
      }
      else
         System.out.println("Too many of that card!");
      return false;
   }

   public boolean removeCard(Card card)
   {
      for (int i = 0; i < topCard; i++)
      {
         if (cards[i].equals(card))
         {
            cards[i] = new Card(cards[topCard - 1]);
            cards[topCard - 1] = null;
            topCard--;
            return true;
         }
      }
      return false;
   }

   void sort()
   {
      Card.arraySort(cards, this.topCard);
   }

   int getNumCards()
   {
      return this.topCard;
   }

}
