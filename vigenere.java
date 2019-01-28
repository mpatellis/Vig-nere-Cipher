/*
 Michael Patellis
 9/30/2018
 Created for Michael McAlpin's Computer Security (CIS 3360) class.
 All the provided input key and plaintext files are his work.
 Code is my own.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class vigenere
{
    private static final int BEGIN_ASCII_ALPHA = 97;
    private static final int CHARS_PER_LINE = 80;
    private static final int MAX_SIZE = 512;
    private static final int SIZE_ALPHA = 26;

    private static String cipher(String key, String pt)
    {
        int i, j, letter;
        String encrypted = "";

        int keyLen = key.length();
        int ptLen = pt.length();
        int[] shift = new int[keyLen];

        for (i = 0; i < keyLen; i++)
            shift[i] = key.charAt(i) - BEGIN_ASCII_ALPHA;

        for (i = 0, j = 0; i < ptLen; i++, j++)
        {
            if (j == keyLen)
                j = 0;

            letter = pt.charAt(i) - BEGIN_ASCII_ALPHA;
            letter = ((letter + shift[j])%SIZE_ALPHA) + BEGIN_ASCII_ALPHA;
            encrypted = encrypted + (char)letter;
        }
        return encrypted;
    }

    // output equals 0 if we are printing the key
    // output equals 1 if we are printing the plaintext
    // out equals 2 if we are printing the encrypted text
    private static void printOutput(String text, int output)
    {
        int i;
        int loopCon = text.length()/CHARS_PER_LINE;
        String print = "";

        if (output == 0)
            System.out.println("Vigenere Key:\n");
        else if (output == 1)
            System.out.println("Plaintext:\n");
        else if (output == 2)
            System.out.println("Ciphertext:\n");
        else
        {
            System.out.print("Something went wrong uh oh");
            return;
        }

        for (i = 1; i <= loopCon; i++)
        {
            print = text.substring(0, CHARS_PER_LINE);
            System.out.println(print);
            text = text.substring(CHARS_PER_LINE);
        }
        System.out.println(text + "\n\n");
    }

    // if the flag is 0 no x padding is needed
    // if the flag is 1 we do add the x padding
    private static String processString(String str, int pad)
    {
        int i;
        int n = str.length();

        for (i = 0; i < n; i++)
        {
            // if it is a letter
            if (Character.isLetter(str.charAt(i)))
            {
                // if it is an upper case letter
                if (!Character.isLowerCase(str.charAt(i)))
                    str = str.substring(0, i) + Character.toLowerCase(str.charAt(i)) + str.substring(i+1);
                // if it is a lower case letter we ignore it
            }
            // if it isn't a letter
            else
            {
                str = str.substring(0, i) + str.substring(i+1);
                i--;
                n--;
            }
        }

        if (str.length() > MAX_SIZE)
        {
            str = str.substring(0, MAX_SIZE);
            return str;
        }

        if (pad == 0)
            return str;
        else
        {
            n = MAX_SIZE - str.length();

            for (i = 0; i < n; i++)
                str = str + "x";

            return str;
        }
    }

    private static String readFile(String file, int pad)
    {
        String error = "Error";
        try
        {
            String retVal = "";
            String line = "";
            BufferedReader in = new BufferedReader(new FileReader(file));

            while ((line = in.readLine()) != null)
                retVal = retVal + line;
            in.close();

            retVal = processString(retVal, pad);

            return retVal;
        } catch (FileNotFoundException e1)
        {
            System.out.println("File not found exception");
            return error;
        } catch (IOException e2)
        {
            System.out.println("IOException");
            return error;
        }
    }

    public static void main(String[] args)
    {
        String kFile, pFile, key, plaintext, encrypted;

        kFile = args[0];
        pFile = args[1];

        key = readFile(kFile, 0);
        printOutput(key, 0);

        plaintext = readFile(pFile, 1);
        printOutput(plaintext, 1);

        encrypted = cipher(key, plaintext);
        printOutput(encrypted, 2);
    }
}
