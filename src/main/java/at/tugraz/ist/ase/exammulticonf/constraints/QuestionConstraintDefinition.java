/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

import at.tugraz.ist.ase.exammulticonf.questions.QuestionProperty;

public class QuestionConstraintDefinition
{
    private final QuestionProperty questionProperty;
    private final String comparisonOperator;
    private final int comparisonValue;
    private final boolean negated;

    protected QuestionConstraintDefinition()
    {
        this.questionProperty = null;
        this.comparisonOperator = null;
        this.comparisonValue = 0;
        this.negated = false;
    }

    public QuestionConstraintDefinition(QuestionProperty questionProperty, String comparisonOperator, int comparisonValue, boolean negated)
    {
        this.questionProperty = questionProperty;
        this.comparisonOperator = comparisonOperator;
        this.comparisonValue = comparisonValue;
        this.negated = negated;
    }

    public QuestionConstraintDefinition(QuestionProperty questionProperty, String comparisonOperator, int comparisonValue)
    {
        this(questionProperty, comparisonOperator, comparisonValue, false);
    }

    public QuestionProperty getQuestionProperty()
    {
        return questionProperty;
    }

    public String getComparisonOperator()
    {
        return comparisonOperator;
    }

    public int getComparisonValue()
    {
        return comparisonValue;
    }

    public boolean isNegated()
    {
        return negated;
    }
}
