/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Sureshbabu
 */
public class JTextFieldLimit extends PlainDocument {
          private int limit;
          private boolean toUppercase = false;
           
          JTextFieldLimit(int limit) {
           super();
           this.limit = limit;
           }
          
          public void insertString
            (int offset, String  str, AttributeSet attr)
              throws BadLocationException {
           if (str == null) return;
 
           if ((getLength() + str.length()) <= limit) {
             if (toUppercase) str = str.toUpperCase();
             super.insertString(offset, str, attr);
             }
           }
        }
