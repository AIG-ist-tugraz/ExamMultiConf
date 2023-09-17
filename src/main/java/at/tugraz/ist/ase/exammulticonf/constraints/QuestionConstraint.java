/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

import at.tugraz.ist.ase.exammulticonf.questions.QuestionCollection;
import org.chocosolver.util.objects.setDataStructures.ISet;

import java.util.function.IntConsumer;

public class QuestionConstraint implements IExamConstraint
{
    private final QuestionConstraintDefinition definition;
    private final QuestionCollection questions;
    private final IntConsumer comparisonFunction;

    private boolean isSatisfied;

    protected QuestionConstraint()
    {
        this.definition = null;
        this.questions = null;
        this.comparisonFunction = null;
    }

    public QuestionConstraint(QuestionConstraintDefinition definition, QuestionCollection questions)
    {
        this.definition = definition;
        this.questions = questions;
        this.comparisonFunction = setComparisonFunction();
    }

    private IntConsumer setComparisonFunction()
    {
        switch (definition.getComparisonOperator())
        {
            case "=":
                return this::eq;

            case "<>":
                return this::neq;

            case ">":
                return this::gt;

            case ">=":
                return this::gte;

            case "<":
                return this::lt;

            case "<=":
                return this::lte;

            default:
                throw new RuntimeException("Invalid comparison operator");
        }
    }

    private void eq(int propertyValue)
    {
        isSatisfied = propertyValue == definition.getComparisonValue();
    }

    private void neq(int propertyValue)
    {
        isSatisfied = propertyValue != definition.getComparisonValue();
    }

    private void gt(int propertyValue)
    {
        isSatisfied = propertyValue > definition.getComparisonValue();
    }

    private void gte(int propertyValue)
    {
        isSatisfied = propertyValue >= definition.getComparisonValue();
    }

    private void lt(int propertyValue)
    {
        isSatisfied = propertyValue < definition.getComparisonValue();
    }

    private void lte(int propertyValue)
    {
        isSatisfied = propertyValue <= definition.getComparisonValue();
    }

    public boolean isSatisfied(int questionID)
    {
        comparisonFunction.accept(questions.getQuestionProperty(questionID, definition.getQuestionProperty()));
        return definition.isNegated() != isSatisfied;
    }

    @Override
    public int countHits(ISet exam)
    {
        int hits = 0;

        for (int d : exam)
            hits += isSatisfied(d) ? 1 : 0;

        return hits;
    }

    @Override
    public boolean hasOne(ISet exam)
    {
        for (int d: exam)
        {
            if (isSatisfied(d))
                return true;
        }

        return false;
    }
}
