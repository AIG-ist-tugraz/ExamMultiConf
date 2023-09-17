/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

public class QuestionConstraintLogicalConnective extends QuestionConstraint
{
    private final QuestionConstraint questionConstraint1;
    private final QuestionConstraint questionConstraint2;
    private final boolean isDisjunction;

    public QuestionConstraintLogicalConnective(QuestionConstraint questionConstraint1, QuestionConstraint questionConstraint2, boolean isDisjunction)
    {
        this.questionConstraint1 = questionConstraint1;
        this.questionConstraint2 = questionConstraint2;
        this.isDisjunction = isDisjunction;
    }

    @Override
    public boolean isSatisfied(int questionID)
    {
        if (isDisjunction)
            return questionConstraint1.isSatisfied(questionID) || questionConstraint2.isSatisfied(questionID);

        return questionConstraint1.isSatisfied(questionID) && questionConstraint2.isSatisfied(questionID);
    }
}
