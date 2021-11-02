/**************************************************************
 * Wicaksa Munajat, Cheuk On Yim, Matthew Stoney, Paola Torres 
 * CST 338
 * Assignment 4: Optical Barcode Reader
 * 
 *    This program converts two-dimensional DataMatrix images
 *    into readable text, and vise versa.
 **************************************************************/


class Assig4 
{
   public static void main(String[] args) 
   {
      String[] sImageIn =
         {
               "                                               ",
               "                                               ",
               "                                               ",
               "     * * * * * * * * * * * * * * * * * * * * * ",
               "     *                                       * ",
               "     ****** **** ****** ******* ** *** *****   ",
               "     *     *    ****************************** ",
               "     * **    * *        **  *    * * *   *     ",
               "     *   *    *  *****    *   * *   *  **  *** ",
               "     *  **     * *** **   **  *    **  ***  *  ",
               "     ***  * **   **  *   ****    *  *  ** * ** ",
               "     *****  ***  *  * *   ** ** **  *   * *    ",
               "     ***************************************** ",  
               "                                               ",
               "                                               ",
               "                                               "

         };      



      String[] sImageIn_2 =
         {
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    ",
               "                                          ",
               "                                          ",
               "                                          ",
               "                                          "

         };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

   }
}

/***********************************************************************
 *   
 *   An Interface (which you define) called BarcodeIO that defines the 
 *   I/O and basic methods of any barcode class which might implement it.
 *   
 *   Any class that implements BarcodeIO is expected to store some 
 *   version of an image and some version of the text associated with  
 *   that image. 
 *
 ***********************************************************************/
interface BarcodeIO 
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText(); 
   public boolean translateImageToText(); 
   public void displayTextToConsole();
   public void displayImageToConsole(); 
}

/***********************************************************************
 *   
 *   This class will realize all the essential data and methods
 *   associated with a 2D pattern, thought of conceptually as an image
 *   of a square or rectangular bar code.  Here are the essential 
 *   ingredients. This class has very little "smarts" in it, except for
 *   the parameterized constructor. It mostly just stores and retrieves
 *   2D data. 
 * 
 *   Remember: BarcodeImage implements Cloneable.
 *
 ***********************************************************************/

class BarcodeImage implements Cloneable 
{
   // The exact internal dimension of 2D data (1950)
   public static final int MAX_HEIGHT = 30; // row
   public static final int MAX_WIDTH = 65; // col 

   // This is where image will be stored
   // false for white elements
   // true for black elements
   private boolean [][] imageData; 

   /***********************************************************************
    *   
    *   This method is the default constructor that instantiates a 2D array 
    *   with dimensions of (MAX_HEIGHT x MAX_WIDTH) and fills it all with 
    *   blanks (false).
    *
    ***********************************************************************/
   BarcodeImage()
   {
      this.imageData = new boolean [MAX_HEIGHT][MAX_WIDTH];
      initialize();
   }

   /***********************************************************************
    *   
    *   This constructor method takes a 1D array of String and converts it 
    *   to the internal 2D array of booleans. 
    *
    *   Warning: The incoming image might not be the same size as the matrix.
    *   To solve this issue, you have to pack it into the lower-left corner 
    *   of the double array. The DataMatrix class will make sure that there 
    *   is no extra space below or left of the image so this constructor can 
    *   put it into the lower-left corner of the array.
    *   
    ***********************************************************************/
   BarcodeImage(String [] strData) 
   {
      this.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];

      if (!checkSize(strData))
      {
         System.out.println("Data too large! Resetting image array.");
         initialize();
         return;
      }

