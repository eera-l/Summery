
public class Main {

    public static void main(String[] args) {

        String text = "Love, oneness, is no separation between you and life. It is a progressive letting go, a progressive not fault finding. Just do nothing and love this moment. It's very beautiful and very deep.";
        Summarizer summarizer = new Summarizer(text);

        summarizer.divideTextInSentences();

        for (Sentence s : summarizer.getSentences()) {
            System.out.println(s.getText());
            System.out.println("Words: " + s.countWordsInSentence());
            System.out.println("--------------");
        }

        summarizer.calcSentencePercPos();

        for (Sentence s :summarizer.getSentences()) {
            System.out.println("Percentile position: " + s.getPercentilePos());
        }
    }
}
