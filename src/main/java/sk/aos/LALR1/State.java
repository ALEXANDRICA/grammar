package sk.aos.LALR1;

import lombok.Getter;
import lombok.Setter;
import sk.aos.FirstandFollow.FirstAndFollowClass;
import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.*;

@Getter
@Setter
public class State {

    private static int counter = 0;                        // pocitadlo na pocet stavov
    int stateNumber;                                       // atribut pre cislo stavu
    Map<String, Integer> nextStates;                       // atribut pre nasledujuci stav z aktualneho
    List<Integer> previousStates;
    String previousTransition;
    List<LALR1item> lalr1Items;                            // atribut pre zapamatanie si LALR1 polozky
    Set<String> transitions;                               // atribut pre ulozenie symbolov cez ktore robime presun
    Set<String> reductions;                                // atribut pre ulozenie symbolov pri ktorych robime redukciu
    ContextFreeGrammar grammar;


    public State(ContextFreeGrammar grammar) {             // konstruktor pre prvy stav, vstupom je bezkontextova G
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
            throw e;
        }
        try {
            transitionsAndReductions();
        } catch (Exception e) {
            System.out.println("Chyba transit&reduct 1.stav.");
            throw e;
        }
        this.stateNumber = counter;
        counter++;

//        printState();
    }

    public State(List<LALR1item> items, ContextFreeGrammar grammar) {         // konstruktor pre presun LALR1 poloziek z predosleho stavu
        this.grammar = grammar;                                               // vstupom su LALR1 pravidla z predosleho stavu a bezkontexotva G
        this.lalr1Items = new ArrayList<>();
        this.lalr1Items.addAll(items);
        this.reductions = new HashSet<>();
        this.transitions = new HashSet<>();
        this.nextStates = new HashMap<>();

        try {
            closure();
        } catch (Exception e) {
            System.out.println("Chyba closure stavy.");
            throw e;
        }
        try {
            transitionsAndReductions();
        } catch (Exception e) {
            System.out.println("Chyba transit&reduct stavy.");
            throw e;
        }
        this.stateNumber = counter;
        counter++;

//        printState();
    }

    private void printState() {
        System.out.println("state " + stateNumber);
        for (LALR1item item : lalr1Items) {
            System.out.println(item);
        }
        System.out.println("T: " + String.join(", ", transitions));
        System.out.println("R: " + String.join(", ", reductions));
        System.out.println();
    }

    private void closure() {                                    // uzaverova operacia
        for (int i = 0; i < lalr1Items.size(); i++) {
            Rule rule = lalr1Items.get(i).LALRrule;

            for (int j = 0; j < rule.getRightSide().size(); j++) {
                String aktualnySymbol = rule.getRightSide().get(j);

                if (Objects.equals(aktualnySymbol, ".") && j + 1 < rule.getRightSide().size()) {

                    String nasledujuciSymbol = rule.getRightSide().get(j + 1);
                    if (grammar.getNonterminals().contains(nasledujuciSymbol)) {

                        for (Rule pravidloGramatiky : grammar.getRules()) {
                            if (Objects.equals(pravidloGramatiky.getLeftSide().get(0), nasledujuciSymbol)) {

                                try {
                                    String naNasledujuciSymbol = rule.getRightSide().get(j + 2);
                                    LALR1item novyRiadok = new LALR1item(pravidloGramatiky, FirstAndFollowClass.first(grammar, naNasledujuciSymbol));
                                    lalr1Items.add(novyRiadok);
                                    continue;
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                LALR1item novyRiadok = new LALR1item(pravidloGramatiky, lalr1Items.get(i).getExpectedSymbols());
                                lalr1Items.add(novyRiadok);
                            }
                        }

                    }

                }
            }
        }
    }


    private void transitionsAndReductions() {                        // Podmienka pre pridanie symbolov do transitions resp reduction
        for (LALR1item riadok : lalr1Items) {
            List<String> rside = riadok.getLALRrule().getRightSide();

            int idxBodka = rside.indexOf(".");
            if (idxBodka == rside.size() - 1) {
                this.reductions.addAll(riadok.getExpectedSymbols());
            } else {
                String nasledujuciSymbol = rside.get(idxBodka + 1);
                this.transitions.add(nasledujuciSymbol);
            }
        }
    }
}