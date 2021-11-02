/*
 * Name: Wicaksa Munajat
 * Class: CST338 Software Design
 * School/Year: CSUMB Spring 2021
 * Date: March 4, 2021
 * Program: (1) Asks user to enter full name which will be printed on the screen
 * in both upper case and lower case. (2) Asks user to enter the amount of hours
 * spent studying weekly to three decimal places and will print out the amount
 * of hours to one decimal place using Decimal Format Class.
 */

package showName;

import java.util.Scanner;
import java.text.DecimalFormat;
            
public class Module1
{
   public static void main(String[] args)
   {
      // ******************** Part 1 Start ******************** //
      // Part 1 asks the user to enter their full name which will be printed
      // on the screen in both upper case and lower case characters.

      // Ask user to enter first name and last name.
      // Ask user to capitalize first letter of each name.
      // Store names in separate string variables.
      Scanner keyboard = new Scanner(System.in);
      System.out.println("Enter your first name. Capitalize the first letter: ");
      String firstName = keyboard.next();
      System.out.println("Enter your last name. Capitalize the first letter: ");
      String lastName = keyboard.next();

      // Concatenate names and save into one variable.
      String fullName = firstName + " " + lastName;

      // Show fullName and the length in console.
      System.out.println("Your name is " + fullName + ".");
      System.out.println("The length of your name is " + (fullName.length() - 1) + "."); // -1 to disregard the " ".

      // Convert names to upper and lower case. Show in console.
      String fullNameUpperCase = fullName.toUpperCase();
      String fullNameLowerCase = fullName.toLowerCase();
      System.out.println("Your name in upper case: " + fullNameUpperCase + ".");
      System.out.println("Your name in lower case: " + fullNameLowerCase + ".");

      // ******************** Part 1 End ******************** //

      // ******************** Part 2 Start ******************** //
      // Part 2 asks the user to enter the amount of hours they spend studying
      // per week to three decimal places. It will print out the amount of hours
      // to one decimal place using the Decimal Format Class.

      // Create 2 static final variables to represent the number of hours
      // you should spend each week for class.
      final int MIN_HOURS = 12;
      final int MAX_HOURS = 20;

      // Print that range and then ask the user to enter how many hours they
      // have spent this week to 3 decimal places.
      System.out.println("You should be studying " + MIN_HOURS + " to " + MAX_HOURS + " each week for class.");
      System.out.println("How many hours did you spend on class this week?");
      System.out.println("Please enter your answer to 3 decimal places.");
      double studentHours = keyboard.nextFloat();

      // Use the Decimal Format class to print the rounded value of the user's
      // hours to 1 decimal place.
      DecimalFormat pattern0dot0 = new DecimalFormat("0.0");
      System.out.println("You study " + pattern0dot0.format(studentHours) + " hours per week.");
      // ******************** Part 2 End ******************** //
   } // End of main().
}

   /********************
    * Output ******************** 
    * Run1:
    * 
    * Enter your first name. Capitalize the first letter: Wicaksa Enter your last
    * name. Capitalize the first letter: Munajat Your name is Wicaksa Munajat. The
    * length of your name is 14. Your name in upper case: WICAKSA MUNAJAT. Your
    * name in lower case: wicaksa munajat. You should be studying 12 to 20 each
    * week for class. How many hours did you spend on class this week? Please enter
    * your answer to 3 decimal places. 13.654 You study 13.7 hours per week.
    * 
    * Run2:
    * 
    * Enter your first name. Capitalize the first letter: John Enter your last
    * name. Capitalize the first letter: Cena Your name is John Cena. The length of
    * your name is 8. Your name in upper case: JOHN CENA. Your name in lower case:
    * john cena. You should be studying 12 to 20 each week for class. How many
    * hours did you spend on class this week? Please enter your answer to 3 decimal
    * places. 16.789 You study 16.8 hours per week.
    * 
    * Run3:
    * 
    * Enter your first name. Capitalize the first letter: Hubert Enter your last
    * name. Capitalize the first letter: Wolfeschlegelsteinhausenbergerdorff Your
    * name is Hubert Wolfeschlegelsteinhausenbergerdorff. The length of your name
    * is 41. Your name in upper case: HUBERT WOLFESCHLEGELSTEINHAUSENBERGERDORFF.
    * Your name in lower case: hubert wolfeschlegelsteinhausenbergerdorff. You
    * should be studying 12 to 20 each week for class. How many hours did you spend
    * on class this week? Please enter your answer to 3 decimal places.
    ******************** 
    * Output
    ********************/
