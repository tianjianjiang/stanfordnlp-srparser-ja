package com.yaraku.nlp.parser.shiftreduce.demo;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.shiftreduce.ShiftReduceParser;
import edu.stanford.nlp.trees.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.util.logging.RedwoodConfiguration;

/**
 * Demonstrates how to first use a Japanese tagger, then use the
 * ShiftReduceParser. Note that ShiftReduceParser will not work
 * on untagged text.
 *
 * @author Mike Tian-Jian Jiang
 */
public class JapaneseShiftReduceParserDemo {

    public static void main(String[] args) {
        String modelPath = "ja.beam.rightmost.model.ser.gz";

        for (int argIndex = 0; argIndex < args.length; ) {
            switch (args[argIndex]) {
                case "-model":
                    modelPath = args[argIndex + 1];
                    argIndex += 2;
                    break;
                default:
                    throw new RuntimeException("Unknown argument " + args[argIndex]);
            }
        }

        RedwoodConfiguration.empty().capture(System.err).apply();
        ShiftReduceParser model = ShiftReduceParser.loadModel(modelPath);
        RedwoodConfiguration.current().clear().apply();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String text = scanner.nextLine();
            try {
                List<TaggedWord> taggedWords = new ArrayList<>();
                String[] tokens = text.split(" ");
                for (String token: tokens) {
                    TaggedWord taggedWord = new TaggedWord();
                    taggedWord.setFromString(token);
                    taggedWords.add(taggedWord);
                }
                Tree tree = model.apply(taggedWords);
                System.out.println(tree);
            } catch (Exception e) {
                System.err.println(e.getMessage() + " with " + text);
                throw e;
            }
        }
    }
}
