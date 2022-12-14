package sk.aos.LALR1;

import lombok.Getter;
import lombok.Setter;
import sk.aos.grammar.Rule;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
@Setter
public class LALR1item {

    Rule LALRrule;											// atribut pre LALR1 pravidlo s "." ako ukazovatel
    HashSet<String> expectedSymbols;		// atribut reprezentujuci ocakavany symbol pre LALR1 polozku

	public LALR1item(Rule rule, HashSet<String> expectSymbols){    // parametre su pravidla G, z kt. chceme vytvorit LALR1 polozky v jednotl. stavoch; + ocakavane symboly
        ArrayList<String> rightSide = new ArrayList<>(rule.getRightSide());
        ArrayList<String> leftSide = new ArrayList<>(rule.getLeftSide());

        if(rightSide.contains(".")) {							// vytvorenie novej polozky s ukazovatelom "." na cast pravidla, ktora uz bola spracovana v zasobniku
            int i = rightSide.indexOf(".");
            rightSide.set(i, rule.getRightSide().get(i+1));
            rightSide.set(i+1, ".");
        } else {                                                // else vytvorenie novej polozky LALR1 s ukazovatelom "." na zaciatku
            int size = rightSide.size();
            rightSide.add(rightSide.get(size-1));
            for(int i = size-1; i > 0; i--) {
                rightSide.set(i, rightSide.get(i-1));
            }
            rightSide.set(0, ".");
        }

        this.LALRrule = new Rule(leftSide, rightSide);
        this.expectedSymbols = expectSymbols;
    }

}

