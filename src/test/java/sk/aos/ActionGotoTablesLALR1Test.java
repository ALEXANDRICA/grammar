package sk.aos;

import org.junit.Test;
import sk.aos.LALR1.ActionGotoTables;
import sk.aos.LALR1.LALR1item;
import sk.aos.LALR1.State;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.List;
import java.util.Set;

public class ActionGotoTablesLALR1Test {

    @Test
    public void test() throws Exception {
        Set<String> terminals = Set.of("$", "id");
        Set<String> nonterminals = Set.of("S", "O", "P");
        Set<Rule> rules = Set.of(
                new Rule(List.of("S"), List.of("id", "O", "$")),
                new Rule(List.of("O"), List.of("P", "P")),
                new Rule(List.of("P"), List.of("id")));
        String startSymbol = "S";

        ContextFreeGrammar g = new ContextFreeGrammar(terminals, nonterminals, rules, startSymbol);
        ActionGotoTables test1 = new ActionGotoTables(g);

        for (State state : test1.getStates()) {
            System.out.println("State: " + state.getStateNumber());
            List<LALR1item> items = state.getLalr1Items();

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
    }

    @Test
    public void test1() throws Exception {
        Set<String> terminals = Set.of("$", "0", "1", "a", "b");
        Set<String> nonterminals = Set.of("S", "E", "A", "B");
        Set<Rule> rules = Set.of(
                new Rule(List.of("S"), List.of("E", "$")),
                new Rule(List.of("E"), List.of("A", "0")),
                new Rule(List.of("E"), List.of("b", "A", "1")),
                new Rule(List.of("E"), List.of("B", "1")),
                new Rule(List.of("E"), List.of("b", "B", "0")),
                new Rule(List.of("A"), List.of("a")),
                new Rule(List.of("B"), List.of("a")));
        String startSymbol = "S";

        ContextFreeGrammar g = new ContextFreeGrammar(terminals, nonterminals, rules, startSymbol);
        ActionGotoTables test1 = new ActionGotoTables(g);

        for (State state : test1.getStates()) {
            System.out.println("State: " + state.getStateNumber());
            List<LALR1item> items = state.getLalr1Items();

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
    }

}
