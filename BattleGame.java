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
 *  Lets a player play a two-player card game (a modified version of Battle).
 *  The player takes up to 5 cards to form a hand of cards. 
 *  The player can put the leftmost card from their hand onto the table, pick up more cards from the deck of cards
 *  to fill the gaps in their "hand", replace a card in their hand with a card from the deck, and reorder the cards
 *  in their hand.
 *  For each battle, the player puts their leftmost card from their hand on the table.
 *  The other player is controlled by the computer, who will simply take the top card from the deck.
 *  The player with the highest card wins the battle. If both cards have the same value, neither player wins. 
 *  Each winning battle gives the player a point. The first player to win 7 battles is the winner. 
 *
 * See the Assignment page for description of the program design.
 */

public class BattleGame{

    // Constants for the game
    public static final int NUM_HAND = 5;      // Number of cards in hand
    public static final int TARGET_SCORE = 7;  // Number of rounds to win the game
    public static final int NUM_REPLACE = 3;   // Number of cards the player is allowed to replace per game

    // Fields for the game: deck, hand, and table
    private ArrayList<Card> deck = Card.getShuffledDeck();          // the deck (a list of Cards)
    private Card[] hand = new Card[NUM_HAND];                       // the hand (fixed size array of Cards)
    private ArrayList<Card> tableComputer = new ArrayList<Card>();  // the list of Cards that the computer has played
    private ArrayList<Card> tablePlayer = new ArrayList<Card>();    // the list of Cards that the player has played

    private int selectedPos = 0;      // selected position in the hand.
    private int compScore = 0;        // the number of points scored by the computer player
    private int playScore = 0;        // the number of points scored by the user player
    private int remainingReplaces = NUM_REPLACE;  // 

    // Constants for the layout
    public static final Color RACK_COLOR = new Color(122,61,0);

    public static final int HAND_LEFT = 60;      // x-position of the leftmost Card in the hand
    public static final int HAND_TOP = 500;      // y-Position of all the Cards in the hand 
    public static final int CARD_SPACING = 80;   // spacing is the distance from left side of a card
    // to left side of the next card in the hand
    public static final int CARD_OVERLAP = 15;   // overlap is the distance from left side of a card
    // to left side of the next card on the table
    public static final int CARD_HEIGHT = 110; 

    public static final int TABLE_LEFT = 10;                
    public static final int TABLE_TOP_COMPUTER = 80;
    public static final int TABLE_TOP_PLAYER   = TABLE_TOP_COMPUTER+CARD_HEIGHT+10;

    public static final int SCORES_TOP = 20;

    /**
     * CORE
     * 
     * Restarts the game:
     *  get a new shuffled deck,
     *  set the compScore, playScore and remainingReplaces to their initial values
     *  set the table to be empty,
     *  refill the hand from the deck
     */
    public void restart(){
        /*# YOUR CODE HERE */
        
        compScore = 0;
        playScore = 0;
        remainingReplaces = NUM_REPLACE;
        ArrayList<Card> deck = Card.getShuffledDeck();
        tableComputer.clear();
        tablePlayer.clear();
        
        // Reset all the counters
        
        for (int i = 0; i < hand.length; i++){
            
            hand[i] = deck.remove(0);
            
            // get the top cards of the deck and put them into our hand.
            
        }
        
        UI.println(deck.size()); // This is just to check that the cards are actually being removed from the deckm into our hand. 

        this.redraw();
        

        
    }

