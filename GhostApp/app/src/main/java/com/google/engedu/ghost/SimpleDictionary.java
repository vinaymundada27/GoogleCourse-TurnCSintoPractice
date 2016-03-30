package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    String s;

    Random random;
    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix==null)
        {
            while(true) {
                random = new Random();
                int i = random.nextInt(words.size());

                if (words.get(i).length() >= 6)
                    return words.get(i);
            }

        }
        else{
            //binary search
            if(binary_search(prefix)){
                return s;
            }
            else
                return null;
        }
    }

    private boolean binary_search(String prefix) {

        int low=0,high=words.size();

        while(low<=high)
        {
            int mid = (low + high)/2;
            if(words.get(mid).contains(prefix))
            {
                s = words.get(mid);
                return true;
            }
            else if(words.get(mid).compareTo(prefix)<0)
            {
                low = mid + 1;
            }
            else
                high = mid - 1;
        }

        return false;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
