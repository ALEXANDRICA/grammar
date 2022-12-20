package sk.aos;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;
import sk.aos.LALR1.ActionGotoTables;
import sk.aos.LALR1.LALR1item;
import sk.aos.LALR1.State;

public class ActionGotoTablesLALR1Test {

    HashSet<String> terminals1, terminals2;
    HashSet<String> nonterminals1, nonterminals2;
    Rule rule11, rule12, rule13, rule21, rule22, rule23, rule24, rule25, rule26, rule27;
    HashSet<Rule> rules1, rules2;
    String startsymbol1, startsymbol2;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

        terminals1 = new HashSet<>(Arrays.asList("$", "id"));
        nonterminals1 = new HashSet<>(Arrays.asList("S", "O", "P"));
        rule11 = new Rule(new ArrayList<>(Arrays.asList("S")), new ArrayList<>(Arrays.asList("id","O", "$")));
        rule12 = new Rule(new ArrayList<>(Arrays.asList("O")), new ArrayList<>(Arrays.asList("P", "P")));
        rule13 = new Rule(new ArrayList<>(Arrays.asList("P")), new ArrayList<>(Arrays.asList("id")));
        rules1= new HashSet<>(Arrays.asList(rule11, rule12, rule13));
        startsymbol1="S";

        terminals2 = new HashSet<>(Arrays.asList("a", "b", "c", "$"));
        nonterminals2 = new HashSet<>(Arrays.asList("S", "A", "B"));
        rule21 = new Rule(new ArrayList<>(Arrays.asList("S")), new ArrayList<>(Arrays.asList("B", "$")));
        rule22 = new Rule(new ArrayList<>(Arrays.asList("B")), new ArrayList<>(Arrays.asList("a", "B", "b")));
        rule23 = new Rule(new ArrayList<>(Arrays.asList("B")), new ArrayList<>(Arrays.asList("A")));
        rule24 = new Rule(new ArrayList<>(Arrays.asList("A")), new ArrayList<>(Arrays.asList("b", "A")));
        rule25 = new Rule(new ArrayList<>(Arrays.asList("A")), new ArrayList<>(Arrays.asList("c")));
        rules2= new HashSet<>(Arrays.asList(rule21, rule22, rule23, rule24, rule25));
        startsymbol2="S";
    }

    @Test
    public void test() {

        try {
            ContextFreeGrammar g = new ContextFreeGrammar(terminals2, nonterminals2, rules2, startsymbol2);
            ActionGotoTables test1 = new ActionGotoTables(g);

            for(State state : test1.getStates()) {
                System.out.println("State: " + state.getStateNumber());
                ArrayList<LALR1item> items = state.getLalr1Items();

                for (LALR1item item : items) {
                    System.out.print(item.getLALRrule().getLeftSide().toString());
                    System.out.print("=>");
                    System.out.print(item.getLALRrule().getRightSide().toString() + ", {");
                    System.out.print(item.getExpectedSymbols().toString() + "}");
                    System.out.println();

                }
                System.out.println(state.getReductions());
                System.out.println(state.getTransitions());
            }
            System.out.println();



        } catch (Exception e) {

            fail("Error in test.");
        }
    }

    @SuppressWarnings("static-access")
    @Test
    public void test1() {

        try {
            ContextFreeGrammar g = new ContextFreeGrammar(terminals1, nonterminals1, rules1, startsymbol1);
            ActionGotoTables test1 = new ActionGotoTables(g);

            for(State state : test1.getStates()) {
                System.out.println("State: " + state.getStateNumber());
                ArrayList<LALR1item> items = state.getLalr1Items();

                for (LALR1item item : items) {
                    System.out.print(item.getLALRrule().getLeftSide().toString());
                    System.out.print("=>");
                    System.out.print(item.getLALRrule().getRightSide().toString() + ", {");
                    System.out.print(item.getExpectedSymbols().toString() + "}");
                    System.out.println();

                }
                System.out.println(state.getReductions());
                System.out.println(state.getTransitions());
            }
            System.out.println();
        } catch (Exception e) {

            fail("Not yet implemented");
        }
    }

}
