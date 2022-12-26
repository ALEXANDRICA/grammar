package sk.aos.automata;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.RegularGrammar;
import sk.aos.grammar.Rule;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class NondeterministicFinAutomaton {

    private Set<String> states;
    private Set<String> inputSymbols;
    private String initialState;
    private TransitionFunction transitionFunction;
    private Set<String> acceptingStates;
    private final String epsilon = "epsilon";
    private final String qf = "q";

    public NondeterministicFinAutomaton(Set<String> states, Set<String> inputSymbols, String initialState, TransitionFunction transitionFunction, Set<String> acceptingStates) {
        super();
        this.states = states;
        this.inputSymbols = inputSymbols;
        this.initialState = initialState;
        this.transitionFunction = transitionFunction;
        this.acceptingStates = acceptingStates;
    }

    public NondeterministicFinAutomaton(RegularGrammar g) throws Exception {
        this.states = g.getNonterminals();
        this.states.add(qf);
        this.inputSymbols = g.getTerminals();

        // if there is an epsilon in right side of the rules, adds epsilon to inputSymbols
        int pom = 0;
        for (Rule r : g.getRules()) {
            if (r.getRightSide().get(0).equals("epsilon")) {
                pom++;
            }
        }
        if (pom > 0) {
            this.inputSymbols.add("epsilon");
        }

        this.initialState = g.getStartsymbol();
        this.acceptingStates = new HashSet<>();
        if (g.getStartrule().getRightSide().equals("epsilon")) {
            this.acceptingStates.add(g.getStartsymbol());
        }

        this.acceptingStates.add(qf);

        this.transitionFunction = new TransitionFunction(this.states, this.inputSymbols);
        for (Rule r : g.getRules()) {
            if (r.getRightSide().size() == 1) {
                this.transitionFunction.add(r.getLeftSide().get(0), r.getRightSide().get(0), qf);
            } else if (r.getRightSide().size() == 2) {
                this.transitionFunction.add(r.getLeftSide().get(0), r.getRightSide().get(0), r.getRightSide().get(1));
            } else {
                throw new Exception("Grammar is not regular");
            }
        }
    }

}
