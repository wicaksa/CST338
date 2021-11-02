# Deck of Cards Lab
This week's lab is a team project. This lab takes a look at creating different classes that can be used for future programs that involve playing card games with a human,
or simulating card games entirely by a computer. The classes that we built this week include:

## Card
This class represents a playing card. This class has two members: **value**(char) **suit**(enum). 
In addition, this class contains an **errorFlag**(boolean) which tells the client that a card is illegal.
Method includes **constructors, mutators, acccessors, and toString()**. No jokers or special cards are included. 

## Hand
This class represents a client's hand that holds many cards. The Hand object is represented by an array of Card objects. 
Methods include **takeCard(), playCard()** in order to take a card from the deck or elsewhere and play card on the table or to another player. 

## Deck
This class represents a deck of cards. It is represented by an array of Card objects similar to the Hand object. 
