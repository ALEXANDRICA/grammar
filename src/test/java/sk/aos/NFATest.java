package sk.aos;

import org.junit.Before;
import org.junit.Test;
import sk.aos.automata.NondeterministicFinAutomaton;
import sk.aos.grammar.Grammar;
import sk.aos.grammar.RegularGrammar;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class NFATest {

    HashSet<String> terminals;
    HashSet<String> nonterminals;

    HashSet<String> terminals2;
    HashSet<String> nonterminals2;

    HashSet<String> terminals3;
    HashSet<String> nonterminals3;

    Rule rule1;
    Rule rule2;
    Rule rule3;
    Rule rule4;
    Rule rule5;
    Rule rule6;

    Rule rule7;
    Rule rule8;
    Rule rule9;
    Rule rule10;
    Rule rule11;
    Rule rule12;
    Rule rule13;
    Rule rule14;
    Rule rule15;
    Rule rule16;
    Rule rule17;
    Rule rule18;

    Rule rule19;
    Rule rule20;
    Rule rule21;
    Rule rule22;


    HashSet<Rule> rules1;
    HashSet<Rule> rules2;
    HashSet<Rule> rules3;

    String startsymbol, startsymbol2, startsymbol3;

    Grammar g, g2, g3;

    @Before
    public void setUp() throws Exception {
        terminals = new HashSet<>(Arrays.asList("e", "l", "s", "n", "d")); // Set of terminals
        nonterminals = new HashSet<>(Arrays.asList("S", "A", "B", "C", "D"));

        rule1 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("e", "A")));
        rule2 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(Arrays.asList("l", "B")));
        rule3 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(Arrays.asList("n", "C")));
        rule4 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(Arrays.asList("s", "D")));
        rule5 = new Rule(new ArrayList<>(List.of("C")), new ArrayList<>(List.of("d")));
        rule6 = new Rule(new ArrayList<>(List.of("D")), new ArrayList<>(List.of("e")));

        rules1 = new HashSet<>(Arrays.asList(rule1, rule2, rule3, rule4, rule5, rule6));

        startsymbol = "A";

        g = new RegularGrammar(terminals, nonterminals, rules1, startsymbol);

        terminals2 = new HashSet<>(Arrays.asList("+", "-", "0", "1")); // Set of terminals
        nonterminals2 = new HashSet<>(Arrays.asList("S", "A", "B"));

        rule7 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("+", "A")));
        rule8 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("-", "A")));
        rule9 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("1", "B")));
        rule10 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(List.of("0")));
        rule11 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(List.of("1")));
        rule12 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(Arrays.asList("1", "B")));
        rule13 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(List.of("0")));
        rule14 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(List.of("1")));
        rule15 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(Arrays.asList("0", "B")));
        rule16 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(Arrays.asList("1", "B")));
        rule17 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(List.of("0")));
        rule18 = new Rule(new ArrayList<>(List.of("B")), new ArrayList<>(List.of("1")));

        rules2 = new HashSet<>(Arrays.asList(rule7, rule8, rule9, rule10, rule11, rule12, rule13, rule14, rule15, rule16, rule17, rule18));

        startsymbol2 = "S";

        g2 = new RegularGrammar(terminals2, nonterminals2, rules2, startsymbol2);

        terminals3 = new HashSet<>(Arrays.asList("a", "b", "c")); // Set of terminals
        nonterminals3 = new HashSet<>(Arrays.asList("S", "A"));

        rule19 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("a", "S")));
        rule20 = new Rule(new ArrayList<>(List.of("S")), new ArrayList<>(Arrays.asList("b", "A")));
        rule21 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(List.of("epsilon")));
        rule22 = new Rule(new ArrayList<>(List.of("A")), new ArrayList<>(Arrays.asList("c", "A")));

        rules3 = new HashSet<>(Arrays.asList(rule19, rule20, rule21, rule22));

        startsymbol3 = "S";

        g3 = new RegularGrammar(terminals3, nonterminals3, rules3, startsymbol3);
    }

    @Test
    public void test() throws Exception {
        NondeterministicFinAutomaton n = new NondeterministicFinAutomaton((RegularGrammar) g);
        System.out.println("Transition function of n");
        n.getTransitionFunction().showTable();

        NondeterministicFinAutomaton n2 = new NondeterministicFinAutomaton((RegularGrammar) g2);
        System.out.println("Transition function of n2");
        n2.getTransitionFunction().showTable();
    }

    @Test
    public void test2() throws Exception {
        // test of adding epsilon to inputSymbols
        NondeterministicFinAutomaton n3 = new NondeterministicFinAutomaton((RegularGrammar) g3);
        System.out.println("Transition function of n3");
        n3.getTransitionFunction().showTable();
    }

}
