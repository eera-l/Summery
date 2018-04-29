import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Federica on 26/04/2018.
 */
public class Summarizer {

    private String beginningText;
    private ArrayList<Sentence> sentences = new ArrayList<>();
    public static HashMap<String, Integer> frequencyMap = new HashMap<>();

    public Summarizer(String text) {
        beginningText = text;
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Divides text into sentences.
     * Sentences are separated by either a period or
     * an exclamation point or a question mark.
     * Three dots do not count as sentence separators.
     */
    public void divideTextInSentences() {

        String sentence = "";
        int i = 0;

        while (i < beginningText.length()) {

            while ((beginningText.charAt(i) == '.' && ((i > 0 && beginningText.charAt(i - 1) == '.') || (i < beginningText.length() - 1 && beginningText.charAt(i + 1) == '.'))) || (beginningText.charAt(i) != '.' && beginningText.charAt(i) != '!' && beginningText.charAt(i) != '?')) {

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
                if (w.length() >= 4) {
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
}
