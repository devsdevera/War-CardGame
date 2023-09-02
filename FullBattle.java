// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP102/112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP-102-112 - 2022T1, Assignment 9
 * Name:   Emmanuel De Vera
 * Username:  Deveremma
 * ID:  300602434
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;


/**
 *  This is the class for Full Battle also known as War (Card Game).
 *  A majority of the code is replicated from the BattleGame code but only the relevant Fields remain on this Class. 
 */

public class FullBattle{

    // Constants for the game
    public static final int NUM_HAND = 26;      // Number of cards in hand

    // Fields for the game: deck, hand, and table
    private ArrayList<Card> deck = Card.getShuffledDeck();          // the deck (a list of Cards)
    
    private Card[] hand = new Card[NUM_HAND];                       // the hand (fixed size array of Cards)

    private ArrayList<Card> tableComputer = new ArrayList<Card>();  // the list of Cards that the computer has played
    private ArrayList<Card> tablePlayer = new ArrayList<Card>();    // the list of Cards that the player has played
    
    private ArrayList<Card> ComputerStack = new ArrayList<Card>();  // the list of Cards that the computer has played
    private ArrayList<Card> PlayerStack = new ArrayList<Card>(); 
    
    private ArrayList<Card> WarStack = new ArrayList<Card>();  // the list of Cards that the computer has played
    
    public static int battle_count;
    
    public static int war_count;
    
    public static final int HAND_LEFT = 60;      // x-position of the leftmost Card in the hand
    
    public static final int HAND_TOP = 500;      // y-Position of all the Cards in the hand 
    // to left side of the next card in the hand
    public static final int CARD_OVERLAP = 15;   // overlap is the distance from left side of a card
    // to left side of the next card on the table
    public static final int CARD_HEIGHT = 110; 

    public static final int TABLE_LEFT = 60;                

    public static final int TABLE_TOP_COMPUTER = 200;
    
    public static final int TABLE_TOP_PLAYER   = TABLE_TOP_COMPUTER+CARD_HEIGHT+10;

    /**
     * CORE
     * 
     * Restarts the game:
     *  get a new shuffled deck,
     *  set the table to be empty,
     *  refill the hand from the deck
     */
    public void restart(){
        /*# YOUR CODE HERE */
        
        ArrayList<Card> deck = Card.getShuffledDeck();
        
        tableComputer.clear();
        tablePlayer.clear();
        
        // Clear the table Arraylists
        
        PlayerStack.clear();
        ComputerStack.clear();
        
        battle_count = 0;
        war_count = 0;
        
        // Clear the Stacks for the player and Computer
        
        
        for (int i = 0; i < 26; i++){
            
            PlayerStack.add(deck.remove(0));
            
        }
        
        // Convert the new shuffled deck into each Player and Computer Stack evenly
        
        for (int i = 0; i < 26; i++){
            
            ComputerStack.add(deck.remove(0));
            
        }
        
        // deck just becomes the opponents cards
        
        UI.println(PlayerStack);
        UI.println(ComputerStack);
        
        this.redraw();
        
        
    }

    /**
     * CORE
     * 
     * Draws all the Cards in the hand,
     *  This MUST use the constants:  (in order to make the selection work!)
     *   - CARD_SPACING, HAND_LEFT, HAND_TOP
     *   See the descriptions where these fields are defined.
     */
    public void drawHandCards(){
        //draw the cards.
        /*# YOUR CODE HERE */
        
        // War required both stacks of the opponenet and the player to be face down, therefore its simply cards back * stack.size()
        
        for (int i = 0; i < PlayerStack.size(); i++){
            
            UI.drawImage("cards/back.png", HAND_LEFT + (CARD_OVERLAP * i), HAND_TOP);
            
        }
        
        for (int i = 0; i < ComputerStack.size(); i++){
            
            UI.drawImage("cards/back.png", HAND_LEFT + (CARD_OVERLAP * i), HAND_TOP - 450);
            
        }

    }

