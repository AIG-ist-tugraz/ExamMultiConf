/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.propagators.exam;

import at.tugraz.ist.ase.exammulticonf.constraints.IExamConstraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

public class ExamPropagatorAtMost extends Propagator<SetVar>
{
    private final IExamConstraint constraint;
    private final int value;

    public ExamPropagatorAtMost(SetVar vars, IExamConstraint constraint, int value) {
        super(vars);
        this.constraint = constraint;
        this.value = value;
    }

    @Override
    public void propagate(int i) throws ContradictionException
    {
        SetVar set = vars[0];

        int hitsUpper = constraint.countHits(set.getUB());

        if (hitsUpper <= value)
            setPassive();

        int hitsLower = constraint.countHits(set.getLB());

        if (hitsLower > value)
            fails();
    }

    @Override
    public ESat isEntailed()
    {
        SetVar set = vars[0];

        int hitsUpper = constraint.countHits(set.getUB());

        if (hitsUpper <= value)
            return ESat.TRUE;

        int hitsLower = constraint.countHits(set.getLB());

        if (hitsLower > value)
            return ESat.FALSE;

        return ESat.UNDEFINED;
    }
}