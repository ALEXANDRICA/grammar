package sk.aos.LALR1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import sk.aos.FirstandFollow.FirstAndFollowClass;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

public class State {
    private static int counter = 0;                             // pocitadlo na pocet stavov
    int stateNumber;                                            // atribut pre cislo stavu
    HashMap<String, Integer> nextStates;                        // atribut pre nasledujuci stav z aktualneho
    ArrayList<LALR1item> lalr1Items;                            // atribut pre zapamatanie si LALR1 polozky
    ContextFreeGrammar grammar;

    public State(ContextFreeGrammar grammar) {                  // konstruktor pre prvy stav, vstupom je bezkontextova G
        this.grammar = grammar;
        this.lalr1Items = new ArrayList<>();
        this.nextStates = new HashMap<>();
        HashSet<String> expectedSymbols = new HashSet<>();
        expectedSymbols.add("epsilon");
        LALR1item lalr1Item = new LALR1item(grammar.getStartrule(), expectedSymbols);
        lalr1Items.add(lalr1Item);
    }

    public State(ArrayList<LALR1item> items, ContextFreeGrammar grammar) {         // konstruktor pre presun LALR1 poloziek z predosleho stavu
        this.grammar = grammar;                                                    // vstupom su LALR1 pravidla z predosleho stavu a bezkontexotva G
        this.lalr1Items = new ArrayList<>();
        this.lalr1Items.addAll(items);
        this.nextStates = new HashMap<>();
    }
}