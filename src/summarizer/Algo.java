package summarizer;/*String text = "Once upon a time, there was a little girl who lived in a village near the forest.  Whenever she went out, the little girl wore a red riding cloak, so everyone in the village called her Little Red Riding Hood.\n" +
                "One morning, Little Red Riding Hood asked her mother if she could go to visit her grandmother as it had been awhile since they'd seen each other.\n" +
                "\"That's a good idea,\" her mother said.  So they packed a nice basket for Little Red Riding Hood to take to her grandmother.\n" +
                "When the basket was ready, the little girl put on her red cloak and kissed her mother goodbye.\n" +
                "\"Remember, go straight to Grandma's house,\" her mother cautioned.  \"Don't dawdle along the way and please don't talk to strangers!  The woods are dangerous.\"\n" +
                "\"Don't worry, mommy,\" said Little Red Riding Hood, \"I'll be careful.\"\n" +
                "But when Little Red Riding Hood noticed some lovely flowers in the woods, she forgot her promise to her mother.  She picked a few, watched the butterflies flit about for awhile, listened to the frogs croaking and then picked a few more. \n" +
                "Little Red Riding Hood was enjoying the warm summer day so much, that she didn't notice a dark shadow approaching out of the forest behind her...\n" +
                "Suddenly, the wolf appeared beside her.\n" +
                "\"What are you doing out here, little girl?\" the wolf asked in a voice as friendly as he could muster.\n" +
                "\"I'm on my way to see my Grandma who lives through the forest, near the brook,\"  Little Red Riding Hood replied.\n" +
                "Then she realized how late she was and quickly excused herself, rushing down the path to her Grandma's house. \n" +
                "The wolf, in the meantime, took a shortcut...\n" +
                "The wolf, a little out of breath from running, arrived at Grandma's and knocked lightly at the door.\n" +
                "\"Oh thank goodness dear!  Come in, come in!  I was worried sick that something had happened to you in the forest,\" said Grandma thinking that the knock was her granddaughter.\n" +
                "The wolf let himself in.  Poor Granny did not have time to say another word, before the wolf gobbled her up!\n" +
                "The wolf let out a satisfied burp, and then poked through Granny's wardrobe to find a nightgown that he liked.  He added a frilly sleeping cap, and for good measure, dabbed some of Granny's perfume behind his pointy ears.\n" +
                "A few minutes later, Red Riding Hood knocked on the door.  The wolf jumped into bed and pulled the covers over his nose.  \"Who is it?\" he called in a cackly voice.\n" +
                "\"It's me, Little Red Riding Hood.\"\n" +
                "\"Oh how lovely!  Do come in, my dear,\" croaked the wolf.\n" +
                "When Little Red Riding Hood entered the little cottage, she could scarcely recognize her Grandmother.\n" +
                "\"Grandmother!  Your voice sounds so odd.  Is something the matter?\" she asked.\n" +
                "\"Oh, I just have touch of a cold,\" squeaked the wolf adding a cough at the end to prove the point.\n" +
                "\"But Grandmother!  What big ears you have,\" said Little Red Riding Hood as she edged closer to the bed.\n" +
                "\"The better to hear you with, my dear,\" replied the wolf.\n" +
                "\"But Grandmother!  What big eyes you have,\" said Little Red Riding Hood.\n" +
                "\"The better to see you with, my dear,\" replied the wolf.\n" +
                "\"But Grandmother!  What big teeth you have,\" said Little Red Riding Hood her voice quivering slightly.\n" +
                "\"The better to eat you with, my dear,\" roared the wolf and he leapt out of the bed and began to chase the little girl.\n" +
                "Almost too late, Little Red Riding Hood realized that the person in the bed was not her Grandmother, but a hungry wolf.\n" +
                "She ran across the room and through the door, shouting, \"Help!  Wolf!\" as loudly as she could.\n" +
                "A woodsman who was chopping logs nearby heard her cry and ran towards the cottage as fast as he could.\n" +
                "He grabbed the wolf and made him spit out the poor Grandmother who was a bit frazzled by the whole experience, but still in one piece.\"Oh Grandma, I was so scared!\"  sobbed Little Red Riding Hood, \"I'll never speak to strangers or dawdle in the forest again.\"\n" +
                "\"There, there, child.  You've learned an important lesson.  Thank goodness you shouted loud enough for this kind woodsman to hear you!\"\n" +
                "The woodsman knocked out the wolf and carried him deep into the forest where he wouldn't bother people any longer.\n" +
                "Little Red Riding Hood and her Grandmother had a nice lunch and a long chat.";*/

public class Algo {

    private String text;
    public Algo(){
    }

    public static String run(String text) {

        Summarizer summarizer = new Summarizer(text);

        summarizer.divideTextInSentences();
        text = "";

        for (Sentence s : summarizer.getSentences()) {
            text += s.getText()+"<br />";
            text += "Words: " + s.countWordsInSentence()+"<br />";
            text +="--------------<br />";
            s.extractWords();
        }

        summarizer.calcSentencePercPos();
        summarizer.calculateWordFrequency();
        summarizer.calculateSimilarityToTitle();
        summarizer.setKeywords();
        summarizer.checkDoubleKeywords();
        summarizer.calcSentencesRelativeLength();
        summarizer.calcSimilarityToOtherSentences();
        summarizer.calcCohesionValue();
        //summarizer.findNouns();
        //summarizer.findMostFrequentNouns();
        //summarizer.setMainConceptIndicator();

        for (Sentence s : summarizer.getSentences()) {
            text += String.format("Percentile position: %.2f", s.getPercentilePos());

            s.calculateMeanFrequency();
            s.calculateSimilarityToKeywords();
            text += String.format(" Average frequency: %.2f", s.getMeanWordFrequency());
            text += String.format(" Similarity to title: %.2f", s.getSimilarityToTitle());
            text += String.format(" Similarity to keywords: %.2f", s.getSimilarityToKeywords());
            text += String.format(" Relative length: %.2f", s.getRelativeLength());
            text += String.format(" Cohesion value: %.2f%n", s.getCohesionValue());
            //text += String.format(" Main concept indicator: %b%n", s.getMainConcept());
            text += "------------------------------------<hr />";
        }
        for (String s : Summarizer.keywords) {
            text += "Keyword: " + s + "<br />";
        }

        for (String s : summarizer.doubleKeywords) {
            text += "Double keyword: " + s + "<br />";
        }

        return text;
    }

    public String getText() {
        return text;
    }
}