    /**
     * CORE
     * 
     * If the deck is not empty and there is at least one empty position on the hand, then
     * pick up the top card from the deck and put it into the first empty position on the hand.
     * (needs to search along the array for an empty position.)
     */
    public void pickupCard(){
        /*# YOUR CODE HERE */
        
        for (int i = 0; i < hand.length; i++){
            
            if (this.hand[i]==null){
                
                hand[i] = deck.remove(0);
                i += (hand.length - i); // without this code here, whenever there are multiple spaces it will fill it all up immeditately
                
                // The code makes it so that each time the pickup card is pressed only one card is picked up at a time. 
                
            }
            
        }
        

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
        
        for (int i = 0; i < hand.length; i++){
            
            if (this.hand[i]!=null){
                
                hand[i].draw(HAND_LEFT + (CARD_SPACING * i), HAND_TOP);
                
                // for every card in hand length, draw the card. unless it is null where it just leaves a space. 
                
            }
            
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
        
        for (int i = 0; i < tablePlayer.size(); i++){
            
            tablePlayer.get(i).draw(TABLE_LEFT + (CARD_OVERLAP * i), TABLE_TOP_PLAYER);
            tableComputer.get(i).draw(TABLE_LEFT + (CARD_OVERLAP * i), TABLE_TOP_COMPUTER);
            
            // This is code draws all the cards in the table ArrayLIst, it assumes that the tablePlayer.size() is the same as tableComputer.size()
            
        }

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
        
        if (hand[0] != null){
            
            tablePlayer.add(hand[0]);
            int player = hand[0].getRank();
            
            hand[0] = null; // get rid of the card in the first position
            
            Card opponent = deck.remove(0);
            tableComputer.add(opponent);
            int computer = opponent.getRank();
            
            // get the rank of the card on the left and compare it to the rank of the computer.
            
            if (player > computer){
                playScore += 1;
            }
            else if (player < computer){
                compScore += 1;
            }
            else {
                ;// if it a tie then do nothing, 
            }
        }
        else {
            ; // if the very left card is null then don't do anything
        }
        
        if (playScore == TARGET_SCORE || compScore == TARGET_SCORE){
            
            this.redraw();
            this.endGame();
            
        }
        
        this.redraw();

    }

    /**
     * COMPLETION
     * 
     * If there is a card at the selected position in the hand, 
     * replace it by a card from the deck.
     */
    public void replaceCard(){
        if (remainingReplaces > 0 ){
            /*# YOUR CODE HERE */
            
            hand[selectedPos] = deck.remove(0);
            // get the selected posiiton in the hand and set it as the first card on the deck, removing it at the same time. 
            remainingReplaces --; // reduce the remaining replaces down one. 
            
            
        }
        this.redraw();
    }

    /**
     * COMPLETION
     *
     * Swap the contents of the selected position on hand with the
     * position on its left (if there is such a position)
     * and also decrement the selected position to follow the card 
     */
    public void moveLeft(){
        /*# YOUR CODE HERE */
        
        if (selectedPos > 0){
            
            Card previous = hand[selectedPos - 1];
            Card current = hand[selectedPos];
            
            // I think it is easier to set the previous and current as their own Card object
            
            hand[selectedPos - 1] = current;
            hand[selectedPos] = previous;
            
            // then we just do a switch of the two in the left direction
            
            selectedPos --; // the card we are selected has now moved one to the left so we follow it. 
            
        }
        else{
            ;
        }

        this.redraw();
    }
    
    /**
     * COMPLETION
     *
     * Swap the contents of the selected position on hand with the
     * position on its right (if there is such a position)
     * and also decrement the selected position to follow the card 
     */
    public void moveRight(){
        /*# YOUR CODE HERE */
        
        if (selectedPos < hand.length - 1){
            
            Card next = hand[selectedPos + 1];
            Card current = hand[selectedPos];
            
            // This method is just a repeat of left but adds the versatility if the user would like to move their selected card to the right. 
            
            hand[selectedPos + 1] = current;
            hand[selectedPos] = next;
            
            // rather the previous and current we are dealing with the current and the next card on the hand. 
            
            selectedPos ++; // we also add one to the selected position to follow our selected card.
            
        }
        else{
            ; // this is neccessary so that it stops if the card is at the end. 
        }

        this.redraw();
    }

    /** ---------- The code below is already written for you ---------- **/

    /** 
     * Allows the user to select a position in the hand using the mouse.
     * If the mouse is released over the hand, then sets  selectedPos
     * to be the index into the hand array.
     * Redraws the hand and table 
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            if (y >= HAND_TOP && y <= HAND_TOP+CARD_HEIGHT && 
            x >= HAND_LEFT && x <= HAND_LEFT + NUM_HAND*CARD_SPACING) {
                this.selectedPos = (int) ((x-HAND_LEFT)/CARD_SPACING);
                //UI.clearText();UI.println("selected "+this.selectedPos);
                this.redraw();
            }
        }
    }

    /**
     * Displays a win/lose message
     */
    public void endGame(){
        UI.setFontSize(40);
        UI.setColor(Color.red);
        if (this.playScore > this.compScore){
            UI.drawString("YOU WIN!!!", 500, HAND_TOP-80);
        }
        else{
            UI.drawString("YOU LOSE", 500, HAND_TOP-80);
        }
        UI.sleep(3000);
        this.restart();
    }

    /**
     *  Redraw the table and the hand.
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(20);
        UI.setColor(Color.black);
        UI.drawString("Player: " + playScore + " Computer: " + compScore, TABLE_LEFT+150, SCORES_TOP);
        UI.drawString("Remaining replaces: " + remainingReplaces, TABLE_LEFT+600, SCORES_TOP);

        // outline the hand and the selected position
        UI.setLineWidth(2);
        UI.setColor(Color.black);
        UI.drawRect(HAND_LEFT-4, HAND_TOP-4, (CARD_SPACING)*NUM_HAND+4, CARD_HEIGHT+8);

        UI.setColor(Color.green);
        int selLeft = HAND_LEFT + (this.selectedPos * (CARD_SPACING)) - 2;
        UI.drawRect(selLeft, HAND_TOP - 2, CARD_SPACING, CARD_HEIGHT+4);

        // draw the rack top
        UI.setColor(RACK_COLOR);
        UI.fillRect(HAND_LEFT-10, HAND_TOP-28, (CARD_SPACING)*NUM_HAND+20, 20);

        this.drawHandCards();
        this.drawTableCards();
    }

    /**
     * Set up the user interface
     */
    public void setupGUI(){
        UI.setMouseListener( this::doMouse );
        UI.addButton("Battle", this::playBattle);
        UI.addButton("Pickup", this::pickupCard);
        UI.addButton("Replace",  this::replaceCard); 
        UI.addButton("Left", this::moveLeft);
        UI.addButton("Right", this::moveRight);
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
