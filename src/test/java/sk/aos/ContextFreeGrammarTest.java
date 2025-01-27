package sk.aos;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.fail;


public class ContextFreeGrammarTest {

    HashSet<String> terminals;
    HashSet<String> nonterminals;
    Rule rule1;
    Rule rule2;
    Rule rule3;
    HashSet<Rule> rules1;
    HashSet<Rule> rules2;
    String startsymbol;

    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @Before
    public void setUp() {
        terminals = new HashSet<>(Arrays.asList("a", "b")); // Set of terminals
        nonterminals = new HashSet<>(Arrays.asList("A", "B"));

        rule1 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(Arrays.asList("a", "A", "b")));
        rule2 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(Arrays.asList("b", "A")));
        // legitime rules, test1
        rule3 = new Rule(new ArrayList<>(Arrays.asList("A", "B")), new ArrayList<>(Arrays.asList("a", "B", "A", "a")));
        // rule3 has 2 symbols on left side, test2
        rules1 = new HashSet<>(Arrays.asList(rule1, rule2)); // test1
        rules2 = new HashSet<>(Arrays.asList(rule1, rule2, rule3)); // test2

        startsymbol = "A";
    }

    @Test
    public void test1() throws Exception {
        new ContextFreeGrammar(terminals, nonterminals, rules1, startsymbol);
    }

    @Test
    public void test2() {
        try {
            new ContextFreeGrammar(terminals, nonterminals, rules2, startsymbol);
            fail("Expected exception:Left side of rule has to be just one nonterminal");
        } catch (Exception e) {
            // test successful
            e.printStackTrace();
        }
    }

}
