/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

public class QuestionConstraintLogicalConnectiveDefinition extends QuestionConstraintDefinition
{
    private final QuestionConstraintDefinition questionConstraintDefinition1;
    private final QuestionConstraintDefinition questionConstraintDefinition2;
    private final boolean isDisjunction;

    public QuestionConstraintLogicalConnectiveDefinition(QuestionConstraintDefinition questionConstraintDefinition1, QuestionConstraintDefinition questionConstraintDefinition2, boolean isDisjunction)
    {
        this.questionConstraintDefinition1 = questionConstraintDefinition1;
        this.questionConstraintDefinition2 = questionConstraintDefinition2;
        this.isDisjunction = isDisjunction;
    }

    public QuestionConstraintDefinition getQuestionConstraintDefinition1()
    {
        return questionConstraintDefinition1;
    }

    public QuestionConstraintDefinition getQuestionConstraintDefinition2()
    {
        return questionConstraintDefinition2;
    }

    public boolean isDisjunction()
    {
        return isDisjunction;
    }
}
