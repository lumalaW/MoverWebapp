package Application;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by william
 * A class to encrypt the passwords stored in the database.
 */
public class Encryption {

    // Lists used to create the ciphers.
    private static Character [] numbers = new Character [] {'0','1','2','3','4','5','6','7','8','9'};
    private static ArrayList<Character> numbersList = new ArrayList<>(Arrays.asList(numbers));

    private static Character [] letters = new Character [] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static ArrayList<Character> lettersList = new ArrayList<>(Arrays.asList(letters));

    private static Character [] cipherLetters = new Character[] {'s','h','q','g','i','z','m','e','y','a','l','n','f','o','d','x','j','r','k','c','v','p','t','u','w','b'};
    private static Character [] cipherNumbers = new Character[] {'8','5','7','9','6','2','0','4','3','1'};


    // Method to create/get the final password that is stored in the DB
    // Salting process  is done i.e. password and username are joined and then a cipher is used in the new string to get final password.
    public static String getFinalPassword(String userName, String userPassword){
        String finalPassword;
        String cat = userPassword + userName;
        cat = cat.toLowerCase();
        ArrayList <Character> newPassword = new ArrayList<>();

        for(int i = 0; i < cat.length(); i++){
            char passwordCharacter = cat.charAt(i);
            char newCharacter;
            if(numbersList.contains(passwordCharacter)){ // this is a number
                // convert the number to a new number using the cipher list
                int position = numbersList.indexOf(passwordCharacter);
                newCharacter = cipherNumbers[position];
                newPassword.add(newCharacter);
            }else if(lettersList.contains(passwordCharacter)){ // this is a letter
                //convert the letter to a new number using the cipher list
                int position = lettersList.indexOf(passwordCharacter);
                newCharacter = cipherLetters[position];
                newPassword.add(newCharacter);
            }else{ // if it's a # or @
                //keep the character
                newCharacter = passwordCharacter;
                newPassword.add(newCharacter);
            }
        }

        // convert the arrayList to a string
        StringBuilder sb = new StringBuilder();
        for(Character element : newPassword)
        {
            sb.append(element);
        }

        finalPassword = sb.toString();
        return finalPassword;
    }
}
