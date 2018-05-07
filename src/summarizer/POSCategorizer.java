package summarizer;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Federica on 06/05/2018.
 */
public class POSCategorizer implements Runnable {

    private static ArrayList<String> nouns = new ArrayList<>();
    private String sentence;

    public POSCategorizer(String sentence) {
        this.sentence = sentence;
    }

    public static ArrayList<String> getNouns() {
        return nouns;
    }

    @Override
    public void run() {
        extractNouns();
    }

    private void extractNouns() {

        try (FileInputStream fileInputStream = new FileInputStream("src/lib/en-parser-chunking.bin")) {

            ParserModel model = new ParserModel(fileInputStream);

            Parser parser = ParserFactory.create(model);
            Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

            for (Parse p : topParses) {
                getNouns(p);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private void getNouns(Parse p) {

        if (p.getType().equalsIgnoreCase("NN")) {
            nouns.add(p.getCoveredText());
        }

        for (Parse child : p.getChildren()) {
            getNouns(child);
        }
    }
}
