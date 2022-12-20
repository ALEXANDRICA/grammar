package sk.aos.LALR1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import sk.aos.FirstandFollow.FirstAndFollowClass;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

@Getter
@Setter
public class State {
    private static int counter = 0;                             // pocitadlo na pocet stavov
    int stateNumber;                                            // atribut pre cislo stavu
    HashMap<String, Integer> nextStates;                        // atribut pre nasledujuci stav z aktualneho
    ArrayList<LALR1item> lalr1Items;                            // atribut pre zapamatanie si LALR1 polozky
    HashSet<String> transitions;							    // atribut pre ulozenie symbolov cez ktore robime presun
    HashSet<String> reductions;								    // atribut pre ulozenie symbolov pri ktorych robime redukciu
    ContextFreeGrammar grammar;


    public State(ContextFreeGrammar grammar) {                  // konstruktor pre prvy stav, vstupom je bezkontextova G
        this.grammar = grammar;
        this.lalr1Items = new ArrayList<>();
        this.transitions = new HashSet<>();
        this.reductions = new HashSet<>();
        this.nextStates = new HashMap<>();
        HashSet<String> expectedSymbols = new HashSet<>();
        expectedSymbols.add("epsilon");
        LALR1item lalr1Item = new LALR1item(grammar.getStartrule(), expectedSymbols);
        lalr1Items.add(lalr1Item);

        try {
            closure();
        } catch (Exception e) {
            System.out.println("Chyba closure 1.stav.");
        }
        try {
            transitionsAndReductions();
        } catch (Exception e) {
            System.out.println("Chyba transit&reduct 1.stav.");
        }
        this.stateNumber = counter;
        counter++;
    }


    public State(ArrayList<LALR1item> items, ContextFreeGrammar grammar) {         // konstruktor pre presun LALR1 poloziek z predosleho stavu
        this.grammar = grammar;                                                    // vstupom su LALR1 pravidla z predosleho stavu a bezkontexotva G
        this.lalr1Items = new ArrayList<>();
        this.lalr1Items.addAll(items);
        this.reductions = new HashSet<>();
        this.nextStates = new HashMap<>();

        try {
            closure();
        } catch (Exception e) {
            System.out.println("Chyba closure stavy.");
        }
        try {
            transitionsAndReductions();
        } catch (Exception e) {
            System.out.println("Chyba transit&reduct stavy.");
        }
        this.stateNumber = counter;
        counter++;
    }


    @SuppressWarnings("static-access")
    private void closure() throws Exception {									// uzaverova operacia
        FirstAndFollowClass first = new FirstAndFollowClass();
        for (int i = 0; i< lalr1Items.size(); i++) {
            Rule rule = lalr1Items.get(i).LALRrule;
            for(int j = 0; j < rule.getRightSide().size(); j++) {
                if(Objects.equals(rule.getRightSide().get(j), ".")) {
                    if (grammar.getNonterminals().contains(rule.getRightSide().get(j+1))) {

                        for(Rule r : grammar.getRules()) {
                            if(Objects.equals(r.getLeftSide().get(0), rule.getRightSide().get(j + 1))) {

                                try {
                                    String symbol = rule.getRightSide().get(j+2);
                                    LALR1item item = new LALR1item(r, first.first(grammar, symbol));
                                    lalr1Items.add(item);
                                    continue;
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                LALR1item item = new LALR1item(r, lalr1Items.get(i).getExpectedSymbols());
                                lalr1Items.add(item);
                            }
                        }

                    }

                }
            }
        }
    }


    private void transitionsAndReductions() {						// Podmienka pre pridanie symbolov do transitions resp reduction

        for (LALR1item item : lalr1Items) {
            ArrayList<String> rside = item.getLALRrule().getRightSide();

            int i = rside.lastIndexOf(".");
            if ((rside.indexOf(".") != rside.size() - 1)) {  //
                this.transitions.add(rside.get(i + 1));
            } else {
                this.reductions.addAll(item.getExpectedSymbols());
            }
        }
    }
}