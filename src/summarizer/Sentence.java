package summarizer;

import java.util.ArrayList;

/**
 * Created by Federica on 26/04/2018.
 */
public class Sentence {

    private String text;
    private int wordsInText;
    private ArrayList<String> words;
    private ArrayList<String> properNames;

    private double relativeLength;
    //length of the sentence compared to longest sentence in the document

    private double percentilePos;
    //percentile position of the sentence in the document, calculated by
    //position number of the sentence in the document / total num of sentences in the document
    //page 10 of KEA: Practical Automatic Keyphrase Extraction

    private double meanWordFrequency; //average based on how frequent the words
    //within the sentence are compared to the words in all other sentences

    private double similarityToTitle; //average of how many words in the sentence
    //match the words in the title

    private double similarityToKeywords; //average of how many wprds in the sentence
    //match the text's keywords as calculated in the summarizer.Summarizer class

    private double[] similarityToOtherSentences; //for each other sentence in the text
    //it collects how many words they have in common divided by num of words in this sentence

    private double rawCohesionValue; //sum of all elements of similarityToOtherSentences

    private double cohesionValue; //rawCohesionValue / largest rawCohesionValue in the document

    private boolean mainConcept;

    private boolean hasProperName; //if it contains a proper name

    private boolean hasAnaphors; //if it contains anaphors

    private boolean hasNonEssentialInfo; //if it contains non-essential info

    private double totalScore;

    public int order;


    public Sentence(String text) {
        this.text = text;
        words = new ArrayList<>();
        properNames = new ArrayList<>();
        rawCohesionValue = 0.0;
        cohesionValue = 0.0;
        hasProperName = false;
        hasAnaphors = false;
        hasNonEssentialInfo = false;
    }

    public String getText() {
        return text;
    }