    /**
     * CORE
     * Draws all the Cards in both the computer and player tables in two rows.
     *   See the descriptions of TABLE_LEFT, TABLE_TOP_COMPUTER, TABLE_TOP_PLAYER and CARD_OVERLAP.
     *
     * COMPLETION:
     * - The card with the highest rank in the last battle is outlined
     * 
     */
    public void drawTableCards(){
        //draw the cards in the tableComputer and tablePlayer lists.
        /*# YOUR CODE HERE */
        
        // The table card is basically just the battle / war history that displays the Table Arraylist
        
        for (int i = 0; i < tablePlayer.size(); i++){
            
            if (tablePlayer.get(i) != null){
        
                tablePlayer.get(i).draw(TABLE_LEFT + (CARD_OVERLAP * i), TABLE_TOP_PLAYER);
                tableComputer.get(i).draw(TABLE_LEFT + (CARD_OVERLAP * i), TABLE_TOP_COMPUTER);
            }
            else{
                UI.drawImage("cards/back.png", TABLE_LEFT + (CARD_OVERLAP * i), TABLE_TOP_PLAYER);
                UI.drawImage("cards/back.png", TABLE_LEFT + (CARD_OVERLAP * i), TABLE_TOP_COMPUTER);
            }
            
        }
        
        // if it noticed a null value, this must be from a war in which just the back of the cards are shown. 

    }
    
    public void clear(){
        tablePlayer.clear();
        tableComputer.clear();
        
        this.redraw();
    }

    /**
     * CORE
     * 
     * If there is a card in the leftmost position in the hand, then
     * - place it on the table
     * - gets the top card from the deck for the computer player and places it to the table
     * - compare the ranks of the two cards and award a point to the player with the highest card.
     * - redraw the table and hand [this.redraw()]
     * - if the player or the computer have reached the target,  end the game.
     */
    public void playBattle(){
        /*# YOUR CODE HERE */
        
        battle_count += 1;
        
        if (ComputerStack.size() >= 1 && PlayerStack.size() >= 1){
            Card opponent = ComputerStack.remove(0);
            tableComputer.add(opponent);
            int computer = opponent.getRank();
            
            // I am making used of the .remove() method which returns the value and removes it from the ArrayList. 
            
            Card me = PlayerStack.remove(0);
            tablePlayer.add(me);
            int player = me.getRank();
            
            // The code below makes it so that Ace is overidden as the highest card.
            
            // All the royal cards are just set equally to 11, this boosts the rate of wars (ties)
            
            if (computer == 13 || computer == 12){computer = 11;}
            
            if (player == 13 || player == 12){player = 11;}
            
            if (computer == 1){computer = 14;}
            
            if (player == 1){player = 14;}
            
            // Whoever wins gets both of the cards played get put into their stack
            
            if (player > computer){
                PlayerStack.add(opponent);
                PlayerStack.add(me);
                }
            else if (player < computer){
                ComputerStack.add(me);
                ComputerStack.add(opponent);
                }
            else {
                WarStack.add(opponent);
                WarStack.add(me);
                this.playWar();
            }
            
            // but if it is a draw the cards are put into the warstack, who ever wins war will get all the cards in the warstack. 
            
            this.redraw();
            
        }
        else{
            
            // this means that we cannot place down any cards meaning either the computer or the player has won. 
            
            if (PlayerStack.size() > ComputerStack.size()){
                UI.drawString("YOU WON ALL 52 CARDS!!!", 700, HAND_TOP-40);
                
            }
            else {
                UI.drawString("YOU LOST ALL 52 CARDS", 700, HAND_TOP-40);
                
            }
            
        }
        
        

        

    }
    