      for (int row = MAX_HEIGHT - 1, strCtr = strData.length - 1; 
            strCtr >= 0; row--, strCtr--)
      {
         for (int col = 0, charCtr = 0; charCtr < strData[strCtr].length(); 
               col++, charCtr++)
         {
            if (strData[strCtr].charAt(charCtr) == DataMatrix.BLACK_CHAR)
            {
               imageData[row][col] = true;
            }
            else
            {
               imageData[row][col] = false;
            }
         }
      }
   }
   /***********************************************************************
    *   This method will set all values in the imageData array to false.
    *
    ***********************************************************************/
   public void initialize()
   {
      for (int height = 0; height < MAX_HEIGHT; height++) // row
      {
         for (int width = 0; width < MAX_WIDTH; width++) // col
         {
            setPixel(height, width, false); // [row][col]
         }
      }
   }
   /***********************************************************************
    *   
    *   This accessor method returns true if the pixel in the coordinate is
    *   black and false if the pixel in the coordinate is white. It will return
    *   false if the coordinate is out of bounds of the array.
    *
    *   Parameters: 2 int values
    *   Returns: Boolean value
    *
    ***********************************************************************/
   public boolean getPixel(int row, int col) 
   {
      if (row < MAX_HEIGHT && row >= 0 && col < MAX_WIDTH && col >= 0)
         return (this.imageData[row][col]);
      else  // out of bounds 
         return false;
   }

   /***********************************************************************
    *   
    *   This mutator method sets the pixel value (true or false) in the 
    *   coordinates entered. Returns false if coordinates are invalid.
    *
    *   Parameters: 2 int values, 1 boolean value 
    *   Returns: Boolean value 
    *
    ***********************************************************************/
   public boolean setPixel(int row, int col, boolean value)
   {
      if (row < MAX_HEIGHT && row >= 0 && col < MAX_WIDTH && col >= 0)
      {
         imageData[row][col] = value; 
         return true;
      }
      else // out of bounds
         return false; 
   }

   /***********************************************************************
    *   
    *  A utility method that checks incoming data for every conceivable size 
    *  or null error. Smaller is OK. Bigger or null is NOT. 
    *
    ***********************************************************************/
   private boolean checkSize(String [] data)
   {
      // check for null array
      if (data == null)
      {
         return false;
      }
      // check if array has too many rows
      if (data.length > MAX_HEIGHT)
      {
         return false;
      }
      // check if any string elements exceed maximum width
      for (int j = 0; j < data.length; j++)
      {
         if (data[j].length() > MAX_WIDTH)
         {
            return false;
         }
      }
      return true;
   }

   /***********************************************************************
    *   
    *  A method that helps with debugging (OPTIONAL) 
    *
    ***********************************************************************/
   public void displayToConsole() 
   {
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            if (imageData[row][col])
            {
               System.out.print('*');
            }
            else
            {
               System.out.print(' '); // a white space char 
            }
         }
         System.out.println();
      }
   }
   /***********************************************************************
    *   
    *  A method that overrides the method of that name in Cloneable interface.
    *  This method will return a clone of the BarcodeImage object. It also 
    *  clones the imageData array to avoid privacy leaks. Throws exception 
    *  if Cloneable interface is not implemented.
    *
    ***********************************************************************/ 
   public Object clone() throws CloneNotSupportedException
   {
      BarcodeImage copy = (BarcodeImage)super.clone();
      //create deep copy of imageData[][];
      copy.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for (int row = 0; row < MAX_HEIGHT; row++)
      {
         for (int col = 0; col < MAX_WIDTH; col++)
         {
            copy.imageData[row][col] = this.imageData[row][col];
         }
      }  

      return copy;
   }
}

/***********************************************************************
 * 
 * This class is a pseudo Datamatrix data structure, not a true Datamatrix,
 * because it does not contain any error correction or encoding. However, it
 * does have the 2D array format and a left and bottom BLACK "spine" as well as
 * an alternating right and top BLACK-WHITE pattern as seen in the image.
 *
 ***********************************************************************/