    public int getWordsInText() {
        return wordsInText;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public double getPercentilePos() {
        return percentilePos;
    }

    public double getMeanWordFrequency() { return meanWordFrequency; }

    public double getSimilarityToTitle() {
        return similarityToTitle;
    }

    public double getSimilarityToKeywords() { return similarityToKeywords; }

    public double getRelativeLength() { return relativeLength; }

    public double getRawCohesionValue() { return rawCohesionValue; }

    public double getCohesionValue() { return cohesionValue; }

    public double[] getSimilarityToOtherSentences() { return similarityToOtherSentences; }

    public boolean getMainConcept() { return mainConcept; }

    public ArrayList<String> getProperNames() { return properNames; }

    public boolean getHasProperName() {
        return hasProperName;
    }

    public boolean getHasAnaphors() { return hasAnaphors; }

    public boolean getHasNonEssentialInfo() { return hasNonEssentialInfo; }

    public double getTotalScore() {
        return totalScore;
    }

    public void setMainConcept(boolean concept) { mainConcept = concept; }

    public void setSimilarityToTitle(double similarityToTitle) {
        this.similarityToTitle = similarityToTitle;
    }

    public void setSimilarityToOtherSentences(int numOfSentences) { similarityToOtherSentences = new double[numOfSentences]; }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Counts words in a sentence. Words are separated
     * by a space or an apostrophe.
     * @return number of words in a sentence
     */
    public int countWordsInSentence() {

        int numOfWords = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\'' || text.charAt(i) == ' ') {
                numOfWords++;
            }
        }
        wordsInText = ++numOfWords;
        return numOfWords;
    }

    /**
     * Eliminates any punctuation except for spaces from sentence.
     * Replaces apostrophes with spaces.
     * @return String without any punctuation except for spaces
     */
    public String eliminatePunctuation() {
        String word = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ' && !Character.isLetter(text.charAt(i)) && text.charAt(i) != '\'') {
                continue;
            } else if (text.charAt(i) == '\'') {
                text = text.replace('\'', ' ');
            }
            word += text.charAt(i);
        }
       return word;
    }

    /**
     * Extracts words from sentences and
     * adds them to array in lower case.
     */
    public void extractWords() {

        String word = eliminatePunctuation();
        String singleWord = "";

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != ' ') {
                singleWord += word.charAt(i);
            } else {
                words.add(singleWord.toLowerCase());
                singleWord = "";
            }

            if (i == word.length() - 1) {
                words.add(singleWord.toLowerCase());
            }
        }
    }

    public void extractProperNames() {

        boolean dot = false;
        String name = "";
        boolean firstWord = false;
        for (int i = 0; i < text.length(); i++) {
            if (i == 0) {
                firstWord = true;
            }
            if (Character.isLetter(text.charAt(i))) {
                name += text.charAt(i);
            } else if (text.charAt(i) == '.') {
                dot = true;
            } else {
                if (name.length() > 0 && (int)name.charAt(0) >= 65 && (int)name.charAt(0) <= 90 && !firstWord && !dot && !name.equals("North")
                        && !name.equals("South") && !name.equals("East") && !name.equals("West") && !name.equals("January") && !name.equals("February")
                        && !name.equals("March") && !name.equals("April") && !name.equals("May") && !name.equals("June") && !name.equals("July")
                        && !name.equals("August") && !name.equals("September") && !name.equals("October") && !name.equals("November")
                        && !name.equals("December") && !name.equals("Monday") && !name.equals("Tuesday") && !name.equals("Wednesday")
                        && !name.equals("Thursday") && !name.equals("Friday") && !name.equals("Saturday") && !name.equals("Sunday")) {
                    properNames.add(name);
                    break;
                }
                firstWord = false;
                dot = false;
                name = "";
            }
        }
        if (properNames.size() > 0) {
            hasProperName = true;
        } else {
            hasProperName = false;
        }
    }

    public void calculatePercPos(double sentenceIndex, double totalSentences) {
        percentilePos = sentenceIndex / totalSentences;
    }


    /**
     * Calculates average frequency for sentence.
     */
    public void calculateMeanFrequency() {

        double total = 0;
        double totalFrequency = 0;
        for (int i = 0; i < words.size(); i++) {
            String w = words.get(i);
            if (w.length() >= 4 && !w.equals("that") && !w.equals("with") && !w.equals("over")
                    && !w.equals("they") && !w.equals("does") && !w.equals("when")
                    && !w.equals("goes") && !w.equals("where") && !w.equals("which")
                    && !w.equals("without") && !w.equals("some") && !w.equals("said")
                    && !w.equals("then") && !w.equals("from") && !w.equals("this") && !w.equals("went")
                    && !w.equals("while") && !w.equals("until") && !w.equals("says") && !w.equals("about")
                    && !w.equals("will") && !w.equals("after") && !w.equals("before")
                    && !w.equals("well") && !w.equals("have") && !w.equals("done") && !w.equals("been")
                    && !w.equals("instead") && !w.equals("just") && !w.equals("because") && !w.equals("very")
                    && !w.equals("really") & !w.equals("could") && !w.equals("should") && !w.equals("shall")
                    && !w.equals("there") && !w.equals("their") && !w.equals("here") && !w.equals("itself")
                    && !w.equals("herself") && !w.equals("yourself") && !w.equals("himself") && !w.equals("themselves")
                    && !w.equals("yourselves") && !w.equals("ourselves") && !w.equals("myself") && !w.equals("would")
                    && !w.equals("wouldn") && !w.equals("shouldn") && !w.equals("more") && !w.equals("much") && !w.equals("most")
                    && !w.equals("made") && !w.equals("must") && !w.equals("want") && !w.equals("what") && !w.equals("wish")
                    && !w.equals("your") && !w.equals("once") & !w.equals("each") && !w.equals("every") && !w.equals("also")
                    && !w.equals("everyone") && !w.equals("something") && !w.equals("anyone") && !w.equals("somebody") && !w.equals("anybody")) {
                totalFrequency += Summarizer.frequencyMap.get(words.get(i));
                total++;
            }
        }

        meanWordFrequency = totalFrequency / total;
    }

    /**
     * Calculates how many words in the sentence
     * match text's keywords from summarizer.Summarizer and divides it
     * by number of words in the sentence.
     */
    public void calculateSimilarityToKeywords() {

        double numOfSimilar = 0;
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < Summarizer.keywords.length; j++) {
                if (words.get(i).equals(Summarizer.keywords[j])) {
                    numOfSimilar++;
                }
            }
        }

        similarityToKeywords = numOfSimilar / (double)words.size();
    }

    /**
     * Words in sentence / num of words in longest sentence
     * @param longestSentenceLength longest sentence in the text
     */
    public void calcRelativeLength(double longestSentenceLength) {
        relativeLength = words.size() / longestSentenceLength;
    }

    /**
     * Sum of cohesion values
     */
    public void calcRawCohesionValue() {
        for (double d : similarityToOtherSentences) {
            rawCohesionValue += d;
        }
    }

    /**
     * Cohesion value of the sentence divided by largest cohesion
     * value in the document. Sentences with cohesion value
     * closer to 1 are the most cohesive
     * @param largestCV largest cohesion value, duh
     */
    public void calcCohesionValue(double largestCV) {
        cohesionValue = rawCohesionValue / largestCV;
    }


    /**
     * Checks if the first 6 words of the sentence are anaphors
     * (i.e. a word that refers to something said in the previous sentence)
     */
    public void checkForAnaphors() {

        int limit;
        if (words.size() < 6) {
            limit = 3;
        } else {
            limit = 6;
        }

        for (int i = 0; i < limit; i++) {
            String w = words.get(i);
            if (w.equals("it") || w.equals("she") || w.equals("her") || w.equals("him")
                    || w.equals("he") || w.equals("we") || w.equals("us") || w.equals("you") || w.equals("me")
                    || w.equals("this") || w.equals("those") || w.equals("they") || w.equals("former") || w.equals("latter")) {
                hasAnaphors = true;
                break;
            } else {
                hasAnaphors = false;
            }
        }
    }

    public void checkNEI() {
        int limit;
        if (words.size() < 5) {
            limit = 3;
        } else {
            limit = 5;
        }

        for (int i = 0; i < limit; i++) {
            String w = words.get(i);
            if (w.equals("additionally") || w.equals("furthermore") || w.equals("because") || w.equals("finally")
                    || w.equals("consequently") || w.equals("similarly") || w.equals("indeed") || w.equals("subsequently") || w.equals("however")
                    || w.equals("comparatively") || w.equals("likewise") || (i < limit - 1 && w.equals("in") && words.get(i + 1).equals("addition"))
                    || w.equals("nevertheless") || w.equals("nonetheless") || w.equals("moreover")) {
                hasNonEssentialInfo = true;
                break;
            } else {
                hasNonEssentialInfo = false;
            }
        }
    }
}
