/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.configuration;

import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorScope;

public class QuestionScope
{
    private final ExamPropagatorScope range;
    private final int value;
    private final Integer value2;
    private final boolean isPercent;

    public QuestionScope(ExamPropagatorScope range, int value, boolean isPercent)
    {
        this.range = range;
        this.value = value;
        this.value2 = null;
        this.isPercent = isPercent;
    }

    public QuestionScope(ExamPropagatorScope range, int value)
    {
        this(range, value, false);
    }

    public QuestionScope(int valueLower, int valueUpper, boolean isPercent)
    {
        this.range = ExamPropagatorScope.BETWEEN;
        this.value = valueLower;
        this.value2 = valueUpper;
        this.isPercent = isPercent;
    }

    public QuestionScope(int valueLower, int valueUpper)
    {
        this(valueLower, valueUpper, false);
    }

    public ExamPropagatorScope getRange()
    {
        return range;
    }

    public int getValue()
    {
        return value;
    }

    public int getValue2()
    {
        if (value2 == null)
            throw new RuntimeException("QuestionScope was not configured for lower and upper bound values");

        return value2;
    }

    public boolean isPercent()
    {
        return isPercent;
    }
}
