package edu.mills.cs180a.wordnik_client_test;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import edu.mills.cs180a.wordnik.client.api.*;
import edu.mills.cs180a.wordnik.client.invoker.*;
import edu.mills.cs180a.wordnik.client.model.*;

public class Main {
    private static String getApiKey() throws IOException {
        return getResource("api-key.txt");
    }

    private static String getResource(String filename) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream(filename)) {
            if (is == null) {
                throw new IOException("Unable to open file " + filename);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }

    public static void main(String[] args) throws IOException {
        ApiClient client = new ApiClient("api_key", getApiKey());
        WordsApi wordsApi = client.buildClient(WordsApi.class);
        WordOfTheDay word = wordsApi.getWordOfTheDay("2022-03-15");
        System.out.println(word);

        // get random word
        String randomWord = wordsApi
                .getRandomWord("true", "noun", "given-name", 100, -1, 50, -1, 5, 10).getWord();

        // get etymology of random word
        WordApi wordApi = client.buildClient(WordApi.class);
        List<String> etymology = wordApi.getEtymologies(randomWord, "false");
        System.out.println("Etymology of '" + randomWord + "' is: " + etymology);

        // get synonyms of random word
        List<Related> related = wordApi.getRelatedWords(randomWord, "false", "synonym", 5);
        List<String> synonyms = related.get(0).getWords();
        System.out.println("Synonyms of '" + randomWord + "' are: " + synonyms);

        // get frequency of synonyms that are single words only (no phrases)
        System.out.println("Frequencies: ");
        for (String s : synonyms) {
            if (!s.contains(" ")) {
                System.out.println(s + ": " + wordApi.getWordFrequency(s, "false", 2020, 2021));
            }
        }
    }
}
