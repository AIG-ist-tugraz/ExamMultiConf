/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.configuration.utils;

import at.tugraz.ist.ase.exammulticonf.configuration.QuestionScope;
import at.tugraz.ist.ase.exammulticonf.constraints.QuestionConstraint;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.*;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.SetVar;

public class GlobalQuestionConstraintPropagatorBuilder
{
    public static Propagator<SetVar> buildQuestionConstraintPropagatorAtLeast(SetVar exam, QuestionScope scope, QuestionConstraint constraint)
    {
        return new ExamPropagatorAtLeast(exam, constraint, scope.getValue());
    }

    public static Propagator<SetVar> buildQuestionConstraintPropagatorAtMost(SetVar exam, QuestionScope scope, QuestionConstraint constraint)
    {
        return new ExamPropagatorAtMost(exam, constraint, scope.getValue());
    }

    public static Propagator<SetVar> buildQuestionConstraintPropagatorExact(SetVar exam, QuestionScope scope, QuestionConstraint constraint)
    {
        return new ExamPropagatorExact(exam, constraint, scope.getValue());
    }

    public static Propagator<SetVar> buildQuestionConstraintPropagatorBetween(SetVar exam, QuestionScope scope, QuestionConstraint constraint)
    {
        return new ExamPropagatorBetween(exam, constraint, scope.getValue(), scope.getValue2());
    }

    public static Propagator<SetVar> buildQuestionConstraintPropagatorPercent(SetVar exam, QuestionScope scope, QuestionConstraint constraint)
    {
        return scope.getRange() == ExamPropagatorScope.BETWEEN
                ? new ExamPropagatorPercent(exam, constraint, scope.getValue(), scope.getValue2())
                : new ExamPropagatorPercent(exam, constraint ,scope.getRange(), scope.getValue());
    }
}
