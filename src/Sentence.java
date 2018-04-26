/**
 * Created by Federica on 26/04/2018.
 */
public class Sentence {

    private String text;
    private int wordsInText;
    private double deltaX;
    private double deltaY;
    private double magnitude;

    public Sentence(String text) {
        this.text = text;
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

    public void setDeltaX(double x) {
        deltaX = x;
    }

    public void setDeltaY(double y) {
        deltaY = y;
    }

    public double getMagnitude() {
        return calculateMsgnitude();
    }

    private double calculateMsgnitude() {
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
}
