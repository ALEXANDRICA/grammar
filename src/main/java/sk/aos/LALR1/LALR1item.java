package sk.aos.LALR1;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class LALR1item {

    Rule LALRrule;                                          // atribut pre LALR1 pravidlo s "." ako ukazovatel
    Set<String> expectedSymbols;                            // atribut reprezentujuci ocakavany symbol pre LALR1 polozku


    public LALR1item(Rule rule, Set<String> expectSymbols) {             // parametre su pravidla G, z kt. chceme vytvorit LALR1 polozky v jednotl. stavoch; + ocakavane symboly
        List<String> rightSide = new ArrayList<>(rule.getRightSide());
        List<String> leftSide = new ArrayList<>(rule.getLeftSide());

        if (rightSide.contains(".")) {                            // vytvorenie novej polozky s ukazovatelom "." na cast pravidla, ktora uz bola spracovana v zasobniku
            int i = rightSide.indexOf(".");
            rightSide.set(i, rule.getRightSide().get(i + 1));
            rightSide.set(i + 1, ".");
        } else {                                                // else vytvorenie novej polozky LALR1 s ukazovatelom "." na zaciatku
            rightSide.add(0, ".");
        }

        this.LALRrule = new Rule(leftSide, rightSide);
        this.expectedSymbols = expectSymbols;
    }

    @Override
    public String toString() {
        return String.format(
                "%s -> %s {%s}",
                LALRrule.getLeftSide().get(0),
                String.join(" ", LALRrule.getRightSide()),
                String.join(", ", expectedSymbols));
    }
}
