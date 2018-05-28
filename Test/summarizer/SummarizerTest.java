package summarizer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SummarizerTest {

    @Test
    public void getSentences() {
        System.out.println("getSentences");
        String text = "This is sentence one. This is sentence two. This is unrelated string of words.";
        Summarizer summarizer = new Summarizer(text);
        summarizer.divideTextInSentences();
        assertNotNull(summarizer.getSentences());
    }

    @Test
    public void divideTextInSentences() {
        System.out.println("divideTextInSentences");
        String text = "This is sentence one. This is sentence two. This is unrelated string of words.";
        Summarizer summarizer = new Summarizer(text);
        summarizer.divideTextInSentences();
        ArrayList<Sentence> sentences = summarizer.getSentences();
        assertEquals(sentences.get(0).getText(),"This is sentence one.");
        assertEquals(sentences.get(1).getText(),"This is sentence two.");
        assertEquals(sentences.get(2).getText(),"This is unrelated string of words.");
    }

    @Test
    public void calcSentencePercPos() {
        System.out.println("calcSentencePercPos");
        String text = "This is sentence one. This is sentence two. This is unrelated string of words. This is forth sentence.";
        Summarizer summarizer = new Summarizer(text);
        summarizer.divideTextInSentences();
        summarizer.calcSentencePercPos();
        double pos = summarizer.getSentences().get(0).getPercentilePos();
        assertEquals(pos, 0.25,0);
    }
}