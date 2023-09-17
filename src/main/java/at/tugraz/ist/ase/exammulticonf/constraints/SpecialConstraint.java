/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorScope;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.SetVar;

public class SpecialConstraint
{
    public static void restrictSharedQuestions(Model model, int[] questions, SetVar[] exams, ExamPropagatorScope scope, int value)
    {
        for (int i = 0; i < exams.length; i++)
        {
            for (int j = i + 1; j < exams.length; j++)
            {
                SetVar interSet = model.setVar(new int[]{}, questions);
                SetVar[] examSets = { exams[i], exams[j] };

                model.intersection(examSets, interSet).post();

                if (scope == ExamPropagatorScope.EXACT)
                    interSet.getCard().eq(value).post();
                else if (scope == ExamPropagatorScope.ATLEAST)
                    interSet.getCard().ge(value).post();
                else if (scope == ExamPropagatorScope.ATMOST)
                    interSet.getCard().le(value).post();
                else
                    throw new RuntimeException("Invalid exam scope");
            }
        }
    }

    public static void restrictSharedQuestions(Model model, int[] questions, SetVar master, SetVar[] slaves, ExamPropagatorScope scope, int value)
    {
        for (int i = 0; i < slaves.length; i++)
        {
            SetVar interSet = model.setVar(new int[]{}, questions);
            SetVar[] examSets = { master, slaves[i] };

            model.intersection(examSets, interSet).post();

            if (scope == ExamPropagatorScope.EXACT)
                interSet.getCard().eq(value).post();
            else if (scope == ExamPropagatorScope.ATLEAST)
                interSet.getCard().ge(value).post();
            else if (scope == ExamPropagatorScope.ATMOST)
                interSet.getCard().le(value).post();
            else
                throw new RuntimeException("Invalid exam scope");
        }
    }
}
