/*
 * Name: Wicaksa Munajat 
 * Class: CST338 Software Design
 * Date: March 13, 2021
 * Program: This Java game allows user to simulate a game of slot machine. The
 * user is prompted an amount they want to bet. Then depending on the result
 * of the pull, they will either win or lose money.
 */

package showName;

import java.util.*;

public class Assig2
{
   public static void main(String[] args)
   {
      int betAmount; // amount that the user bets
      int multiplier; // amount that user bet will be multiplied by
      int winnings;// amount that user wins 
      ThreeString pullString = new ThreeString();
      
      // Main game loop
      do
      {
         betAmount = getBet(); // User makes a bets 
         if (betAmount > 0) // Checks if the user wants to bet (play) or quit
         {
            pullString = pull(); // Simulates a pulling of the slot machine
            multiplier = getPayMultiplier(pullString); // Calculates mult score
            winnings = betAmount * multiplier; // Calcuates total amount user wins
            // If user reaches max number of pulls, it will exit the game
            if (pullString.saveWinnings(winnings) == false) 
               break;
            display(pullString, winnings); // Displays result of pull
         }
      } while (betAmount != 0);
      System.out.println(pullString.toStringWinnings()); 
   }
   
   private static class ThreeString 
   {
      /* This class stores three strings where each string represents the result
       * of a slot machine pull.
       */
      
      // String data
      private String string1;
      private String string2;
      private String string3;
      
      // Integer data
      public static final int MAX_LEN = 20; // Max length of String.
      public static final int MAX_PULLS = 40; // Amount of pulls user has
      private static int [] pullWinnings = new int [MAX_PULLS]; 
      private static int numPulls = 0; // Keeps track of array index. 
      
      // Default Constructor 
      ThreeString() 
      {
         this.string1 = "";
         this.string2 = "";
         this.string3 = ""; 
      }
      
      // Determines whether a string is valid/legal.
      // Returns true if BOTH the string is not null and its length <= MAX_LEN. 
      boolean validString(String str)
      {
         return (!(str == null) && (str.length() <= MAX_LEN)); 
      }
      
      // Accessor Methods
      public String getString1() 
      {
         return string1;
      }
      
      public String getString2()
      {
         return string2;
      }
      
      public String getString3()
      {
         return string3;
      }
      
      // Mutators Methods
      public boolean setString1(String string)
      {
         if (validString(string))
         {
            this.string1 = string;
            return true;
         }
         else
            return false;
      }
      
      public boolean setString2(String string)
      {
         if (validString(string)) 
         {
            this.string2 = string;
            return true;
         }
         else
            return false;
      }
      
      public boolean setString3(String string)
      {
         if (validString(string))
         {
            this.string3 = string;
            return true;
         }
         else 
            return false; 
      }
      
      //Returns the 3 strings as one string
      public String toString()
      {
         return " " + string1 + "  " + string2 + "  " + string3; 
      }
      
      // Saves winnings from the round
      // Returns true if there is space in the array to save the winnings
      public boolean saveWinnings(int winnings) 
      {
         if (numPulls < 40)
          {
            pullWinnings[numPulls] = winnings;
            numPulls++;
            return true; 
          }
         else // no space to store winnings in array
            System.out.println("You've reached the maximum amount of pulls.\n");
            return false;
      }
      
      // Get values and total winnings out of of the array
      public String toStringWinnings()
      {
         int sumWinnings = 0;
         int [] winningsResults = new int [numPulls];
         
         String returnString = "";
         
         for (int i = 0; i < numPulls; i++)
         {
            sumWinnings += pullWinnings[i];
            winningsResults[i] = pullWinnings[i];
         }
         
         returnString = "Thanks for playing at the Casino!" + "\n" + 
            "Your individual winnings were: " + "\n" +
            Arrays.toString(winningsResults) + "\n" + 
            "Your total winnings were: $" + sumWinnings;
         return returnString;
      }
        
   } // End of ThreeString class. 
   
   // Helper methods
   
