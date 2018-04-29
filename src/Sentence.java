import java.util.ArrayList;

/**
 * Created by Federica on 26/04/2018.
 */
public class Sentence {

    private String text;
    private int wordsInText;
    private ArrayList<String> words;
    private double deltaX;
    private double deltaY;
    private double magnitude;
    private double percentilePos;
    //percentile position of the sentence in the document, calculated by
    //position number of the sentence in the document / total num of sentences in the document
    //page 10 of KEA: Practical Automatic Keyphrase Extraction

    private double meanWordFrequency; //average based on how frequent the words
    //within the sentence are compared to the words in all other sentences

    private double similarityToTitle; //average of how many words in the sentence
    //match the words in the title


    public Sentence(String text) {
        this.text = text;
        words = new ArrayList<>();
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
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

    public void setDeltaX(double x) {
        deltaX = x;
    }

    public void setDeltaY(double y) {
        deltaY = y;
    }

    public void setSimilarityToTitle(double similarityToTitle) {
        this.similarityToTitle = similarityToTitle;
    }

    public double getMagnitude() {
        return calculateMagnitude();
    }

    private double calculateMagnitude() {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
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
            if (words.get(i).length() >= 4) {
                totalFrequency += Summarizer.frequencyMap.get(words.get(i));
                total++;
            }
        }

        meanWordFrequency = totalFrequency / total;
    }
}
