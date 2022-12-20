package sk.aos.LALR1;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
public class ActionGotoTables {
    ArrayList<State> states = new ArrayList<>();
    private final ArrayList<LALR1item> newItems = new ArrayList<>();
    private final ContextFreeGrammar grammar;

    public ActionGotoTables(ContextFreeGrammar grammar) {                    // konstruktor triedy .. vstupnym parametrom je bezkontextova gramatika
        this.grammar = grammar;                                                // pre ktoré chceme vytvorit tabulky ACTION a GOTO
        State state = new State(grammar);
        states.add(state);
        LALRoneAutomaton();
        actionTable();
        gotoTable();

    }


    private void LALRoneAutomaton() {                     // operacia, ktora predstavuje LALR1 automat (vytvaranie novych stavov)
        try {
            for (int x = 0; x < states.size(); x++) {
                State s = states.get(x);
                ArrayList<LALR1item> items = s.getLalr1Items();

                for (String a : s.getTransitions()) {
                    for (LALR1item l : items) {
                        int i = l.getLALRrule().getRightSide().indexOf(".");
                        if (l.getLALRrule().getRightSide().indexOf(".") != l.getLALRrule().getRightSide().size() - 1 && Objects.equals(a, l.getLALRrule().getRightSide().get(i + 1))) {
                            LALR1item k = new LALR1item(l.getLALRrule(), l.getExpectedSymbols());
                            newItems.add(k);
                        }
                    }
                    State newState = new State(newItems, grammar);
                    s.nextStates.put(a, newState.stateNumber);
                    states.add(newState);
                    newItems.clear();
                }
            }
        } catch (Exception e) {
            System.out.println("Chyba tu.");
        }
    }


    private void actionTable() {                                            // operacia na zobrazenie tabulky ACTION  v konzole
        StringBuilder outPut = new StringBuilder(format("ACTION"));
        for (State s : states) {
            outPut.append(format("s" + s.stateNumber));
        }
        System.out.println(outPut);
        for (String t : grammar.getTerminals()) {
            outPut = new StringBuilder(format(t));
            for (State s : states) {
                if (s.getTransitions().contains(t)) {
                    outPut.append(format("P"));
                } else if (s.getReductions().contains(t)) {
                    int i = getRuleNumber(s);
                    outPut.append(format("R" + i));
                } else {
                    outPut.append(format(""));
                }
            }
            System.out.println(outPut);
        }
        outPut = new StringBuilder(format("epsilon"));
        for (State s : states) {
            if (s.getReductions().contains("epsilon")) {
                outPut.append(format("A"));
            } else {
                outPut.append(format(""));
            }
        }
        System.out.println(outPut);
        System.out.println();
    }


    private void gotoTable() {                                                // operacia na zobrazenie tabulky GOTO v konzole
        StringBuilder outPut = new StringBuilder(format("GOTO"));
        for (State s : states) {
            outPut.append(format("s" + s.stateNumber));
        }

        System.out.println(outPut);
        for (String t : grammar.getTerminals()) {
            outPut = new StringBuilder(format(t));
            for (State s : states) {
                if (s.nextStates.containsKey(t)) {
                    outPut.append(format("s" + s.nextStates.get(t)));
                } else {
                    outPut.append(format(""));
                }
            }
            System.out.println(outPut);
        }
        for (String t : grammar.getNonterminals()) {
            outPut = new StringBuilder(format(t));
            for (State s : states) {
                if (s.nextStates.containsKey(t)) {
                    outPut.append(format("s" + s.nextStates.get(t)));
                } else {
                    outPut.append(format(""));
                }
            }
            System.out.println(outPut);
        }


    }


    private String format(String str) {
        return String.format("%1$" + 7 + "s", str);
    }


    private int getRuleNumber(State s) {                                    // operacia na ziskanie cisla pravidla, aby bolo mozne zobrazit v tabulke ACTION
        ArrayList<String> rightSide = new ArrayList<>();                    // podla ktoreho pravidla bola vykonana redukcia
        for (LALR1item i : s.getLalr1Items()) {
            if (i.getLALRrule().getRightSide().indexOf(".") == i.getLALRrule().getRightSide().size() - 1) {
                rightSide.addAll(i.getLALRrule().getRightSide());
                rightSide.remove(".");
            }
        }
        int i = 0;
        for (Rule r : grammar.getRules()) {
            if (r.getRightSide().equals(rightSide)) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
