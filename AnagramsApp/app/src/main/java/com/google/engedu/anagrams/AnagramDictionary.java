package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 3;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    int wordLength = DEFAULT_WORD_LENGTH;

    HashSet<String> wordSet;
    HashMap<String, ArrayList<String>> lettersToWord;
    ArrayList<String> wordList;
    ArrayList<String> temp;
    HashMap<Integer, ArrayList<String>> sizeToWords;
    ArrayList<String> list;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        wordSet = new HashSet<String>();
        wordList = new ArrayList<String>();
        lettersToWord = new HashMap<String, ArrayList<String>>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();

        while ((line = in.readLine()) != null) {
            //temp = new ArrayList<String>();// temporary arraylist
            //list = new ArrayList<String>();// temporary arraylist for refactoring

            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);

            if (sizeToWords.containsKey(word.length())) {
                list = sizeToWords.get(word.length());
                list.add(word);
                sizeToWords.put(word.length(), list);
            } else {
                list = new ArrayList<String>();
                list.add(word);
                sizeToWords.put(word.length(), list);
            }

            String key = sortedWords(word);

            if (lettersToWord.containsKey(key)) {
                temp = lettersToWord.get(key); //temp is a temporary arraylist
                temp.add(word);
                lettersToWord.put(key, temp);
            } else {
                temp = new ArrayList<String>();
                temp.add(word);
                lettersToWord.put(key, temp);
            }
        }

    }

    public String sortedWords(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;
    }

    public boolean isGoodWord(String word, String base) {

        if (wordSet.contains(word) && !word.contains(base)) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < 26; i++) {
            String c = Character.toString((char) (97 + i));
            String t = word;
            t = t + c;
            Log.d("words", t);
            String key = sortedWords(t);
            ArrayList<String> list;

            if (lettersToWord.containsKey(key)) {
                list = lettersToWord.get(key);

                for (String s : list) {
                    if (isGoodWord(s, word)) {
                        result.add(s);
                        Log.d("guesses", s);
                    }
                }
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> al;
        while (wordLength <= MAX_WORD_LENGTH) {
            al = sizeToWords.get(wordLength);
            int i = random.nextInt(al.size());
            String s = al.get(i);
            if (getAnagramsWithOneMoreLetter(s).size() >= MIN_NUM_ANAGRAMS) {
                if(wordLength<7)
                    wordLength++;
                return s;
            }
        }
        return "foo";
    }
}


