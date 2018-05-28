package summarizer;

import Neural.FilterTron;
import Neural.autodiff.Graph;
import Neural.matrix.Matrix;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
    public ArrayList<String> mfNouns;
    public ArrayList<String> nounKeywords;
    public final int COMPRESSION_RATE = 60;
    public ArrayList<Sentence> finalSentences;
    private FilterTron filterTron;
    public ArrayList<Sentence> finalSentencesAI;

    private Matrix matrix = null;

    public Summarizer(String text) {

        beginningText = text;
        frequencyMap = new LinkedHashMap<>();
        int numOfKW = calculateNumOfKeywords();
        keywords = new String[numOfKW];
        doubleKeywords = new ArrayList<>();
        doubleKeywordsFrequency = new ArrayList<>();
        mfNouns = new ArrayList<>();
        nounKeywords = new ArrayList<>();
        finalSentences = new ArrayList<>();
        finalSentencesAI = new ArrayList<>();
        filterTron = new FilterTron();
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Divides text into sentences.
     * Sentences are separated by either a period or
     * an exclamation point or a question mark or more than one
     * than any of them.
     * EDIT: 22/05/2018 question marks do not separate sentences anymore
     * as done by SMMRY
     * NOTE: Dr., Mr., Mrs., Ms., Col., Gen., Lt., etc. and lit. do not separate sentences
     * neither do single letters followed by a period (such as middle name initials).
     */
    public void divideTextInSentences() {

        String sentence = "";
        int i = 0;
        beginningText = beginningText.replace("\n", " ");

        while (i < beginningText.length()) {

            while ((beginningText.charAt(i) != '.' && beginningText.charAt(i) != '!' /*&& beginningText.charAt(i) != '?'*/) ||
                    ((beginningText.charAt(i) == '.' /*|| beginningText.charAt(i) == '?'*/ || beginningText.charAt(i) == '!') &&
                            i < beginningText.length() - 1 && beginningText.charAt(i + 1) != ' ') || (i > 1 && beginningText.charAt(i) == '.'
                    && Character.isLetter(beginningText.charAt(i - 1)) && beginningText.charAt(i - 2) == ' ') ||
                    (i > 2 && beginningText.charAt(i) == '.' && Character.isLetter(beginningText.charAt(i - 1))
                            && beginningText.charAt(i - 2) == '.') || (i > 1 && beginningText.charAt(i) == '.' &&
                    beginningText.charAt(i - 1) == 'r' && beginningText.charAt(i - 2) == 'M') || (i > 1 && beginningText.charAt(i) == '.' &&
                    beginningText.charAt(i - 1) == 's' && beginningText.charAt(i - 2) == 'M') || (i > 2 && beginningText.charAt(i) == '.' && beginningText.charAt(i - 1) == 'c'
                    && beginningText.charAt(i - 2) == 't' && beginningText.charAt(i - 3) == 'e') || (i > 2 && beginningText.charAt(i) == '.' && beginningText.charAt(i - 1) == 't'
                    && beginningText.charAt(i - 2) == 'i' && beginningText.charAt(i - 3) == 'l') || (i > 2 && beginningText.charAt(i) == '.' && beginningText.charAt(i - 1) == 's'
                    && beginningText.charAt(i - 2) == 'r' && beginningText.charAt(i - 3) == 'M') || (i > 1 && beginningText.charAt(i) == '.' &&
                    beginningText.charAt(i - 1) == 'r' && beginningText.charAt(i - 2) == 'D') || (i > 2 && beginningText.charAt(i) == '.' && beginningText.charAt(i - 1) == 'l'
                    && beginningText.charAt(i - 2) == 'o' && beginningText.charAt(i - 3) == 'C') || (i > 2 && beginningText.charAt(i) == '.' && beginningText.charAt(i - 1) == 'n'
                    && beginningText.charAt(i - 2) == 'e' && beginningText.charAt(i - 3) == 'G') || (i > 1 && beginningText.charAt(i) == '.' &&
                    beginningText.charAt(i - 1) == 't' && beginningText.charAt(i - 2) == 'L')) {

                sentence += beginningText.charAt(i);

                i++;
            }
            sentence += beginningText.charAt(i);
            sentences.add(new Sentence(sentence));
            sentences.get(sentences.size() - 1).order = sentences.size() - 1;
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
                        && !w.equals("your") && !w.equals("once") && !w.equals("each") && !w.equals("every") && !w.equals("also")
                        && !w.equals("everyone") && !w.equals("something") && !w.equals("anyone") && !w.equals("somebody") && !w.equals("anybody")) {
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
    public void calculateSimilarityToTitle(String heading) {

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
            sentences.get(i).setSimilarityToTitle(wordsInCommon / (double) sentences.get(i).getWords().size());
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
     *
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
     *
     * @param map hashmap
     * @param <K> first generic
     * @param <V> second generic
     * @return hashmap sorted by value
     */
    private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o2)).getValue()).compareTo(((Map.Entry<K, V>) (o1)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
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
            if (tempEntry != null && (Integer) tempEntry.getValue() > 2 && tempEntry.getValue() == entry.getValue()) {
                doubleKeywords.add((tempEntry.getKey() + " " + entry.getKey()));
                doubleKeywordsFrequency.add((Integer) tempEntry.getValue());
            }
            tempEntry = entry;
        }
    }

    /**
     * Finds size of the largest sentence in the text
     *
     * @return size (i.e. num of words) of the longest sentence in the text
     */
    private double findLongestSentenceSize() {

        int index = 0;

        for (int i = 1; i < sentences.size(); i++) {
            if (sentences.get(i).getWords().size() > sentences.get(index).getWords().size()) {
                index = i;
            }
        }

        return (double) sentences.get(index).getWords().size();
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
     * in summarizer.Sentence
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
     * Calls for method in summarizer.Sentence
     */
    private void calcRawCohesionValue() {

        for (Sentence s : sentences) {
            s.calcRawCohesionValue();
        }
    }

    /**
     * Finds alrgest RCV
     *
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

    /**
     * Uses OpenNLP library to find the nouns
     * of some important sentences
     */
    public void findNouns() {

        for (Sentence s : sentences) {
            if (s.getCohesionValue() > 0.5 && s.getRelativeLength() > 0.5) {
                POSCategorizer posCategorizer = new POSCategorizer(s.getText());
                posCategorizer.run();
            }
        }
    }

    /**
     * Compares frequency of nouns found thanks to the OpenNLP library
     * and stores the most recurrent in mfNouns
     */
    public void findMostFrequentNouns() {

        int counter = 0;
        int limit;
        Map<String, Integer> sortedMap = sortByValue(frequencyMap);

        if (sortedMap.size() > 15) {
            int difference = sortedMap.size() - 15;
            sortedMap.keySet().removeAll(Arrays.asList(sortedMap.keySet().toArray()).subList(difference, frequencyMap.size() - 1));
        }

        if (POSCategorizer.getNouns().size() < 15) {
            limit = POSCategorizer.getNouns().size();
        } else {
            limit = 15;
        }

        for (int i = 0; i < limit; i++) {
            if (sortedMap.containsKey(POSCategorizer.getNouns().get(i)) && !mfNouns.contains(POSCategorizer.getNouns().get(i))) {
                mfNouns.add(POSCategorizer.getNouns().get(i));
            }
        }
    }

    /**
     * Checks whether a sentence contains at least one of the main
     * concepts indicator (i.e. nouns with highest frequency)
     */
    public void setMainConceptIndicator() {


        for (Sentence s : sentences) {
            for (String sr : mfNouns) {
                if (s.getWords().contains(sr)) {
                    s.setMainConcept(true);
                    break;
                }
            }
        }
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void chooseSentences() {

        int numOfFinalSentences = (sentences.size() * COMPRESSION_RATE) / 100;
        //adapt this compression rate to matrices rate
        //numOfFinalSentences = sentences.size() - numOfFinalSentences;

        int thirtyPercent = (numOfFinalSentences * 30) / 100;


        //NOTE: now similarity to title is / 2 because it should not be as important as other features.
        //This is because the title is the first sentence of the paragraph which often has nothing to do
        //with the actual title of the document

        // Load a mask
        matrix = new Matrix(6, sentences.size());
        // populate the matrix
        for (int i = 0; i < sentences.size(); i++) {
            matrix.setW(0, i, sentences.get(i).getRelativeLength());
            matrix.setW(1, i, sentences.get(i).getSimilarityToKeywords());
            matrix.setW(2, i, sentences.get(i).getCohesionValue());
            matrix.setW(3, i, sentences.get(i).getSimilarityToTitle() / 2);
            matrix.setW(4, i, sentences.get(i).getMeanWordFrequency());

            //Calculate total score for each sentence
            sentences.get(i).setTotalScore(sentences.get(i).getRelativeLength() + sentences.get(i).getSimilarityToKeywords() +
                    sentences.get(i).getCohesionValue() + (sentences.get(i).getSimilarityToTitle() / 2) + sentences.get(i).getMeanWordFrequency());

            matrix.setW(5, i, sentences.get(i).getTotalScore() / 5);

        }
        try {
            Graph.normalize(matrix);
            System.out.println(matrix.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Always add first and last sentences of the text
        finalSentences.add(sentences.get(0));
        finalSentences.add(sentences.get(sentences.size() - 1));

        //Sort them by total score in descending order
        sentences.sort(new Comparator<Sentence>() {
            @Override
            public int compare(Sentence o1, Sentence o2) {
                if (o2.getTotalScore() > o1.getTotalScore()) {
                    return 1;
                } else if (o2.getTotalScore() == o1.getTotalScore()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        //Pick 30% of sentences from the beginning of the text, 30% from the middle
        //and 30% from the end preferably with proper names, no anaphores and no non-essential info
        //higher score first
        int begCounter = 0;
        int i = 0;
        while (i < sentences.size() && begCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {

            if (sentences.get(i).getPercentilePos() <= 0.33) {
                if (!finalSentences.contains(sentences.get(i)) && sentences.get(i).getHasProperName() && !sentences.get(i).getHasAnaphors() && !sentences.get(i).getHasNonEssentialInfo()) {
                    finalSentences.add(sentences.get(i));
                    begCounter++;
                    //System.out.println(i);
                }
            }
            i++;
        }

        if (begCounter < thirtyPercent) {
            i = 0;
            while (i < sentences.size() && begCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {
                if (sentences.get(i).getPercentilePos() <= 0.33) {
                    if (!finalSentences.contains(sentences.get(i)) && !sentences.get(i).getHasProperName() && !sentences.get(i).getHasAnaphors() && !sentences.get(i).getHasNonEssentialInfo()) {
                        finalSentences.add(sentences.get(i));
                        begCounter++;
                        // System.out.println(i);
                    }
                }
                i++;
            }

        }

        if (begCounter < thirtyPercent) {
            i = 0;
            while (i < sentences.size() && begCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {
                if (sentences.get(i).getPercentilePos() <= 0.33) {
                    if (!finalSentences.contains(sentences.get(i)) && sentences.get(i).getHasAnaphors() && !sentences.get(i).getHasNonEssentialInfo()) {
                        finalSentences.add(sentences.get(i));
                        begCounter++;
                        // System.out.println(i);
                    }
                }
                i++;
            }

        }

        int medCounter = 0;
        int j = 0;
        while (j < sentences.size() && medCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {

            if (sentences.get(j).getPercentilePos() > 0.33 && sentences.get(j).getPercentilePos() <= 0.66) {
                if (!finalSentences.contains(sentences.get(j)) && sentences.get(j).getHasProperName() && !sentences.get(j).getHasAnaphors() && !sentences.get(j).getHasNonEssentialInfo()) {
                    finalSentences.add(sentences.get(j));
                    medCounter++;
                    // System.out.println(j);
                }
            }
            j++;
        }

        if (medCounter < thirtyPercent) {
            j = 0;
            while (j < sentences.size() && medCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {
                if (sentences.get(j).getPercentilePos() > 0.33 && sentences.get(j).getPercentilePos() <= 0.66) {
                    if (!finalSentences.contains(sentences.get(j)) && !sentences.get(j).getHasProperName() && !sentences.get(j).getHasAnaphors() && !sentences.get(j).getHasNonEssentialInfo()) {
                        finalSentences.add(sentences.get(j));
                        medCounter++;
                        //System.out.println(j);
                    }
                }
                j++;
            }

        }

        if (medCounter < thirtyPercent) {
            j = 0;
            while (j < sentences.size() && medCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {
                if (sentences.get(j).getPercentilePos() > 0.33 && sentences.get(j).getPercentilePos() <= 0.66) {
                    if (!finalSentences.contains(sentences.get(j)) && sentences.get(j).getHasAnaphors() && !sentences.get(j).getHasNonEssentialInfo()) {
                        finalSentences.add(sentences.get(j));
                        //System.out.println(j);
                        medCounter++;
                    }
                }
                j++;
            }

        }

        int endCounter = 0;
        int k = 0;
        while (k < sentences.size() && endCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {

            if (sentences.get(k).getPercentilePos() > 0.66) {
                if (!finalSentences.contains(sentences.get(k)) && sentences.get(k).getHasProperName() && !sentences.get(k).getHasAnaphors() && !sentences.get(k).getHasNonEssentialInfo()) {
                    finalSentences.add(sentences.get(k));
                    //System.out.println(k);
                    endCounter++;
                }
            }
            k++;
        }

        if (endCounter < thirtyPercent) {
            k = 0;
            while (k < sentences.size() && endCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {
                if (sentences.get(k).getPercentilePos() > 0.66) {
                    if (!finalSentences.contains(sentences.get(k)) && !sentences.get(k).getHasProperName() && !sentences.get(k).getHasAnaphors() && !sentences.get(k).getHasNonEssentialInfo()) {
                        finalSentences.add(sentences.get(k));
                        //System.out.println(k);
                        endCounter++;
                    }
                }
                k++;
            }

        }

        if (endCounter < thirtyPercent) {
            k = 0;
            while (k < sentences.size() && endCounter <= thirtyPercent && finalSentences.size() <= numOfFinalSentences) {
                if (sentences.get(k).getPercentilePos() > 0.66) {
                    if (!finalSentences.contains(sentences.get(k)) && sentences.get(k).getHasAnaphors() && !sentences.get(k).getHasNonEssentialInfo()) {
                        finalSentences.add(sentences.get(k));
                        //System.out.println(k);
                        endCounter++;
                    }
                }
                k++;
            }

        }

        //Sort them back by order
        Collections.sort(finalSentences, new Comparator<Sentence>() {
            @Override
            public int compare(Sentence o1, Sentence o2) {
                if (o1.order > o2.order) {
                    return 1;
                } else if (o1.order == o2.order) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        Collections.sort(sentences, new Comparator<Sentence>() {
            @Override
            public int compare(Sentence o1, Sentence o2) {
                if (o1.order > o2.order) {
                    return 1;
                } else if (o1.order == o2.order) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });


    }

    //Hard-coded for Cinderella text http://webshare.hkr.se/FECO0002/cinderella.html
    //18 sentences
    //Indexes: 0, 1, 3, 4, 9, 18, 19, 22, 23, 25, 27, 28, 30, 31, 33, 35, 37, 40
    //25 sentences
    //Indexes: 0, 1, 3, 4, 9, 12, 13, 14, 18, 19, 22, 23, 25, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 39, 40
    public void train(int[] index) {

        // total matching set to 2 because first and last sentences are always added
        int totalMatching = 2;
        int falseSentences = 0;
        for (int i : index) {
            int score = checkMatchingSentences(i);
            double[] sample = {matrix.getW(0,i),matrix.getW(1,i),matrix.getW(2,i),matrix.getW(3,i),matrix.getW(4,i),matrix.getW(5,i)};

            filterTron.train(sample,score);
            // if sentences match
            if (score > 0) {
                for (int l = 0; l < 6; l++) {
                    double value = (filterTron.filter.getW(0, l) + matrix.getW(l, i)) * filterTron.LEARNING_RATE;
                    filterTron.filter.setW(0, l, filterTron.filter.getW(0, l) + value);
                }
                totalMatching++;
            } else {
                for (int i1 = 0; i1 < 6; i1++) {
                    filterTron.filter.setW(0, i1, filterTron.filter.getW(0, i1) - (matrix.getW(i1, i) * filterTron.LEARNING_RATE));

                }
                falseSentences++;
            }
            // Add it to the string to return

        }
        System.out.println("\nMatching Sentences " + totalMatching + "\nFalse sentences " + falseSentences+
                "\nMatching rate "+totalMatching*100/(totalMatching+falseSentences));

        //printSeparator();
//        int[] indexes = {0, 1, 3, 4, 9, 12, 13, 14, 18, 19, 22, 23, 25, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 39, 40};
//        //Cinderella: int[] indexes = {0, 1, 3, 4, 9, 12, 13, 14, 18, 19, 22, 23, 25, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 39, 40};
//        //Snow White: int[] indexes = {0, 1, 2, 3, 4, 6, 7, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 21, 24, 25, 26, 35, 37, 39, 41, 42, 45, 46};
//        //Elephants: int[] indexes = {0, 1, 5, 6, 9, 10, 13, 14, 15, 16, 17, 19, 20, 21, 22, 24, 25, 28, 29};
//        //Quarks: int[] indexes = {0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 25, 29, 30, 32};
//        //AI game: int[] indexes = {3, 4, 7, 8, 9, 10, 11, 12, 13, 14, 18, 19};
//        //Networked cars: int[] indexes = {2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14};
//        double[] inputs = new double[6];
//
//        //for (int i = 0; i < sentences.size(); i++)
//        for (int i = 0; i < matrix.cols; i++) {
//            /*System.out.printf("Sentence %d: RL: %.2f, SKW: %.2f, CV: %.2f, ST: %.2f, MWF: %.2f, TS: %.2f%n", i,
//                    matrix.getW(0, i), matrix.getW(1, i), matrix.getW(2, i),
//                    matrix.getW(3, i), matrix.getW(4, i), matrix.getW(5, i));*/
//
//            //inputs[0] = sentences.get(i).getRelativeLength();
//            //inputs[1] = sentences.get(i).getSimilarityToKeywords();
//            //inputs[2] = sentences.get(i).getCohesionValue();
//            //inputs[3] = sentences.get(i).getSimilarityToTitle();
//            //inputs[4] = sentences.get(i).getMeanWordFrequency();
//            //inputs[5] = sentences.get(i).getTotalScore();
//            inputs[0] = matrix.getW(0, i); //relative length
//            inputs[1] = matrix.getW(1, i); //similarity to keywords
//            inputs[2] = matrix.getW(2, i); //cohesion value
//            inputs[3] = matrix.getW(3, i); //similarity to title
//            inputs[4] = matrix.getW(4, i); //mean word frequency
//            inputs[5] = matrix.getW(5, i); //total score
//            int guess = filterTron.nonRandomGuess(inputs);
//            boolean isChosen = false;
//            boolean actualChosen;
//            if (finalSentences.contains(sentences.get(i))) {
//                actualChosen = true;
//            } else {
//                actualChosen = false;
//            }
//            for (int j = 0; j < indexes.length; j++) {
//                if (indexes[j] == i) {
//                    isChosen = true;
//                    break;
//                } else {
//                    isChosen = false;
//                }
//            }
//
//            int target = computeIncreasing(isChosen, actualChosen);
//            filterTron.train(inputs, target);
//            if (guess == target) {
//
//            } else {
//                if (isChosen && !actualChosen /*|| (isChosen && actualChosen)*/) {
//                    matrix.setW(5, i, matrix.getW(5, i) + 1);
//                    //sentences.get(i).setTotalScore(sentences.get(i).getTotalScore() + 1);
//                } else if (!isChosen && actualChosen /*|| (!isChosen && !actualChosen)*/) {
//                    matrix.setW(5, i, matrix.getW(5, i) - 1);
//                    //sentences.get(i).setTotalScore(sentences.get(i).getTotalScore() - 1);
//                }
//            }
//        }
        System.out.print(filterTron.filter);
    }

    private int computeIncreasing(boolean isChosen, boolean actualIsChosen) {

        if (isChosen && !actualIsChosen) { //sentence should be chosen but it wasn't
            return 1;
        } else if (!isChosen && actualIsChosen) { //sentence should not be chosen but it was
            return -1;
        } else { //sentence should be chosen and it was, or should not be chosen and it wasn't
            return 0;
        }
    }

    private void printSeparator() {
        System.out.println("------------ TRAINING -------------");
    }

    public void displayForBetterUnderstanding() {

        //System.out.println("------------ AFTER TRAINING -----------");

        /*for (int i = 0; i < sentences.size(); i++) {
            System.out.printf("Sentence %d: RL: %.2f, SKW: %.2f, CV: %.2f, ST: %.2f, MWF: %.2f, TS: %.2f%n", sentences.get(i).order,
                    sentences.get(i).getRelativeLength(), sentences.get(i).getSimilarityToKeywords(), sentences.get(i).getCohesionValue(),
                    sentences.get(i).getSimilarityToTitle(), sentences.get(i).getMeanWordFrequency(), sentences.get(i).getTotalScore());
        }*/
        /*for (int i = 0; i < matrix.cols; i++) {
            System.out.printf("Sentence %d: RL: %.2f, SKW: %.2f, CV: %.2f, ST: %.2f, MWF: %.2f, TS: %.2f%n", i,
                    matrix.getW(0, i), matrix.getW(1, i), matrix.getW(2, i),
                    matrix.getW(3, i), matrix.getW(4, i), matrix.getW(5, i));
        }*/
        //reorganizeAISentences();
    }

    private void reorganizeAISentences() {
        HashMap<Integer, Double> scores = new HashMap<>();
        for (int i = 0; i < matrix.cols; i++) {
            scores.put(i, matrix.getW(5, i));
        }
        Map<Integer, Double> sortedScores = sortByValue(scores);

        Set<Integer> indexes = sortedScores.keySet();
        Integer[] indexArray = indexes.toArray(new Integer[finalSentences.size()]);
        Integer[] helpArray = new Integer[25];

        System.arraycopy(indexArray, 0, helpArray, 0, helpArray.length);

        Arrays.sort(helpArray);
        for (int i = 0; i < helpArray.length; i++) {
            finalSentencesAI.add(sentences.get(helpArray[i]));
        }
    }

    public void saveFilter() {
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream("Filter.mx"))) {
            filterTron.normalize();
            fos.writeObject(filterTron.filter);
            System.out.println("Filter saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAISentences() {
        Graph graph = new Graph();
        try {
            // Multiply a matrix by a filter
            Matrix mx = matrix.clone();
            StringBuilder returnString = new StringBuilder();
            filterTron.normalize();
            mx = graph.mul(filterTron.filter, mx);
            System.out.println(mx.toString());
            // Create an array to hold indexes of selected sentences
            int[] index = new int[mx.w.length * COMPRESSION_RATE / 100];
            returnString.append("<br />Sentences in summary with AI: ").append(index.length + 2).append("<hr />");
            returnString.append(sentences.get(0).getText()).append("<br/>");

            // find MAX in the product of matrices
            for (int j = 0; j < index.length; j++) {
                int max = 1;
                for (int i = 1; i < mx.w.length - 1; i++) {
                    if (mx.w[i] > mx.w[max]) {
                        max = i;
                    }
                }
                index[j] = max;
                // Clear the value, so the sentence wouldn't be repeated
                mx.w[max] = 0;
            }
            System.out.println(mx.toString());
            // Sort the selected values, so it would follow the text
            Arrays.sort(index);

            // training
            for (int i=0;i<100;i++)
            train(index);

            for (int i:index){
                returnString.append(sentences.get(i).getText()).append("<br/>");
            }
            returnString.append(sentences.get(sentences.size() - 1).getText()).append("<br/>");

            returnString.append("<hr />");
            // insert the summary as one sentence.
            finalSentencesAI.add(new Sentence(returnString.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int checkMatchingSentences(int chosen) {
        int[] indexes = {0, 1, 3, 4, 9, 12, 13, 14, 18, 19, 22, 23, 25, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 39, 40};
        for (int index : indexes) {
            if (chosen == index) {
                return +1;
            }
        }
        return -1;

    }
}
