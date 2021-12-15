import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.math.*;
import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
//bug found line 28 beyond - fix it then reorganize code base to be more readable, do the javadocs, finally fix the ugly layout to look better

/**
 * A password generator that generates a random password and can copy to the clipboard.
 */
public class PasswordGenerator {

    /**
     * Generates a password given a certain set of parameters.
     * @param lowercase Determines whether the password may include lowercase letters or not.
     * @param uppercase Determines whether the password may include uppercase letters or not.
     * @param numbers Determines whether the password may include numbers or not.
     * @param symbols Determines whether the password may include symbols or not.
     * @param length The length of the password.
     * @return A password.
     */
    public static String generatePassword(boolean lowercase, boolean uppercase, boolean numbers, boolean symbols, int length){
        //33-47 symbols
        //48-57 numbers
        //65-90 uppercase
        //97-122 lowercase
        StringBuilder password = new StringBuilder();
        //procedure
        //randomly choose between lower, upper, number, or symbol

        ASCIIValues [] choices = new ASCIIValues[4];
        int nums = 0;
        boolean check = false;
        if (lowercase){
            choices[nums++] = new ASCIIValues(97,122);
            check = true;
        }
        if (uppercase){
            choices[nums++] = new ASCIIValues(65,90);
            check = true;
        }
        if (numbers){
            choices[nums++] = new ASCIIValues(48,57);
            check = true;
        }
        if (symbols){
            choices[nums++] = new ASCIIValues(33,47);
            check = true;
        }

        if(!check) {
            return "Please check a parameter.";
        }

        for(int i = 0; i < length; i++){
            ASCIIValues range = choices[((int)(Math.random()*(nums)))];
            String letter = Character.toString((int) (Math.random()*(range.getMax()-range.getMin()) + range.getMin()));
            password.append(letter);
        }
        return password.toString();
    }

    public static void writeToClipboard(String s) {
        StringSelection passwordCpy = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(passwordCpy,null);
    }

    //work in progress or maybe not at all.
    public void revealPassword(){

    }
    public static void main(String [] args){
        //init the frame
        JFrame mainFrame = new JFrame("Password Generator 5000");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //mainFrame.setSize(new Dimension(350, 350));
        //seems to just stack from left to right
        mainFrame.setLayout(new FlowLayout());

        //password options
        Checkbox lowercase = new Checkbox("lowercase", true);
        Checkbox uppercase = new Checkbox("UPPERCASE", true);
        Checkbox numbers = new Checkbox("0-9", false);
        Checkbox symbols = new Checkbox("?/!*", false);

        //length of password, uses a slider
        JSlider passLength = new JSlider(JSlider.HORIZONTAL, 1, 20, 9);
        passLength.setMajorTickSpacing(5);
        passLength.setMinorTickSpacing(1);
        passLength.setPaintTicks(true);
        passLength.setPaintLabels(true);

        //reveal or unreveal password
        

        //text field where password will be generated
        TextComponent passwordField = new TextField("Password generated here...");
        passwordField.setEditable(false);

        //button action
        Button generateButton = new Button("Generate");
        generateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                passwordField.setText(generatePassword(lowercase.getState(), uppercase.getState(), numbers.getState(), symbols.getState(), passLength.getValue()));
            }
        });

        //do this when creating a button
        Button copyButton = new Button("Copy");
        copyButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                PasswordGenerator.writeToClipboard(passwordField.getText());
            } 
        });

        //adding all of our components onto the main frame.
        mainFrame.add(lowercase);
        mainFrame.add(uppercase);
        mainFrame.add(numbers);
        mainFrame.add(symbols);
        mainFrame.add(passwordField);
        mainFrame.add(passLength);
        mainFrame.add(generateButton);
        mainFrame.add(copyButton);
        //keep it last since it turns on the frame
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
