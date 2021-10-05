/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.model.javacard;

import java.util.List;
import javax.smartcardio.*;
import javax.smartcardio.CardTerminals.State;

/**
 *
 * @author Sureshbabu
 */
public class Smartcard {
         
    private Card card=null;
    private CardChannel channel=null;
    
    /**
     * Reads all the connected PCSC version of the SmartCard Reader.
     * @return 
     *    List Object containing list of CardTerminals.
     */
    public List<CardTerminal> getSmartcardReaders()
    {
        List<CardTerminal> terminals = null;
        try
        {
        // Display the list of terminals
        TerminalFactory factory = TerminalFactory.getDefault();
        CardTerminals term = factory.terminals();        
        if(term.list().isEmpty() == true)
            System.out.println("No Smart card reader present");
        terminals = term.list();
        }
        catch(CardException e)
        {
            System.out.println("Exception: " + e.toString());
            System.out.println("No PC/SC Card Readers connected?");
        }
        
        return terminals;
    }
    
    /**
     * Establishes a connection to the card. 
     * @param terminal
     *        PCSC Terminal Object.
     * @return
     *        this method returns the  Card object   
     * @throws CardException 
     */
    public Card connect(CardTerminal terminal) throws CardException
    {    
        try
        {
            // Connect wit hthe card
            card = terminal.connect("*");
            System.out.println("card: " + card);
            channel = card.getBasicChannel();   
        }
        catch(CardException e)
        {
            card=null;
            System.out.println("Exception: " + e.toString());
            if(e.getMessage().contains("No card present"))
            throw e;
        }        
        return card;
    }
    
    /**
     * Transmits the specified command APDU to the Smart Card and returns the response APDU.
     * @param apduCmd
     *        CommandAPDU Object.
     * @return
     *        ResponseAPDU Object.    
     * @throws CardException 
     */
    public ResponseAPDU transmitAPDU(CommandAPDU apduCmd) throws CardException
    {
        ResponseAPDU answer=null;
        try
        {
        answer = channel.transmit(apduCmd);
        System.out.println("answer: " + answer.toString());  
        }
        catch(CardException e)
        {
           System.out.println("Exception: " + e.toString());            
        }
        return answer;
    }
    
    /**
     * Transmits the command APDU stored in the command ByteBuffer and
     * returns the Response APDU in the response ByteBuffer.
     * @param apduCmd
     *        Command APDU stored in the Byte Buffer.
     * @return 
     *       Response APDU in the response ByteBuffer.
     */
    public byte[] transmitAPDU(byte[] apduCmd)
    {
        byte[] answerBytes=null;
        CommandAPDU cmd;
       
        try
        {
            cmd = new CommandAPDU(apduCmd);
            ResponseAPDU  answer = channel.transmit(cmd);
            System.out.println("answer: " + answer.toString());  
            answerBytes = answer.getBytes();
            Thread.sleep(1);
        }
        catch(CardException e)
        {
            System.out.println("Exception: " + e.toString());
        }
        catch(IllegalArgumentException e)        
        {
           System.out.println("Exception: " + e.toString());           
        }
        catch(Exception ex){
            
        }
        return answerBytes;
    }
    
    /**
     * Returns the ATR of this card.
     * @return 
     */
    public byte[] getATR()
    {
        ATR atr = card.getATR();
        byte[] atrBytes = atr.getBytes();
        return atrBytes;
    }
    
    /**
     * Resets the Smart Card connection with the Smart Card Reader.
     * @param terminal
     *        PCSC Reader Object.
     * @throws CardException 
     *        Throws Card Exception if communication error occurs.
     */
    public void resetTerminal(CardTerminal terminal) throws CardException
    {    
        try
        {
            card.disconnect(true);
            System.out.println("Card disconnected. And reconnecting...");
            // Connect wit hthe card
            card = terminal.connect("*");
            System.out.println("card: " + card);
            channel = card.getBasicChannel();   
        }
        catch(CardException e)
        {
            System.out.println("Exception: " + e.toString());
            if(e.getMessage().contains("Card not present"))
            throw e;
        }        
    }
    
    /**
     * Disconnects Smart card PC/SC communication interface from the Smart card Reader.
     */
    public void disconnect()
    {
        try
        {
            card.disconnect(true);
        }
        catch(CardException e)
        {
           System.out.println("Exception: " + e.toString()); 
        }        
        card = null;
        channel=null;
    }
}
