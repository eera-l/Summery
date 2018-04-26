import java.util.ArrayList;

/**
 * Created by Federica on 26/04/2018.
 */
public class Summarizer {

    private String beginningText;
    private ArrayList<Sentence> sentences = new ArrayList<>();

    public Summarizer(String text) {
        beginningText = text;
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    public void divideTextInSentences() {

        String sentence = "";
        int i = 0;

        while (i < beginningText.length()) {

            while ((beginningText.charAt(i) == '.' && ((i > 0 && beginningText.charAt(i - 1) == '.') || (i < beginningText.length() - 1 && beginningText.charAt(i + 1) == '.'))) || beginningText.charAt(i) != '.') {

                sentence += beginningText.charAt(i);

                i++;
            }
            sentence += beginningText.charAt(i);
            sentences.add(new Sentence(sentence));
            sentence = "";
            i += 2; //Skip space after period
        }
    }

    public void calcSentencePercPos() {

        double size = sentences.size();
        for (int i = 0; i < sentences.size(); i++) {
            double index = i + 1;
            sentences.get(i).calculatePercPos(index, size);
        }
    }
}
