/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.propagators.crossexam;

import at.tugraz.ist.ase.exammulticonf.constraints.IExamConstraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

public class CrossExamPropagatorExact extends Propagator<SetVar>
{
    private final IExamConstraint constraint;
    private final int value;

    public CrossExamPropagatorExact(SetVar[] vars, IExamConstraint constraint, int value) {
        super(vars);
        this.constraint = constraint;
        this.value = value;
    }

    @Override
    public void propagate(int i) throws ContradictionException
    {
        int hitsUpper = 0;

        for (SetVar set : vars)
            hitsUpper += constraint.hasOne(set.getUB()) ? 1 : 0;

        if (hitsUpper < value)
            fails();

        int hitsLower = 0;

        for (SetVar set : vars)
            hitsLower += constraint.hasOne(set.getLB()) ? 1 : 0;

        if (hitsLower > value)
            fails();
    }

    @Override
    public ESat isEntailed()
    {
        int hitsUpper = 0;

        for (SetVar set : vars)
            hitsUpper += constraint.hasOne(set.getUB()) ? 1 : 0;

        if (hitsUpper < value)
            return ESat.FALSE;

        int hitsLower = 0;

        for (SetVar set : vars)
            hitsLower += constraint.hasOne(set.getLB()) ? 1 : 0;

        if (hitsLower > value)
            return ESat.FALSE;

        if (hitsUpper == hitsLower)
            return ESat.TRUE;

        return ESat.UNDEFINED;
    }
}
