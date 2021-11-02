
/******************************************************************************
 * 
 *   Phase 1 Reading and Displaying the .gif Files
 *   Paola Torres, Matthew Stoney, Cheuk On Yim, Wicaksa Munajat
 *   CSUMB CST338 Software Design Fall '21 
 * 
 ******************************************************************************/

import javax.swing.*;
import java.awt.*;

public class Assignment5
{
   // static for the 57 icons and their corresponding labels
   private static final int NUM_CARD_IMAGES = 57; // 52 + 4 jokers + 1 back-of-card image
   private static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];

   // build the file names ("AC.gif", "2C.gif", "3C.gif", "TC.gif", etc.)
   private static void loadCardIcons()
   {
      String strFileName = ""; // file name
      final int NUM_VAL = 14; // number of values
      final int NUM_SUIT = 4; // number of suits
      int intCard = 0; // current index of the icon array

      do
      {
         for (int val = 0; val < NUM_VAL; val++)
         {
            for (int suit = 0; suit < NUM_SUIT; suit++)
            {
               // Create the file name
               strFileName = "images/" + turnIntIntoCardValue(val) + 
                                         turnIntIntoCardSuit(suit) + ".gif";

               // Create a new icon
               icon[intCard] = new ImageIcon(strFileName);

               // increment the index
               intCard++;
            }
         }
         // Add the back of the card
         icon[intCard] = new ImageIcon("images/BK.gif");

      } while (intCard < icon.length - 1);
   }

   // A helper method that converts int into card value
   private static String turnIntIntoCardValue(int k)
   {
      String[] valueArr =
      { "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A", "X" };
      String strVal = valueArr[k];

      // Check bounds
      if (k < 0 || k > valueArr.length)
      {
         return "Error";
      }
      return strVal;
   }

   // A helper method that converts int into card suit
   private static String turnIntIntoCardSuit(int j)
   {
      String[] suitArr =
      { "C", "D", "H", "S" };
      String strSuit = suitArr[j];

      // Check bounds
      if (j < 0 || j > suitArr.length)
      {
         return "Error";
      }
      return strSuit;
   }

   // a simple main to throw all the JLabels out there for the world to see
   public static void main(String[] args)
   {
      int k;

      // prepare the image icon array
      loadCardIcons();

      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
      frmMyWindow.setLayout(layout);

      // prepare the image label array
      JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         labels[k] = new JLabel(icon[k]);

      // place your 3 controls into frame
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         frmMyWindow.add(labels[k]);

      // show everything to the user
      frmMyWindow.setVisible(true);
   }
}
