package sk.aos.FirstandFollow;

import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.HashSet;
import java.util.Objects;


public class FirstAndFollowClass {

    public static HashSet<String> first(ContextFreeGrammar grammar, String symbol) {
        HashSet<String> first = new HashSet<>();

        if (grammar.getTerminals().contains(symbol)) {
            first.add(symbol); // ak je terminal tak first je symbol
        } else {
            for (Rule r : grammar.getRules()) {
                if (Objects.equals(r.getLeftSide().get(0), symbol)) {
                    if (grammar.getTerminals().contains(r.getRightSide().get(0))) //ak je terminal alebo e vlozime do first
                        first.add(r.getRightSide().get(0));
                    else { // ak je neterminal, prehladavame pravu stranu pravidla

                        for (int i = 0; i < r.getRightSide().size(); i++) {
                            first.addAll(first(grammar, r.getRightSide().get(i)));
                            first.remove("e");
                            if (!first(grammar, r.getRightSide().get(i)).contains("e")) break;
                        }

                    }
                }
            }
        }
        return first;
    }

    public static HashSet<String> Follow(ContextFreeGrammar grammar, String symbol) throws Exception {
        HashSet<String> follow = new HashSet<>();
        HashSet<String> lv_f = new HashSet<>();
        if (grammar.getTerminals().contains(symbol)) throw new Exception("Symbol musi byt z mnoziny neterminalov");

        do {
            lv_f.clear();
            lv_f.addAll(follow);
            for (Rule r : grammar.getRules()) {
                if (r.getRightSide().contains(symbol)) {
                    for (int i = 0; i < r.getRightSide().size(); i++) {
                        if (Objects.equals(r.getRightSide().get(i), symbol)) {
                            if ((i + 1) < r.getRightSide().size()) {
                                follow.addAll(first(grammar, r.getRightSide().get(i + 1)));
                                follow.remove("e");
                                if (first(grammar, r.getRightSide().get(i + 1)).contains("e"))
                                    follow.addAll(Follow(grammar, r.getLeftSide().get(0)));
                            } else if ((i + 1) == r.getRightSide().size()) {
                                if (first(grammar, r.getRightSide().get(i)).contains("e"))
                                    follow.addAll(Follow(grammar, r.getLeftSide().get(0)));
                            }
                        }
                    }
                }
            }
        } while (lv_f.size() != follow.size());

        if (Objects.equals(grammar.getStartsymbol(), symbol))
            follow.add("e");

        return follow;
    }

}