class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth = 0; 
   private int actualHeight = 0;
   private String invalidText = "INVALID TEXT"; 

   /***********************************************************************
    * 
    *   This default constructor constructs an EMPTY, NON-NULL, image and 
    *   text value. The INITIAL IMAGE should be all WHITE. actualWidth and 
    *   actualHeight should start at 0. The text is set to "undefined".
    *
    *   Parameters: None
    *   Returns: None
    *
    ***********************************************************************/
   DataMatrix()
   {
      this.image = new BarcodeImage(); 
      this.text = "undefined";
   }

   /***********************************************************************
    * 
    *   This constructor sets the image but leaves the text at its default 
    *   value. Calls on the scan(). 
    *
    *   Parameters: BarcodeImage object
    *   Returns: None 
    ***********************************************************************/
   DataMatrix(BarcodeImage image)
   {
      this.scan(image);
      this.text = "undefined";
   }

   DataMatrix(String text)
   {
      if(!this.readText(text))
      {
         System.out.println(invalidText);
      }
      this.image = new BarcodeImage();
   }

   /***********************************************************************
    * 
    *   This method sets the String variable text.
    *
    *   Parameters: String 
    *   Returns: True if valid string, false if null string
    *
    ***********************************************************************/
   public boolean readText(String text)
   {
      if (text == null || text.length() > BarcodeImage.MAX_WIDTH)
      {
         this.text = invalidText;
         this.image = new BarcodeImage();
         this.actualHeight = 0;
         this.actualWidth = 0;
         return false;
      }
      else
      {
         this.text = text;
         return true;
      }
   }

   /***********************************************************************
    * 
    *   This method clones the BarcodeImage passed into it. It then cleans the 
    *   image and places it in the bottom left of the 2D imageData array. It
    *   also calculates the height and width of the image signal.
    *
    *   Parameters: BarcodeImage object
    *   Returns: False if argument is null, true otherwise.
    *
    ***********************************************************************/
   public boolean scan(BarcodeImage image)
   {  
      if(image != null) 
      {
         try
         {
            this.image = (BarcodeImage)image.clone();
         }
         catch (CloneNotSupportedException e)
         {

         }
         cleanImage();
         this.actualHeight = computeSignalHeight();
         this.actualWidth = computeSignalWidth();
         return true;
      }
      return false;

   }

   /*********************************************************
    *   This method moves the signal image to the lower left corner
    *   of the image array.
    *
    *   Parameters: none
    *   Returns: none 
    **********************************************************/

   private void cleanImage()
   {
      moveImageToLowerLeft();
   }
   /****************************************
    *   This method searches every row starting at the bottom to find
    *   the first element that is set to TRUE. It then uses those coordinates
    *   to calculate the offset to shift the signal down and left by.
    *   
    *   Parameters: none
    *   Returns: none
    *****************************************/
   private void moveImageToLowerLeft() 
   {
      int offsetRow = 0, offsetCol = 0;
      // exits loop if loopEnd set to true
      boolean loopEnd = false;

      // this loop calculates offsets by finding first lower-left 
      // pixel that is true
      for (int row = BarcodeImage.MAX_HEIGHT - 1; row >= 0 && loopEnd == false; 
            row--)
      {
         for (int col = 0; col < BarcodeImage.MAX_WIDTH - 1; col++)
         {
            if (image.getPixel(row, col) == true)
            {
               // If a TRUE element found, determine offsets and exit loops
               offsetRow = BarcodeImage.MAX_HEIGHT - 1 - row;
               offsetCol = col;
               loopEnd = true;
               break;
            }
         }
      }
      shiftImageDown(offsetRow);
      shiftImageLeft(offsetCol);
   }
   /***********************************************************************
    *  This method shifts the signal downward by the number of rows 
    *  equal to offset
    *
    *  Parameters: int offset - number of rows to shift the image
    *  Returns: none
    **********************************************************************/
   private void shiftImageDown(int offset)
   {
      for (int row = BarcodeImage.MAX_HEIGHT - 1; row - offset >= 0; row--)
      {
         for (int col = 0; col < BarcodeImage.MAX_WIDTH - 1; col++)
         {
            image.setPixel(row, col, image.getPixel(row - offset, col));
         }
      }
   }
   /***********************************************************************
    *  This method shifts the signal leftward by the number of columns equal 
    *  to offset
    *
    *  Parameters: int offset - number of rows to shift the image
    *  Returns: none
    **********************************************************************/
   private void shiftImageLeft(int offset)
   {
      for (int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
      {
         for (int col = 0; col < BarcodeImage.MAX_WIDTH - offset; col++)
         {
            image.setPixel(row,  col, image.getPixel(row, col + offset));
         }
      }
   }

   /***********************************************************************
    * 
    *   This is an accessor method for actualHeight. 
    *
    *   Parameters: None
    *   Returns: integer 
    ***********************************************************************/
   public int getActualHeight()
   {
      return this.actualHeight;
   }
   /***********************************************************************
    * 
    *   This is an accessor method for actualWidth.
    *
    *   Parameters: none
    *   Returns: integer
    *
    ***********************************************************************/
   public int getActualWidth()
   {
      return this.actualWidth;
   }

   /***********************************************************************
    * 
    *   This method uses the bottom "spine" of the array to determine the 
    *   actual width of the image. 
    *
    *   Parameters: None
    *   Returns: integer
    *
    ***********************************************************************/
   private int computeSignalWidth()
   {
      int signalWidth = 0;
      int col = 0;

      while(this.image.getPixel(BarcodeImage.MAX_HEIGHT - 1, col)) {
         col++;
         signalWidth++;
      }
      return signalWidth;
   }

   /***********************************************************************
    * 
    *   This method calculates the height of the signal image.
    *
    *   Parameters: none
    *   Returns: integer
    *
    ***********************************************************************/
   private int computeSignalHeight()
   {
      int signalHeight = 0;
      int row = BarcodeImage.MAX_HEIGHT - 1;

      while(this.image.getPixel(row, 0)) {
         row--;
         signalHeight++;
      }
      return signalHeight;
   }

   /***********************************************************************
    * 
    *   This method looks at this.text and generate a BarcodeImage for 
    *   this.image.
    *
    *   Parameters: None
    *   Returns: None 
    *
    ***********************************************************************/
   public boolean generateImageFromText()
   {
      if(this.text != null && !this.text.equals(invalidText)) 
      {    
         //Create a new image
         BarcodeImage newImage = new BarcodeImage();
         this.image = newImage;

         //fill all boarder spines
         fillSpines();

         //get char at each index and turn char into asciiValue, then pass to
         //writeCharToCol()
         int txtLen = this.text.length();
         for(int col = 1, i = 0; col <= txtLen && i < txtLen; col++, i++)
         {
            int asciiValue = this.text.charAt(i);
            writeCharToCol(col, asciiValue);
         }
         cleanImage();
         this.actualHeight = computeSignalHeight();
         this.actualWidth = computeSignalWidth();

         return true;
      }
      else
      {
         return false;
      }
   }

   /***********************************************************************
    * 
    *   This method draws the border around the signal image.
    *
    *   Parameters: None
    *   Returns: None 
    *
    ***********************************************************************/
   private void fillSpines()
   {  
      //fill top
      for(int topRowCol = 0; topRowCol < this.text.length() + 2; topRowCol++)
      {
         if(topRowCol % 2 == 0)
            this.image.setPixel(0, topRowCol, true);
         else
            this.image.setPixel(0, topRowCol, false);
      }

      //fill left spine
      for(int row = 0; row < this.actualHeight; row++)
      {
         this.image.setPixel(row, 0, true);
      }

      //fill right spine
      for(int row = 0; row < this.actualHeight; row++)
      {
         if(row % 2 == 0)
            this.image.setPixel(row, this.text.length() + 1, false);
         else
            this.image.setPixel(row, this.text.length() + 1, true);
      }

      //fill bottom spine
      for(int bottomRowCol = 0; bottomRowCol < this.text.length() + 2; 
            bottomRowCol++)
      {
         this.image.setPixel(9, bottomRowCol, true);
      }
   }

   /***********************************************************************
    * 
    *   This is a helper method for writing a char into a binary 
    *   representation that will be placed into the image column.
    *
    *   Parameters: int col - column to place binary into
    *               int code - int representation of a char
    *   Returns:    boolean
    ***********************************************************************/
   private boolean writeCharToCol(int col, int code) 
   {
      if(code >= 0 && code <= 255)
      {
         String binaryString = Integer.toBinaryString(code);
         for(int row = 8, i = binaryString.length() - 1; i >= 0; i--, row--) 
         {
            if(Character.compare(binaryString.charAt(i), '1') == 0) 
            {
               this.image.setPixel(row, col, true);
            }
            else
            {
               this.image.setPixel(row, col, false);
            } 
         }
         return true;
      }  
      return false;   
   }

   /***********************************************************************
    * 
    * This method converts an image into its corresponding text 
    * Parameters: None 
    * Returns: true if the image is not null and false otherwise 
    *
    ***********************************************************************/
   public boolean translateImageToText()
   {
      if(this.image != null) 
      {
         String newText = "";
         for(int i = 1; i < this.actualWidth-1; i++)
         {
            newText += Character.toString(readCharFromCol(i));
         }
         this.text = newText;
         return true;
      }
      return false;         
   }

   /***********************************************************************
    * 
    * This method reads the values of a given column in an image 
    * Parameter: int 
    * Returns: char 
    *
    ***********************************************************************/
   private char readCharFromCol(int col) {

      int asciiValue = 0;

      for (int row = BarcodeImage.MAX_HEIGHT - 2, exp = 0; 
            row > BarcodeImage.MAX_HEIGHT - this.actualHeight; row--, exp++)
      {
         if(this.image.getPixel(row, col))
         {   
            asciiValue += Math.pow(2, exp);
         }  
      }
      return (char)asciiValue;
   }

   /***********************************************************************
    * 
    * This method prints the text to console
    * Parameters: None 
    * Returns: void 
    *
    ***********************************************************************/
   public void displayTextToConsole()
   {
      System.out.println(this.text);
   }

   /***********************************************************************
    * 
    * This method prints the image along with its corresponding boarders 
    * Parameter: None 
    * Returns: void 
    *
    ***********************************************************************/
   public void displayImageToConsole()
   {  
      if(this.text.equals(invalidText))
      {
         System.out.println("Cannot display image from invalid text.");
      }
      else
      {
         //print top boarder
         String upperBoarder = "";
         for(int i = 0; i < this.actualWidth + 2; i++)
         {
            upperBoarder += "-";
         }
         System.out.println(upperBoarder);

         //print content row by row, wrapped between two '|'s
         for(int row = 0; row < this.actualHeight; row++)
         {
            String contentRow = "|";

            for(int col = 0; col < this.actualWidth; col++)
            {
               if(this.image.getPixel(BarcodeImage.MAX_HEIGHT 
                     - this.actualHeight + row , col))           
               {
                  contentRow +=  BLACK_CHAR;           
               }
               else
               {
                  contentRow += WHITE_CHAR;      
               }
            }
            contentRow += "|";
            System.out.println(contentRow);
         }
      }  
   }
}

/***************SAMPLE OUTPUT*********************

CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
What a great resume builder this is!
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|***** * ***** ****** ******* **** **  |
|* ************************************|
|**  *    *  * * **    *    * *  *  *  |
|* *               *    **     **  *  *|
|**  *   * * *  * ***  * ***  *        |
|**      **    * *    *     *    *  * *|
|** *  * * **   *****  **  *    ** *** |
|**************************************|

******************************************************/