   // Ask user for an input for how much they want to bet
   // Will ask again if input < 0 or input > 100
   public static int getBet()
   {
      Scanner keyboard = new Scanner(System.in); 
      int userBet;
      
      do 
      {
         System.out.print("How much would you like to bet (1 - 100) "
               + "or 0 to quit? $");
         userBet = keyboard.nextInt();
      } while ((userBet < 0) || (userBet > 100));
      // keyboard.close();
      return userBet;
   }
   
   // Simulates a "pull" of the slot machine
   public static ThreeString pull()
   {
      ThreeString userPull = new ThreeString(); 
      userPull.setString1(randString());
      userPull.setString2(randString());
      userPull.setString3(randString());
      return userPull;
   }
   
   // Generates a random string based on the odds of each string
   private static String randString()
   {
      // odds
      // space (50%) 
      // cherries (25%) 
      // BAR (12.5%)  
      // 7 (12.5%)
      double randomNumber = Math.random() * 100;
      if (randomNumber <= 50.0)
         return "space";
      else if (randomNumber <= 75.0)
         return "cherries";
      else if (randomNumber <= 87.5)
         return "BAR";
      else // randomNumber <= 100.0
         return "7";
   }
   
   // Returns a multiplier depending on the result of the user's pull
   public static int getPayMultiplier(ThreeString thePull)
   {
      //[cherries][not cherries][any] = 5x
      //[cherries][cherries][not cherries] = 15x
      //[cherries][cherries][cherries] = 30x
      //[BAR][BAR][BAR] = 50x
      // 7 7 7 = 100x
      // Anything else = 0x
      
      int multiplier;
      
      // [7] [7] [7]
      if (thePull.getString1().equals("7") && 
         (thePull.getString2().equals("7")) && 
         (thePull.getString3().equals("7")))
      {
         multiplier = 100;    
      }
      
      // [BAR] [BAR] [BAR]
      else if (thePull.getString1().equalsIgnoreCase("bar") && 
         (thePull.getString2().equalsIgnoreCase("bar")) && 
         (thePull.getString3().equalsIgnoreCase("bar")))
      {
         multiplier = 50;
      }
      
      // [cherries] [cherries] [cherries]
      else if (thePull.getString1().equalsIgnoreCase("cherries") && 
         (thePull.getString2().equalsIgnoreCase("cherries")) && 
         (thePull.getString3().equalsIgnoreCase("cherries")))
      {
         multiplier = 30;
      }
      
      // cherries cherries [not cherries]
      else if (thePull.getString1().equalsIgnoreCase("cherries") && 
         (thePull.getString2().equalsIgnoreCase("cherries")) && 
         !(thePull.getString3().equalsIgnoreCase("cherries")))
      {
         multiplier = 15; 
      }
      
      // [cherries] [not cherries] [any]
      else if (thePull.getString1().equalsIgnoreCase("cherries") && 
         !(thePull.getString2().equalsIgnoreCase("cherries")))
      {
         multiplier = 5;
      }
      
      // Any other combination
      else 
      {
         multiplier = 0;
      }
      
      return multiplier; 
   }
   
   // Displays whether or not the user won and how much money the user got. 
   public static void display(ThreeString thePull, int winnings)
   {
      System.out.println("whirrrrrr .... and your pull is ...");
      System.out.println(thePull.toString());
      if (winnings > 0)
         System.out.println("Congratulations, you win: $" + winnings);
      else
         System.out.println("Sorry, you lose.");
      System.out.print("\n");
   }
}

