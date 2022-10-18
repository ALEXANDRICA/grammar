package sk.aos.grammar;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Rule {

    private ArrayList<String> leftSide;
    private ArrayList<String> rightSide;

    public Rule(ArrayList<String> leftSide, ArrayList<String> rightSide) {
        super();
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

}
