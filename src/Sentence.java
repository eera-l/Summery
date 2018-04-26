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

    public void setDeltaX(double x) {
        deltaX = x;
    }

    public void setDeltaY(double y) {
        deltaY = y;
    }

    public double getMagnitude() {
        return calculateMagnitude();
    }

    private double calculateMagnitude() {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

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

    public void extractWords() {

        String word = eliminatePunctuation();
        String singleWord = "";

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != ' ') {
                singleWord += word.charAt(i);
            } else {
                words.add(singleWord);
                singleWord = "";
            }

            if (i == word.length() - 1) {
                words.add(singleWord);
            }
        }
    }
}