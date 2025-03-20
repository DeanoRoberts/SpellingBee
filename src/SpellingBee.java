// Spelling Bee By Deano Roberts

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 * <p>
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 * <p>
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 * <p>
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 * <p>
 * Written on March 5, 2023 for CS2 @ Menlo School
 * <p>
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    // Inputted letters from user
    private String letters;
    // stores generated words
    private ArrayList<String> words;
    // Our Dictionary Constants
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        wordMaker("", letters);
    }

    // Recursive method to generate the words from the letters
    public void wordMaker(String word, String letters) {
        // Adds our current word
        words.add(word);
        // Base case
        if (letters.isEmpty()) {
            return;
        }
        // Generates all words by adding each character
        for (int i = 0; i < letters.length(); i++) {
            String newWord = word + letters.charAt(i);
            String newLetters = letters.substring(0, i) + letters.substring(i + 1);
            wordMaker(newWord, newLetters);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Recursive call
        words = mergeSort(words);
        // YOUR CODE HERE

    }

    private ArrayList<String> mergeSort(ArrayList<String> wordList) {
        // Base case
        if (wordList.size() <= 1) {
            return wordList;
        }

        // Splits the list into two halves
        int mid = wordList.size() / 2;
        ArrayList<String> leftSide = new ArrayList<>();
        ArrayList<String> rightSide = new ArrayList<>();

        for (int i = 0; i < mid; i++) {
            leftSide.add(wordList.get(i));
        }

        for (int i = mid; i < wordList.size(); i++) {
            rightSide.add(wordList.get(i));
        }
        // Merge sorts both halfs then merges the left and right side
        leftSide = mergeSort(leftSide);
        rightSide = mergeSort(rightSide);
        return merge(leftSide, rightSide);
    }

    private ArrayList<String> merge(ArrayList<String> leftSide, ArrayList<String> rightSide) {
        ArrayList<String> sol = new ArrayList<>();
        int index1 = 0;
        int index2 = 0;
        int count = 0;
        // Merges all elements form the lists
        while (index1 < leftSide.size() && index2 < rightSide.size()) {
            if (leftSide.get(index1).compareTo(rightSide.get(index2)) < 0) {
                sol.add(leftSide.get(index1++));
            } else {
                sol.add(rightSide.get(index2++));
            }
        }

        while (index1 < leftSide.size()) {
            sol.add(leftSide.get(index1++));
        }

        while (index2 < rightSide.size()) {
            sol.add(rightSide.get(index2++));
        }

        return sol;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        ArrayList<String> goodWords = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            if (checkWordsBinarySearch(DICTIONARY, words.get(i))) {
                goodWords.add(words.get(i));
            }
        }
        words = goodWords;
    }

    private boolean checkWordsBinarySearch(String[] dictionary, String theWord) {
        int left = 0;
        int right = dictionary.length - 1;

        while (left <= right) {
            int middle = (left + right) / 2;
            int compareValue = dictionary[middle].compareTo(theWord);

            if (compareValue == 0) {
                return true;
            } else if (compareValue < 0) {
                left = middle + 1;

            } else {
                right = middle - 1;
            }


        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while (s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
