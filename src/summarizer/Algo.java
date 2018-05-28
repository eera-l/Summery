package summarizer;

public class Algo {

    public Algo(){
    }

    public static String run(String text, String heading) {
        Summarizer summarizer = new Summarizer(text);
        summarizer.divideTextInSentences();
        text = "";

        for (Sentence s : summarizer.getSentences()) {

            s.countWordsInSentence();
            s.extractWords();
        }

        summarizer.calcSentencePercPos();
        summarizer.calculateWordFrequency();
        summarizer.calculateSimilarityToTitle(heading);
        summarizer.setKeywords();
        summarizer.checkDoubleKeywords();
        summarizer.calcSentencesRelativeLength();
        summarizer.calcSimilarityToOtherSentences();
        summarizer.calcCohesionValue();
        //summarizer.findNouns();
        //summarizer.findMostFrequentNouns();
        //summarizer.setMainConceptIndicator();

        for (Sentence s : summarizer.getSentences()) {
            s.calculateMeanFrequency();
            s.calculateSimilarityToKeywords();
            s.extractProperNames();
            s.checkForAnaphors();
            s.checkNEI();
        }
        summarizer.chooseSentences();

        text += "Sentences in source text: " + summarizer.getSentences().size() + "<br />";
        text += "Sentences in summary: " + (summarizer.finalSentences.size()-1) + "<hr />";
        text += "Summary before training: " + "<br />";

        for (Sentence s : summarizer.finalSentences) {
            text += s.getText() + "<br />";
        }
        text += "<hr />";

//        for (int i = 0; i < 100; i++)
//            summarizer.train();

        summarizer.getAISentences();
        summarizer.saveFilter();

        //summarizer.displayForBetterUnderstanding();

        text += "Summary after training: " + "<br />";

        for (Sentence s : summarizer.finalSentencesAI) {
            text += s.getText() + "<br />";
        }

        /*for (String s : Summarizer.keywords) {
            text += "Keyword: " + s + "<br />";
        }

        for (String s : summarizer.doubleKeywords) {
            text += "Double keyword: " + s + "<br />";
        }*/

        return text;
    }

}
