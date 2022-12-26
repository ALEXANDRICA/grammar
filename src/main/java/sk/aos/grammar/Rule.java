package sk.aos.grammar;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rule {

    private List<String> leftSide;
    private List<String> rightSide;

    public Rule(List<String> leftSide, List<String> rightSide) {
        super();
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Rule rule) {
            return leftSide.equals(rule.getLeftSide())
                    && rightSide.equals(rule.getRightSide());
        }
        return false;
    }

}
