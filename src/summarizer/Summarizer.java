package summarizer;

import Neural.autodiff.Graph;
import Neural.matrix.Matrix;
import Neural.model.SigmoidUnit;
import Neural.model.SineUnit;
import Neural.model.TanhUnit;
import summarizer.POSCategorizer;
import summarizer.Sentence;

import java.security.SecureRandom;
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
    public final int COMPRESSION_RATE = 40;
    public ArrayList<Sentence> finalSentences;

    private Matrix matrix = null;

    public Summarizer(String text) {

        beginningText = text;
        frequencyMap = new LinkedHashMap<>();
        keywords = new String[calculateNumOfKeywords()];
        doubleKeywords = new ArrayList<>();
        doubleKeywordsFrequency = new ArrayList<>();
        mfNouns = new ArrayList<>();
        nounKeywords = new ArrayList<>();
        finalSentences = new ArrayList<>();
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

    public Matrix getMatrix(){return matrix;}

    public void chooseSentences() {

        int numOfFinalSentences = (sentences.size() * COMPRESSION_RATE) / 100;
        numOfFinalSentences = sentences.size() - numOfFinalSentences;

        int thirtyPercent = (numOfFinalSentences * 30) / 100;


        //NOTE: now similarity to title is / 2 because it should not be as important as other features.
        //This is because the title is the first sentence of the paragraph which often has nothing to do
        //with the actual title of the document

        // Load a mask
        matrix = new Matrix(6,sentences.size());
        Graph graph = new Graph();
        // TODO replace with trained filter
        Matrix filter = Matrix.rand(1,6,1,new Random());
        try {
            filter = graph.nonlin(new SigmoidUnit(), filter);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(filter.toString());
        // populate the matrix
        for (int i = 0; i < sentences.size(); i++) {
            matrix.setW(0,i,sentences.get(i).getRelativeLength());
            matrix.setW(1,i,sentences.get(i).getSimilarityToKeywords());
            matrix.setW(2,i,sentences.get(i).getCohesionValue());
            matrix.setW(3,i,sentences.get(i).getSimilarityToTitle()/2);
            matrix.setW(4,i,sentences.get(i).getMeanWordFrequency());

            //Calculate total score for each sentence
            sentences.get(i).setTotalScore(sentences.get(i).getRelativeLength() + sentences.get(i).getSimilarityToKeywords() +
                    sentences.get(i).getCohesionValue() + (sentences.get(i).getSimilarityToTitle()/2) + sentences.get(i).getMeanWordFrequency());
            matrix.setW(5,i,sentences.get(i).getTotalScore()/5);

        }
        try {
            matrix = graph.nonlin(new SigmoidUnit(),matrix);
            System.out.println(matrix.toString());

            // Multiply a matrix by a filter
            StringBuilder returnString = new StringBuilder();
            matrix = graph.mul(filter,matrix);
            System.out.println(matrix.toString());
            // Create an array to hold indexes of selected sentences
            int [] index = new int[matrix.w.length*COMPRESSION_RATE/100];
            returnString.append("<br />Sentences in summary with AI: ").append(index.length+2).append("<hr />");
            returnString.append(sentences.get(0).getText()).append("<br/>");

            // find MAX in the product of matrices
            for (int j = 0;j<index.length;j++){
                int max = 0;
                for (int i =1;i<matrix.w.length-1;i++) {
                    if (matrix.w[i]>max){
                        max = i;
                        // Clear the value, so the sentence wouldn't be repeated
                        matrix.w[i] = 0;
                    }
                }
                if (max!=0) {
                    index[j] = max;
                }
            }
            // Sort the selected values, so it would follow the text
            Arrays.sort(index);
            for (int i :index){
                System.out.print(i+"\t\t");
                // Add it to the string to return
                returnString.append(sentences.get(i).getText()).append("<br/>");
            }
            System.out.println();
            // add the last sentence
            returnString.append(sentences.get(sentences.size()-1).getText()).append("<br/>");
            returnString.append("<hr />");
            // insert the summary as one sentence.
            finalSentences.add(new Sentence(returnString.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
        //Always add first and last sentences of the text
        finalSentences.add(sentences.get(0));
        finalSentences.add(sentences.get(sentences.size() - 1));

        //Sort them by total score in descending order
       Collections.sort(sentences, new Comparator<Sentence>() {
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


    }
}
