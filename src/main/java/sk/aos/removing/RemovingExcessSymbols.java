package sk.aos.removing;

import sk.aos.grammar.ContextFreeGrammar;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.HashSet;


public class RemovingExcessSymbols {

    public static ArrayList<String> vytvorNt(ContextFreeGrammar grammar) {
        // Vytvorenie mno�iny netermin�lov, ktor� m��u generovat termin�lne retazce

        ArrayList<String> Nt = new ArrayList<>();
        ArrayList<String> lv_Nt = new ArrayList<>();
        do {
            boolean find;
            lv_Nt.clear();
            lv_Nt.addAll(Nt);

            for (Rule r : grammar.getRules()) {
                find = false;
                for (String t : r.getRightSide()) {
                    if (grammar.getNonterminals().contains(t) && !Nt.contains(t)) {
                        find = true;
                        break;
                    }
                }

                if (!find && !Nt.contains(r.getLeftSide().get(0))) Nt.add(r.getLeftSide().get(0));
            }
        } while (Nt.size() != lv_Nt.size());

        return Nt;
    }

    public static ArrayList<String> vytvorVd(ContextFreeGrammar grammar) {
        //Vytvorenie mno�iny dostupn�ch symbolov gramatiky
        ArrayList<String> Vd = new ArrayList<>();
        ArrayList<String> lv_Vd = new ArrayList<>();
        do {
            if (Vd.isEmpty()) {
                Vd.add(grammar.getStartsymbol());
            } else {

                lv_Vd.clear();
                lv_Vd.addAll(Vd);

                for (Rule r : grammar.getRules()) {
                    if (Vd.contains(r.getLeftSide().get(0))) {
                        for (String value : r.getRightSide())
                            if (!Vd.contains(value))
                                Vd.add(value);
                    }
                }
            }
        } while (Vd.size() != lv_Vd.size());

        return Vd;
    }

    public static ContextFreeGrammar redukovanaGramatika(ContextFreeGrammar grammar) {
        // 1.krok odstranienie neterminalov z gramatiky
        ContextFreeGrammar redG = null; // vystupna redukovana gramatika, zatial null

        ArrayList<String> Nt = vytvorNt(grammar); // mnozina Neterminalov z ktorych vieme odvodit terminaly
        ArrayList<String> Nt_ = new ArrayList<>(); // mnozina Neterminalov, ktore niesu v Nt

        HashSet<String> nter = new HashSet<>(); // nova mnozina neterminalov pre redukovanu gramatiku

        for (String N : grammar.getNonterminals()) {
            if (!Nt.contains(N)) Nt_.add(N);
            else nter.add(N); // naplnenie mnoziny Nt_ a novych neterminalov
        }

        HashSet<Rule> novePravidla = new HashSet<>(); // nove pravidla pre gramatiku

        // odstranenie Neterminalnych symbolov z gramatiky
        for (Rule r : grammar.getRules()) {
            if (Nt.contains(r.getLeftSide().get(0))) {
                novePravidla.add(r);
                for (int i = 0; i < r.getRightSide().size(); i++)
                    if (Nt_.contains(r.getRightSide().get(i)))
                        novePravidla.remove(r);
            }
        }

        try { // 2. krok vytvorenie gramatiky a hladanie dostupnych symbolov
            ContextFreeGrammar novaGramatika = new ContextFreeGrammar(grammar.getTerminals(),
                    nter,
                    novePravidla,
                    grammar.getStartsymbol());

            ArrayList<String> Vd = vytvorVd(novaGramatika);
            ArrayList<String> Vn = new ArrayList<>();
            HashSet<String> terminals = new HashSet<>();
            HashSet<String> nterminals = new HashSet<>();

            for (String s : novaGramatika.getNonterminals())
                if (!Vd.contains(s)) Vn.add(s);
                else nterminals.add(s);
            for (String s : novaGramatika.getTerminals())
                if (!Vd.contains(s)) Vn.add(s);
                else terminals.add(s);

            HashSet<Rule> redukovanePravidla = new HashSet<>();

            for (Rule r : novaGramatika.getRules()) {
                if (Vd.contains(r.getLeftSide().get(0))) {
                    redukovanePravidla.add(r);
                    for (int i = 0; i < r.getRightSide().size(); i++)
                        if (Vn.contains(r.getRightSide().get(i)))
                            redukovanePravidla.remove(r);
                }
            }

            redG = new ContextFreeGrammar(terminals,
                    nterminals,
                    redukovanePravidla,
                    novaGramatika.getStartsymbol());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redG;
    }

}
