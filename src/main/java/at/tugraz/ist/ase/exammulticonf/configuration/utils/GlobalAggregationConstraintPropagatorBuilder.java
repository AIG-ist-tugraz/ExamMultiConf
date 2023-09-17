/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.configuration.utils;

import at.tugraz.ist.ase.exammulticonf.constraints.ExamConstraintAggregator;
import at.tugraz.ist.ase.exammulticonf.constraints.ExamConstraintAggregatorType;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorAtLeast;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorAtMost;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorExact;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionCollection;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionProperty;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.SetVar;

public class GlobalAggregationConstraintPropagatorBuilder
{
    public static Propagator<SetVar> buildAggregatorConstraintPropagatorAtLeast(SetVar exam, int value, ExamConstraintAggregatorType aggregatorType, QuestionProperty property, QuestionCollection questions)
    {
        return new ExamPropagatorAtLeast(exam, new ExamConstraintAggregator(aggregatorType, property, questions), value);
    }

    public static Propagator<SetVar> buildAggregatorConstraintPropagatorAtMost(SetVar exam,  int value, ExamConstraintAggregatorType aggregatorType, QuestionProperty property, QuestionCollection questions)
    {
        return new ExamPropagatorAtMost(exam, new ExamConstraintAggregator(aggregatorType, property, questions), value);
    }

    public static Propagator<SetVar> buildAggregatorConstraintPropagatorExact(SetVar exam,  int value, ExamConstraintAggregatorType aggregatorType, QuestionProperty property, QuestionCollection questions)
    {
        return new ExamPropagatorExact(exam, new ExamConstraintAggregator(aggregatorType, property, questions), value);
    }
}
