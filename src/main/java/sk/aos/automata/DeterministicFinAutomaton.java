package sk.aos.automata;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.RegularGrammar;

import java.util.*;

@Getter
@Setter
public class DeterministicFinAutomaton {

    private List<Set<String>> states;
    private Set<String> inputSymbols;
    private Set<String> initialState;
    private Map<Set<String>, Map<String, Set<String>>> transitionFunction;
    private List<Set<String>> acceptingStates;
    private String epsilon = "epsilon";

    public DeterministicFinAutomaton(NondeterministicFinAutomaton n) {
        this.inputSymbols = n.getInputSymbols();

        // adds closure of initial state of NFA to initial state of DKA
        Set<String> pom = new HashSet<>();
        pom.add(n.getInitialState());
        this.initialState = closure(pom, n.getTransitionFunction());

        // adds result to states of DKA
        this.states = new ArrayList<>();
        this.states.add(initialState);

        // initialize of transition function
        this.transitionFunction = new HashMap<>();

        // for all states of DKA
        for (int i = 0; i < this.states.size(); i++) {
            // gets state of DKA
            Set<String> state = states.get(i);
            Map<String, Set<String>> pomocna = new HashMap<>();

            // finds result state of state at input symbol
            for (String is : this.inputSymbols) {
                // help variables
                List<String> pomAL;
                Set<String> pomresult = new HashSet<>();
                Set<String> result = new HashSet<>();

                if (!is.equals("epsilon")) {
                    for (String stInNKA : state) {
                        pomAL = new ArrayList<>();
                        try {
                            pomAL = n.getTransitionFunction().getAL(stInNKA, is);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // won't add "-" to the result
                        if (pomAL.size() > 0) {
                            for (String s : pomAL) {
                                if (!s.equals("-")) {
                                    pomresult.add(s);
                                }
                            }
                        }

                        result.addAll(pomresult);

                        // closure of all states in NKA
                        pomresult = closure(result, n.getTransitionFunction());

                        result.addAll(pomresult);
                    }

                    if (result.size() > 0) {
                        // if states doesn't contain result adds result to states of DKA
                        if (!states.contains(result)) {
                            states.add(result);
                        }

                        // adds result to the transition function of DKA
                        pomocna.put(is, result);
                    }
                }
            }

            if (!pomocna.isEmpty()) {
                this.transitionFunction.put(state, pomocna);
            }
        }

        // finds accepting states in set of states
        this.acceptingStates = new ArrayList<>();
        for (Set<String> state : this.states) {
            for (String s : n.getAcceptingStates()) {
                if (state.contains(s)) {
                    if (!this.acceptingStates.contains(state)) {
                        this.acceptingStates.add(state);
                    }
                }
            }
        }
    }

    public DeterministicFinAutomaton(RegularGrammar g) {
        try {
            NondeterministicFinAutomaton n = new NondeterministicFinAutomaton(g);
            DeterministicFinAutomaton d = new DeterministicFinAutomaton(n);
            this.states = d.getStates();
            this.inputSymbols = d.getInputSymbols();
            this.initialState = d.getInitialState();
            this.acceptingStates = d.getAcceptingStates();
            this.transitionFunction = d.getTransitionFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<String> closure(Set<String> states, TransitionFunction transitionFunction) {
        List<String> pom;
        List<String> statesAL = new ArrayList<>(states);
        List<String> pom2 = new ArrayList<>();

        // if there is epsilon defined as input symbol
        if (transitionFunction.getColumnHeaders().contains("epsilon")) {
            do {
                // stores initial set of states
                pom = statesAL;
                ListIterator<String> it = statesAL.listIterator();
                while (it.hasNext()) {
                    String s = it.next();
                    // adds states, to which automaton can get on epsilon
                    try {
                        pom2 = transitionFunction.getAL(s, "epsilon");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (String value : pom2) {
                        // state cant be "-"
                        if (!value.equals("-")) {
                            it.add(value);

                        }
                    }
                }
            }
            while (pom.size() != statesAL.size());
        }

        states = new HashSet<>(statesAL);
        return states;
    }

    public void showDFA() {
        System.out.println("SHOWING DFA");
        System.out.println("-------States of DFA:-------");
        System.out.println(this.getStates());
        System.out.println("-------Input Symbols-------");
        System.out.println(this.getInputSymbols());
        System.out.println("------Transition Function:------");
        for (Map.Entry<Set<String>, Map<String, Set<String>>> entry : transitionFunction.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("------Accepting states:------");
        System.out.println(this.getAcceptingStates());
    }

}
