/**
 * Created by Federica on 26/04/2018.
 */
public class Word {

    private String word;
    private int frequency;


    public Word(String word) {
        this.word = word;
        frequency = 0;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