    public void playWar(){
        /*# YOUR CODE HERE */
        
        war_count += 1;
        
        // War is very similar to battle but instead we draw 3 cards face down and the fourth will be a regular battle
        
        if (ComputerStack.size() >= 5 && PlayerStack.size() >= 5){
            
            Card computer_dummy_1 = ComputerStack.remove(0);
            Card computer_dummy_2 = ComputerStack.remove(0);
            Card computer_dummy_3 = ComputerStack.remove(0);
            
            Card player_dummy_1 = PlayerStack.remove(0);
            Card player_dummy_2 = PlayerStack.remove(0);
            Card player_dummy_3 = PlayerStack.remove(0);
            
            // I have called all the face down cards dummy cards
            
            tableComputer.add(null);
            tableComputer.add(null);
            tableComputer.add(null);
            
            tablePlayer.add(null);
            tablePlayer.add(null);
            tablePlayer.add(null);
            
            // They are added to the table as null, which the code above reads as just display the back of the card. 
            
            Card opponent = ComputerStack.remove(0);
            tableComputer.add(opponent);
            int computer = opponent.getRank();
            
            Card me = PlayerStack.remove(0);
            tablePlayer.add(me);
            int player = me.getRank();
            
            // The code above is basically just a repeat now of the battle 
            
            WarStack.add(computer_dummy_1);
            WarStack.add(computer_dummy_2);
            WarStack.add(computer_dummy_3);
            WarStack.add(player_dummy_1);
            WarStack.add(player_dummy_2);
            WarStack.add(player_dummy_3);
            WarStack.add(opponent);
            WarStack.add(me);
            
            //The code above makes it so that all the dummy cards and the cards we will 'battle' are added to the Warstack. \
            
            // Whoever wins will get 10 cards to their stack, potentially more if another successive war take place.
            
            // Below is again just code to set Aces to be highest and royals to be of equal value. 
            
            if (computer == 13 || computer == 12){computer = 11;}
            
            if (player == 13 || player == 12){player = 11;}
            
            if (computer == 1){computer = 14;}
            
            if (player == 1){player = 14;}
            
            
            
            
            if (player > computer){
                
                //UI.println(WarStack.size());
                
                int num = WarStack.size();
                
                // All of Warstack's cards get funnelled into Player's stack if they win the war
                
                for (int i = 0; i < num; i ++){
                    
                    PlayerStack.add(WarStack.remove(0));
                    
                }
                
                this.redraw();
            }
            else if (player < computer){
                
                //UI.println(WarStack.size());
                
                int num = WarStack.size();
                
                // All of Warstack's cards get funnelled into the Computer's stack if they win the war. 
                
                for (int i = 0; i < num; i ++){
                    
                    ComputerStack.add(WarStack.remove(0));
                    
                }
                
                this.redraw();
            }
            else {
                
                // If it is yet another tie, we repeat the function again and go to war. 
            
                this.playWar();

            }
            
        }
        else {
            
            // if there is a war but either the computer or the player can no longer distribute three dummy cards and a fourth battle card, then it is game over. 
            
            if (PlayerStack.size() > ComputerStack.size()){
                UI.drawString("YOU WIN!!!", 500, HAND_TOP-40);
            }
            else {
                UI.drawString("YOU LOSE", 500, HAND_TOP-40);
            }
        }
        
        
        

    }


    /**
     *  Redraw the table and the hand.
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(20);
        UI.setColor(Color.black);
        UI.drawString("Battles: " + battle_count + "      Wars: " + war_count, TABLE_LEFT+350, 20);
        UI.setColor(Color.black);
        UI.drawString("Computer", TABLE_LEFT, 40);
        UI.drawString("Battle/War History", TABLE_LEFT, 190);
        UI.drawString("You", TABLE_LEFT, 490);
        UI.drawString("(Cards Automatically Allocated To Winner's Stack)", TABLE_LEFT+220, 630);

        // outline the hand and the selected position
        //UI.setLineWidth(2);
        //UI.setColor(Color.black);
        //UI.drawRect(HAND_LEFT-4, HAND_TOP-4, (CARD_OVERLAP)*NUM_HAND+4, CARD_HEIGHT+8);

        //UI.setColor(Color.green);
        //int selLeft = HAND_LEFT + (this.selectedPos * (CARD_SPACING)) - 2;
        //UI.drawRect(selLeft, HAND_TOP - 2, CARD_SPACING, CARD_HEIGHT+4);

        // draw the rack top
        //UI.setColor(RACK_COLOR);
        //UI.fillRect(HAND_LEFT-10, HAND_TOP-28, (CARD_OVERLAP)*NUM_HAND+20, 20);

        this.drawHandCards();
        this.drawTableCards();
    }

    /**
     * Set up the user interface
     */
    public void setupGUI(){
        UI.addButton("Battle", this::playBattle);
        UI.addButton("Clear History", this::clear);
        UI.addButton("Restart", this::restart);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100,650);
        UI.setDivider(0.0);

    }

    public static void main(String[] args){
        BattleGame bg = new BattleGame();
        bg.setupGUI();
        bg.restart();
    }   
}
