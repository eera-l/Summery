import java.util.*;

/**
 * Created by Federica on 26/04/2018.
 */
public class Summarizer {

    private String beginningText;
    private ArrayList<Sentence> sentences = new ArrayList<>();
    public static LinkedHashMap<String, Integer> frequencyMap;
    public static String[] keywords;
    public ArrayList<String> doubleKeywords;
    public ArrayList<Integer> doubleKeywordsFrequency;

    public Summarizer(String text) {

        beginningText = text;
        frequencyMap = new LinkedHashMap<>();
        keywords = new String[calculateNumOfKeywords()];
        doubleKeywords = new ArrayList<>();
        doubleKeywordsFrequency = new ArrayList<>();
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Divides text into sentences.
     * Sentences are separated by either a period or
     * an exclamation point or a question mark or more than one
     * than any of them.
     */
    public void divideTextInSentences() {

        String sentence = "";
        int i = 0;

        while (i < beginningText.length()) {

            while ((beginningText.charAt(i) != '.' && beginningText.charAt(i) != '!' && beginningText.charAt(i) != '?') ||
                    ((beginningText.charAt(i) == '.' || beginningText.charAt(i) == '?' || beginningText.charAt(i) == '!') &&
                            i < beginningText.length() - 1 && beginningText.charAt(i + 1) != ' '))/*(beginningText.charAt(i) == '.' && ((i > 0 && beginningText.charAt(i - 1) == '.') || (i < beginningText.length() - 1 && (beginningText.charAt(i + 1) == '.'))) || (beginningText.charAt(i) != '.' && beginningText.charAt(i) != '!' && beginningText.charAt(i) != '?'))*/ {

                sentence += beginningText.charAt(i);

                i++;
            }
            sentence += beginningText.charAt(i);
            sentences.add(new Sentence(sentence));
            sentence = "";
            i += 2; //Skip space after period
        }
    }

    /**
     * Calculates percentile position of a sentence
     * in relation to the text as a whole.
     */
    public void calcSentencePercPos() {

        double size = sentences.size();
        for (int i = 0; i < sentences.size(); i++) {
            double index = i + 1;
            sentences.get(i).calculatePercPos(index, size);
        }
    }

    /**
     * Calculates the frequency (i.e. occurrence)
     * of any word with at least 4 letters
     * in the whole text.
     */
    public void calculateWordFrequency() {

        for (Sentence s : sentences) {
            for (String w : s.getWords()) {
                if (w.length() >= 4 && !w.equals("that") && !w.equals("with") && !w.equals("over")
                        && !w.equals("they") && !w.equals("does") && !w.equals("when")
                        && !w.equals("goes") && !w.equals("where") && !w.equals("which")
                        && !w.equals("without") && !w.equals("some") && !w.equals("said")
                        && !w.equals("then") && !w.equals("from") && !w.equals("this") && !w.equals("went")
                        && !w.equals("while") && !w.equals("until") && !w.equals("says") && !w.equals("about")
                        && !w.equals("will") && !w.equals("after") && !w.equals("before")
                        && !w.equals("well") && !w.equals("have") && !w.equals("done") && !w.equals("been")
                        && !w.equals("instead") && !w.equals("just") && !w.equals("because") && !w.equals("very")
                        && !w.equals("really") && !w.equals("could") && !w.equals("should") && !w.equals("shall")
                        && !w.equals("there") && !w.equals("their") && !w.equals("here") && !w.equals("itself")
                        && !w.equals("herself") && !w.equals("yourself") && !w.equals("himself") && !w.equals("themselves")
                        && !w.equals("yourselves") && !w.equals("ourselves") && !w.equals("myself") & !w.equals("would")
                        && !w.equals("wouldn") && !w.equals("shouldn") && !w.equals("more") && !w.equals("much") && !w.equals("most")
                        && !w.equals("made") && !w.equals("must") && !w.equals("want") && !w.equals("what") && !w.equals("wish")
                        && !w.equals("your") && !w.equals("once")) {
                    if (!frequencyMap.containsKey(w)) {
                        frequencyMap.put(w, 1);
                    } else {
                        frequencyMap.replace(w, frequencyMap.get(w) + 1);
                    }
                }
            }
        }
    }

    /**
     * Calculates how many words does the sentence
     * have in common with the title, which is the first sentence
     * of the list. The number of words in common is then divided by the number
     * of words in the sentence.
     */
    public void calculateSimilarityToTitle() {

        sentences.get(0).setSimilarityToTitle(1);
        double wordsInCommon = 0;
        for (int i = 1; i < sentences.size(); i++) {
            for (int j = 0; j < sentences.get(i).getWords().size(); j++) {
                for (int k = 0; k < sentences.get(0).getWords().size(); k++) {
                    if (sentences.get(i).getWords().get(j).equals(sentences.get(0).getWords().get(k))) {
                        wordsInCommon++;
                    }
                }
            }
            sentences.get(i).setSimilarityToTitle(wordsInCommon / (double)sentences.get(i).getWords().size());
            wordsInCommon = 0;
        }
    }

    /**
     * Sorts the words in the hashmap by frequency
     * and saves the ones with the highest frequency - including two-worded keywords
     */
    public void setKeywords() {

        Map<String, Integer> sortedMap = sortByValue(frequencyMap);
        int doubleKeywordCounter = 0;
        int regKeywordCounter = 0;

        Set<String> keys = sortedMap.keySet();
        String[] keysArray = keys.toArray(new String[keys.size()]);

        for (int i = 0; i < keywords.length; i++) {

            if (doubleKeywords.size() > 0 && doubleKeywordCounter < doubleKeywords.size()) {
                if (doubleKeywordsFrequency.get(doubleKeywordCounter) >= sortedMap.get(keysArray[regKeywordCounter])) {
                    keywords[i] = doubleKeywords.get(doubleKeywordCounter);
                    doubleKeywordCounter++;
                    if (doubleKeywords.get(doubleKeywordCounter - 1).contains(keysArray[regKeywordCounter]) && doubleKeywords.get(doubleKeywordCounter - 1).contains(keysArray[regKeywordCounter + 1])) {
                        regKeywordCounter += 2;
                    }
                } else {
                    keywords[i] = keysArray[i];
                    regKeywordCounter++;
                }
            } else {
                keywords[i] = keysArray[regKeywordCounter];
                regKeywordCounter++;
            }
        }
    }

    /**
     * Sets num of keywords based on text length
     * @return num of keywords
     */
    private int calculateNumOfKeywords() {

        int num = 0;
        if (beginningText.length() > 0 && beginningText.length() <= 100) {
            num = 1;
        } else if (beginningText.length() > 100 && beginningText.length() <= 1000) {
            num = 3;
        } else if (beginningText.length() > 1000 && beginningText.length() <= 3000) {
            num = 4;
        } else if (beginningText.length() > 3000 && beginningText.length() <= 10000) {
            num = 5;
        } else if (beginningText.length() > 10000) {
            num = 8;
        }

        return num;
    }

    /**
     * Given a hashmap, it returns
     * a linked hashmap sorted by its values.
     * Code from https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
     * @param map hashmap
     * @param <K> first generic
     * @param <V> second generic
     * @return hashmap sorted by value
     */
    private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o2)).getValue()).compareTo(((Map.Entry<K, V>) (o1)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Check if two words occur with the same frequency, at least 3 times
     * and are close to each other, they count as one single keyword
     */
    public void checkDoubleKeywords() {
        Map.Entry tempEntry = null;
        for (Map.Entry entry : frequencyMap.entrySet()) {
            if (tempEntry != null && (Integer)tempEntry.getValue() > 2 && tempEntry.getValue() == entry.getValue() ) {
                doubleKeywords.add((tempEntry.getKey() + " " + entry.getKey()));
                doubleKeywordsFrequency.add((Integer)tempEntry.getValue());
            }
            tempEntry = entry;
        }
    }

    /**
     * Finds size of the largest sentence in the text
     * @return size (i.e. num of words) of the longest sentence in the text
     */
    private double findLongestSentenceSize() {

        int index = 0;

        for (int i = 1; i < sentences.size(); i++) {
            if (sentences.get(i).getWords().size() > sentences.get(index).getWords().size()) {
                index = i;
            }
        }

        return (double)sentences.get(index).getWords().size();
    }

    /**
     * Calculates the relative length
     * for each sentence
     */
    public void calcSentencesRelativeLength() {

        double maxSize = findLongestSentenceSize();

        for (int i = 0; i < sentences.size(); i++) {
            sentences.get(i).calcRelativeLength(maxSize);
        }
    }

    /**
     * Initializes array of cohesive values
     * in Sentence
     */
    private void initializeSentences() {
        for (int i = 0; i < sentences.size(); i++) {

            sentences.get(i).setSimilarityToOtherSentences(sentences.size() - 1);
        }
    }

    /**
     * A total of 4 nested loops to calculate how many words
     * each sentence has in common with all other sentences
     * in the text
     */
    public void calcSimilarityToOtherSentences() {
        initializeSentences();

        int firstSentenceIndex = 0;
        int secondSentenceIndex = 0;
        int sentenceCounter = 0;

        for (int i = 0; i < sentences.size(); i++) {

            for (int j = 0; j < sentences.size(); j++) {

                if (i != j) {

                    firstSentenceIndex = 0;
                    while (firstSentenceIndex < sentences.get(i).getWords().size()) {
                        secondSentenceIndex = 0;
                        while (secondSentenceIndex < sentences.get(j).getWords().size()) {

                            if (sentences.get(i).getWords().get(firstSentenceIndex).equals(sentences.get(j).getWords().get(secondSentenceIndex))) {
                                sentences.get(i).getSimilarityToOtherSentences()[sentenceCounter]++;
                            }
                            secondSentenceIndex++;
                        }
                        firstSentenceIndex++;

                    }
                    //sentences.get(i).getSimilarityToOtherSentences()[sentenceCounter] = sentences.get(i).getSimilarityToOtherSentences()[sentenceCounter] / sentences.get(i).getWords().size();
                    sentenceCounter++;
                }

            }
            sentenceCounter = 0;
        }
    }

    /**
     * Calls for method in Sentence
     */
    private void calcRawCohesionValue() {

        for (Sentence s : sentences) {
            s.calcRawCohesionValue();
        }
    }

    /**
     * Finds alrgest RCV
     * @return largest RCV
     */
    private double findLargestRawCohesionValue() {

        calcRawCohesionValue();

        int index = 0;

        for (int i = 1; i < sentences.size(); i++) {

            if (sentences.get(i).getRawCohesionValue() > sentences.get(index).getRawCohesionValue()) {
                index = i;
            }
        }
        return sentences.get(index).getRawCohesionValue();
    }

    /**
     * Calculates CV for each sentence
     */
    public void calcCohesionValue() {

        double maxCV = findLargestRawCohesionValue();

        for (Sentence s : sentences) {
            s.calcCohesionValue(maxCV);
        }
    }



}
