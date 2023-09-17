/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.propagators.exam;

import at.tugraz.ist.ase.exammulticonf.constraints.QuestionConstraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

import java.util.function.DoubleConsumer;

public class ExamPropagatorPercent extends Propagator<SetVar>
{
    private final QuestionConstraint constraint;
    private final double value;
    private final double value2;
    private final DoubleConsumer comparisonFunction;

    private boolean isScopeSatisfied;

    public ExamPropagatorPercent(SetVar vars, QuestionConstraint constraint, int valueLower, int valueUpper) {
        super(vars);
        this.constraint = constraint;
        this.value = valueLower / 100.0;
        this.value2 = valueUpper / 100.0;
        this.comparisonFunction = setComparisonFunction(ExamPropagatorScope.BETWEEN);

        this.isScopeSatisfied = false;
    }

    public ExamPropagatorPercent(SetVar vars, QuestionConstraint constraint, ExamPropagatorScope scope, int value) {
        super(vars);

        this.constraint = constraint;
        this.value = value / 100.0;
        this.value2 = value / 100.0;
        this.comparisonFunction = setComparisonFunction(scope);

        this.isScopeSatisfied = false;
    }

    private DoubleConsumer setComparisonFunction(ExamPropagatorScope scope)
    {
        switch (scope)
        {
            case EXACT:
                return this::compareExact;

            case ATLEAST:
                return this::compareAtLeast;

            case ATMOST:
                return this::compareAtMost;

            case BETWEEN:
                return this::compareBetween;

            default:
                throw new RuntimeException("Invalid question scope");
        }
    }

    private void compareExact(double ratio)
    {
        isScopeSatisfied = ratio == value;
    }

    private void compareAtLeast(double ratio)
    {
        isScopeSatisfied = ratio >= value;
    }

    private void compareAtMost(double ratio)
    {
        isScopeSatisfied = ratio <= value;
    }

    private void compareBetween(double ratio)
    {
        isScopeSatisfied = (ratio >= value) && (ratio <= value2);
    }

    @Override
    public void propagate(int x) throws ContradictionException
    {
        SetVar set = vars[0];

        int currentHits = constraint.countHits(set.getLB());
        int currentSize = set.getLB().size();
        int currentNonHits = currentSize - currentHits;

        int maxHits = constraint.countHits(set.getUB());
        int maxNonHits = set.getUB().size() - maxHits;

        for (int hits = currentHits; hits <= maxHits; hits++)
        {
            for (int nonHits = currentNonHits; nonHits <= maxNonHits; nonHits++)
            {
                int size = hits + nonHits;
                double ratio = size == 0 ? 0 : hits / (double)size;
                comparisonFunction.accept(ratio);

                if (isScopeSatisfied)
                    return;
            }
        }

        fails();
    }

    @Override
    public ESat isEntailed()
    {
        SetVar set = vars[0];

        if (set.getLB().isEmpty())
        {
            comparisonFunction.accept(0);
            return isScopeSatisfied ? ESat.TRUE : ESat.FALSE;
        }

        int hitsLower = constraint.countHits(set.getLB());

        comparisonFunction.accept(hitsLower / (double)set.getLB().size());

        if (isScopeSatisfied)
            return ESat.TRUE;

        return ESat.FALSE;
    }
}
