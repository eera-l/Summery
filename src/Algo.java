
public class Algo implements Runnable {
    private String text;
    public Algo(String text){
        this.text=text;
    }

    public void run() {

        //text = "Love, oneness, is no separation between you and life. It is a progressive letting go, a progressive not fault finding. Just do nothing and love this moment. It's very beautiful and very deep.";
        Summarizer summarizer = new Summarizer(text);

        summarizer.divideTextInSentences();
        text = "";

        for (Sentence s : summarizer.getSentences()) {
            text+=s.getText()+"\n";
            text+= "Words: " + s.countWordsInSentence()+"\n";
            text+="--------------\n";


//            System.out.println(s.getText());
//            System.out.println("Words: " + s.countWordsInSentence());
//            System.out.println("--------------");
        }

        summarizer.calcSentencePercPos();

        for (Sentence s :summarizer.getSentences()) {
            text+="Percentile position: " + s.getPercentilePos()+"\n";
            //System.out.println("Percentile position: " + s.getPercentilePos());
        }
    }

    public String getText() {
        return text;
    }
}
