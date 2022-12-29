package sk.aos.LALR1;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class ActionGotoTables {

    List<State> states = new ArrayList<>();
    private final List<LALR1item> newItems = new ArrayList<>();
    private final ContextFreeGrammar grammar;

    public ActionGotoTables(ContextFreeGrammar grammar) {                    // konstruktor triedy .. vstupnym parametrom je bezkontextova gramatika
        this.grammar = grammar;                                              // pre ktoré chceme vytvorit tabulky ACTION a GOTO
        State state = new State(grammar);
        states.add(state);
        LALRoneAutomaton();
        joiningStates();
        actionTable();
        gotoTable();
    }


    private void LALRoneAutomaton() {                     // operacia, ktora predstavuje LALR1 automat (vytvaranie novych stavov)
        try {
            for (int i = 0; i < states.size(); i++) {
                State stav = states.get(i);
                List<LALR1item> riadky = stav.getLalr1Items();

                for (String transition : stav.getTransitions()) {

                    for (LALR1item riadok : riadky) {
                        List<String> rightSide = riadok.getLALRrule().getRightSide();
                        int idxBodky = rightSide.indexOf(".");

                        if (idxBodky != rightSide.size() - 1 && Objects.equals(transition, rightSide.get(idxBodky + 1))) {
                            // ak bodka nie je posledna & znak posunu je za bodkou
                            // prida do newItems novy LALR1 s posunutou bodkou
                            LALR1item item = new LALR1item(riadok.getLALRrule(), riadok.getExpectedSymbols());
                            newItems.add(item);
                        }
                    }

                    State newState = new State(newItems, grammar); // robi sa aj closure, transition and reduction
                    stav.nextStates.put(transition, newState.stateNumber);
                    newState.previousStates.add(stav.stateNumber); //predosly stav
                    newState.setPreviousTransition(transition);  //symbol presunu
                    states.add(newState);
                    newItems.clear();
                }
            }
        } catch (Exception e) {
            System.out.println("Chyba tu.");
            throw e;
        }
    }

    private void joiningStates() {
        for (int i = 0; i < states.size(); i++) {
            State hlavny = states.get(i);
            List<LALR1item> riadkyH = hlavny.getLalr1Items();
            for (int j = i + 1; j < states.size(); j++) {
                State vedlajsi = states.get(j);
                List<LALR1item> riadkyV = vedlajsi.getLalr1Items();
                if (riadkyH.size() == riadkyV.size() && riadkyH.containsAll(riadkyV)) {
                    for (State stav : states) {
                        if (stav.stateNumber == vedlajsi.previousStates.get(0)) {
                            stav.nextStates.put(vedlajsi.previousTransition, hlavny.stateNumber);
                        for (LALR1item riadokH : riadkyH) {
                            Set<String> symbolH = riadokH.getExpectedSymbols();
                            for (LALR1item riadokV : riadkyV) {
                                Set<String> symbolV = riadokV.getExpectedSymbols();
                                //if (symbolH != symbolV)
                                symbolH.add(symbolV.toString());
                            }
                        }

                        }
                    }
                }
            }
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