/********************* Output ********************
How much would you like to bet (1 - 100) or 0 to quit? $-111
How much would you like to bet (1 - 100) or 0 to quit? $111
How much would you like to bet (1 - 100) or 0 to quit? $-444
How much would you like to bet (1 - 100) or 0 to quit? $555
How much would you like to bet (1 - 100) or 0 to quit? $9
whirrrrrr .... and your pull is ...
 cherries  7  cherries
Congratulations, you win: $45

How much would you like to bet (1 - 100) or 0 to quit? $4
whirrrrrr .... and your pull is ...
 7  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $22
whirrrrrr .... and your pull is ...
 cherries  BAR  space
Congratulations, you win: $110

How much would you like to bet (1 - 100) or 0 to quit? $99
whirrrrrr .... and your pull is ...
 BAR  space  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $44
whirrrrrr .... and your pull is ...
 space  cherries  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $22
whirrrrrr .... and your pull is ...
 7  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $5
whirrrrrr .... and your pull is ...
 space  7  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $22
whirrrrrr .... and your pull is ...
 cherries  7  cherries
Congratulations, you win: $110

How much would you like to bet (1 - 100) or 0 to quit? $67
whirrrrrr .... and your pull is ...
 space  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $44
whirrrrrr .... and your pull is ...
 space  BAR  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $76
whirrrrrr .... and your pull is ...
 space  space  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $88
whirrrrrr .... and your pull is ...
 space  cherries  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $33
whirrrrrr .... and your pull is ...
 7  space  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $25
whirrrrrr .... and your pull is ...
 space  7  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $100
whirrrrrr .... and your pull is ...
 space  space  7
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $33
whirrrrrr .... and your pull is ...
 BAR  cherries  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $45
whirrrrrr .... and your pull is ...
 cherries  space  cherries
Congratulations, you win: $225

How much would you like to bet (1 - 100) or 0 to quit? $66
whirrrrrr .... and your pull is ...
 space  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $88
whirrrrrr .... and your pull is ...
 space  BAR  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $66
whirrrrrr .... and your pull is ...
 cherries  space  cherries
Congratulations, you win: $330

How much would you like to bet (1 - 100) or 0 to quit? $47
whirrrrrr .... and your pull is ...
 space  BAR  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $88
whirrrrrr .... and your pull is ...
 BAR  7  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $88
whirrrrrr .... and your pull is ...
 BAR  cherries  BAR
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $5
whirrrrrr .... and your pull is ...
 space  BAR  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $44
whirrrrrr .... and your pull is ...
 BAR  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $32
whirrrrrr .... and your pull is ...
 7  space  BAR
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $55
whirrrrrr .... and your pull is ...
 cherries  space  space
Congratulations, you win: $275

How much would you like to bet (1 - 100) or 0 to quit? $266
How much would you like to bet (1 - 100) or 0 to quit? $8
whirrrrrr .... and your pull is ...
 cherries  space  BAR
Congratulations, you win: $40

How much would you like to bet (1 - 100) or 0 to quit? $22
whirrrrrr .... and your pull is ...
 space  BAR  BAR
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $16
whirrrrrr .... and your pull is ...
 space  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $61
whirrrrrr .... and your pull is ...
 cherries  space  BAR
Congratulations, you win: $305

How much would you like to bet (1 - 100) or 0 to quit? $54
whirrrrrr .... and your pull is ...
 BAR  7  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $55
whirrrrrr .... and your pull is ...
 space  cherries  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $76
whirrrrrr .... and your pull is ...
 space  7  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $89
whirrrrrr .... and your pull is ...
 space  cherries  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $91
whirrrrrr .... and your pull is ...
 7  cherries  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $55
whirrrrrr .... and your pull is ...
 7  cherries  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $66
whirrrrrr .... and your pull is ...
 7  7  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $34
whirrrrrr .... and your pull is ...
 7  space  space
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $75
whirrrrrr .... and your pull is ...
 space  space  cherries
Sorry, you lose.

How much would you like to bet (1 - 100) or 0 to quit? $43
You've reached the maximum amount of pulls.

Thanks for playing at the Casino!
Your individual winnings were: 
[45, 0, 110, 0, 0, 0, 0, 110, 0, 0, 0, 0, 0, 0, 0, 0, 225, 0, 0, 330, 
 0, 0, 0, 0, 0, 0, 275, 40, 0, 0, 305, 0, 0, 0, 0, 0, 0, 0, 0, 0]
Your total winnings were: $1440


 */
