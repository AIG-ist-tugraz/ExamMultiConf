/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.configuration.utils;

import at.tugraz.ist.ase.exammulticonf.constraints.ExamConstraintAggregatorType;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionCollection;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionProperty;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.SetVar;

public interface IGlobalAggregationConstraintPropagatorBuilder
{
    Propagator<SetVar> buildGlobalQuestionConstraintPropagator(SetVar exam, int value, ExamConstraintAggregatorType aggregatorType, QuestionProperty property, QuestionCollection questions);
}
