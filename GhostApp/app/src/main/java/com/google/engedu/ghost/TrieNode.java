package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Character c;

    //default constructor
    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    //paramterized constructor
    /*public TrieNode(Character c)
    {
        children = new HashMap<>();
        isWord = false;
        this.c=c;
    }*/

    public void add(String word) {
        HashMap<Character,TrieNode>children = this.children;

        for(int i=0;i<word.length();i++)
        {
            Character c = word.charAt(i);

            TrieNode t;
            if(children.containsKey(c))
            {
                t = children.get(c);
            }
            else
            {
                t = new TrieNode();
                children.put(c,t);
                //Log.d("add", String.valueOf(c));
            }

            children = t.children;

            //leaf node
            if(i==word.length()-1)
            {
                t.isWord = true;
            }
        }
    }

    public boolean isWord(String s) {
        TrieNode t = searchNode(s);

        if(t!=null && t.isWord==true)
            return true;

        return false;
    }

    public TrieNode searchNode(String s) {
        HashMap<Character,TrieNode>child = this.children;

        TrieNode t=null;
        if(s=="")
        {
            Log.d("null","nulllaa");
        }
        if(s!=null) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (child.containsKey(c)) {
                    t = child.get(c);
                    child = t.children;
                } else {
                    return null;
                }
            }
        }
        return t;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode t = searchNode(s);

        HashMap<Character,TrieNode>temp;

        String globalString = s;
        while(true)
        {
            if(t==null)
                return null;

            if(t.isWord)
            {
                return globalString;
            }

            while(true)
            {
                Random random = new Random();
                int i = random.nextInt(26);

                temp = t.children;

                String alphabets = "abcdefghijklmnopqrstuvwxyz";
                char alpha[] = alphabets.toCharArray();
                char c = alpha[i];
                if(temp.containsKey(c)){
                    t=temp.get(c);
                    globalString += c;
                    break;
                }
            }
        }
    }

    public String getGoodWordStartingWith(String s) {

        /*if(s==null)
        {
            while(true) {
                Random random = new Random();
                int i = random.nextInt(words.size());

                if (words.get(i).length() >= 6)
                    return words.get(i);
            }

        }*/
        TrieNode t = searchNode(s);

        HashMap<Character,TrieNode>temp;

        String globalString = s;
        while(true)
        {
            if(t==null)
                return null;

            if(s.length()>=4)
            {

            }
            if(t.isWord)
            {
                return globalString;
            }

            while(true)
            {
                Random random = new Random();
                int i = random.nextInt(26);

                temp = t.children;

                String alphabets = "abcdefghijklmnopqrstuvwxyz";
                char alpha[] = alphabets.toCharArray();
                char c = alpha[i];
                if(temp.containsKey(c)){
                    t=temp.get(c);
                    String tempString  = globalString;
                    globalString = globalString + c;

                    Log.d("global",globalString);
                    if(isWord(globalString))
                    {
                       globalString = tempString;
                        continue;
                    }
                    else
                        return globalString;
                }
            }
        }
    }
}
