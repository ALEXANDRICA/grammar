package sk.aos.automata;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.RegularGrammar;

import java.util.*;

@Getter
@Setter
public class DeterministicFinAutomaton {

    private ArrayList<HashSet<String>> states;
    private HashSet<String> inputSymbols;
    private HashSet<String> initialState;
    private HashMap<HashSet<String>, HashMap<String, HashSet<String>>> transitionFunction;
    private ArrayList<HashSet<String>> acceptingStates;
    private String epsilon = "epsilon";

    public DeterministicFinAutomaton(NondeterministicFinAutomaton n) {
        this.inputSymbols = n.getInputSymbols();

        // adds closure of initial state of NFA to initial state of DKA
        HashSet<String> pom = new HashSet<>();
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
            HashSet<String> state = states.get(i);
            HashMap<String, HashSet<String>> pomocna = new HashMap<>();

            // finds result state of state at input symbol
            for (String is : this.inputSymbols) {
                // help variables
                ArrayList<String> pomAL;
                HashSet<String> pomresult = new HashSet<>();
                HashSet<String> result = new HashSet<>();

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

                        for (String s : pomresult) {
                            result.add(s);
                        }

                        // closure of all states in NKA
                        pomresult = closure(result, n.getTransitionFunction());

                        for (String s : pomresult) {
                            result.add(s);
                        }
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
        for (int i = 0; i < this.states.size(); i++) {
            for (String s : n.getAcceptingStates()) {
                if (this.states.get(i).contains(s)) {
                    if (!this.acceptingStates.contains(this.states.get(i))) {
                        this.acceptingStates.add(this.states.get(i));
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

    private HashSet<String> closure(HashSet<String> states, TransitionFunction transitionFunction) {
        ArrayList<String> pom;
        ArrayList<String> statesAL = new ArrayList<>(states);
        ArrayList<String> pom2 = new ArrayList<>();

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
                    for (int i = 0; i < pom2.size(); i++) {
                        // state cant be "-"
                        if (!pom2.get(i).equals("-")) {
                            it.add(pom2.get(i));

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
        for (Map.Entry<HashSet<String>, HashMap<String, HashSet<String>>> entry : transitionFunction.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("------Accepting states:------");
        System.out.println(this.getAcceptingStates());
    }

}
