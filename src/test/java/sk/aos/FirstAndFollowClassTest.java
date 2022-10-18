package sk.aos;

import org.junit.Before;
import org.junit.Test;
import sk.aos.FirstandFollow.FirstAndFollowClass;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class FirstAndFollowClassTest {

    HashSet<String> terminals;
    HashSet<String> nonterminals;
    Rule rule1;
    Rule rule2;
    Rule rule3;
    Rule rule4;
    Rule rule5;
    Rule rule6;
    HashSet<Rule> rules;
    String startsymbol;

    @Before
    public void setUp() {
        terminals = new HashSet<>(Arrays.asList("a", "b", "(", ")", "+", "e")); // Set of terminals
        nonterminals = new HashSet<>(Arrays.asList("S", "A", "B"));

        rule1 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("A", "(", "S", ")")));
        rule2 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("a", "B")));
        rule3 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(List.of("b")));
        rule4 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(List.of("e")));
        rule5 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(Arrays.asList("+", "S")));
        rule6 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(List.of("e")));

        rules = new HashSet<>(Arrays.asList(rule1, rule2, rule3, rule4, rule5, rule6));

        startsymbol = "S";
    }

    @Test
    public void testFirst() throws Exception {
        ContextFreeGrammar g = new ContextFreeGrammar(terminals, nonterminals, rules, startsymbol);

        System.out.println("FIRST S:" + FirstAndFollowClass.first(g, "S"));
        System.out.println("FIRST A:" + FirstAndFollowClass.first(g, "A"));
        System.out.println("FIRST B:" + FirstAndFollowClass.first(g, "B"));
        System.out.println("FIRST a:" + FirstAndFollowClass.first(g, "a"));
        System.out.println("FIRST b:" + FirstAndFollowClass.first(g, "b"));
        System.out.println("FIRST (:" + FirstAndFollowClass.first(g, "("));
        System.out.println("FIRST ):" + FirstAndFollowClass.first(g, ")"));
        System.out.println("FIRST +:" + FirstAndFollowClass.first(g, "+"));

        System.out.println("FOLLOW S:" + FirstAndFollowClass.Follow(g, "S"));
        System.out.println("FOLLOW A:" + FirstAndFollowClass.Follow(g, "A"));
        System.out.println("FOLLOW B:" + FirstAndFollowClass.Follow(g, "B"));
    }

}
