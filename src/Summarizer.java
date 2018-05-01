import java.util.*;

/**
 * Created by Federica on 26/04/2018.
 */
public class Summarizer {

    private String beginningText;
    private ArrayList<Sentence> sentences = new ArrayList<>();
    public static HashMap<String, Integer> frequencyMap = new HashMap<>();
    public static String[] keywords;

    public Summarizer(String text) {

        beginningText = text;
        keywords = new String[calculateNumOfKeywords()];
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
                if (w.length() >= 4 && !w.equals("that") && !w.equals("with") && !w.equals("over")
                        && !w.equals("they") && !w.equals("does") && !w.equals("when")
                        && !w.equals("goes") && !w.equals("where") && !w.equals("which")
                        && !w.equals("without") && !w.equals("some") && !w.equals("said")
                        && !w.equals("then") && !w.equals("from") && !w.equals("this") && !w.equals("went")) {
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
     * and saves the ones with the highest frequency
     */
    public void setKeywords() {

        Map<String, Integer> sortedMap = sortByValue(frequencyMap);

        Set<String> keys = sortedMap.keySet();
        String[] keysArray = keys.toArray(new String[keys.size()]);

        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = keysArray[i];
        }
    }

    /**
     * Sets num of keywords based on text length
     * @return num of keywords
     */
    private int calculateNumOfKeywords() {

        int num = 0;
        if (beginningText.length() > 0 && beginningText.length() <= 1000) {
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
}
