package info.kaljuvee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Parentheses checker
 *
 * @author Oliver Kaljuvee
 */
public class ParenthesesChecker {
    private final static Logger log = Logger.getLogger(ParenthesesChecker.class.getName());
    private static final char OPEN = '(';
    private static final char CLOSE = ')';
    private String input;

    /**
     * Constructor
     *
     * @param input Input string which will be evaluated for the parentheses balance.
     */
    public ParenthesesChecker(String input) {
        if(input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        this.input = input;
    }

    /**
     * Empty string or string with no parentheses is considered to be balanced.
     * @return
     */
    public boolean isBalanced() {
        log.info("Checking balanced parentheses on: " + input);
        Stack<Character> stack = new Stack<>();

        for(Character c : input.toCharArray()) {
            if(c == CLOSE && (stack.isEmpty() || !stack.pop().equals(OPEN))) {
                return false;
            }
            if(c == OPEN) {
                stack.push(c);
            }
        }
        return stack.empty();
    }

    private static final String RESOURCE_DIR = "src/main/resources/";

    /**
     * Main method of the parentheses checker and the arguments that are supported:
     * > java ParenthesesChecker ((foo)bar)
     * > java ParenthesesChecker string ((foo)bar)
     * > java ParenthesesChecker file C:/tmp/long-balanced.lisp
     * > java ParenthesesChecker file long-balanced.lisp
     *
     * @param args Program arguments.
     */
    public static void main(String... args) {
        String input = getInputString(args);

        if(input == null) {
            log.info("Unable to construct input string. Exiting.");
            System.exit(-1);
        }
        boolean balanced = new ParenthesesChecker(input).isBalanced();
        log.info("Input string is " + (balanced ? "" : "not ") + "balanced");
        log.info("Complete");
    }

    /**
     * Creates the input string for the checker based on the program arguments provided (see main method above).
     *
     * @param args Program arguments
     * @return Input string for which the parentheses balance will be checked on
     */
    private static String getInputString(String... args) {
        String input = null;

        if(args.length == 1) {
            input = args[0];
        } else if(args.length == 2) {
            if(args[0].equals("string")) {
                input = args[1];
            } else if(args[0].equals("file")) {
                String fileName = args[1];

                if(!fileName.contains("/")) {
                    fileName = RESOURCE_DIR + fileName;
                }
                StringBuilder sb = new StringBuilder();

                try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                    stream.forEach(s -> sb.append(s.trim()));
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Unable to read file: " + fileName);
                    e.printStackTrace();
                }
                input = sb.toString();
            }
        }
        return input;
    }
}
