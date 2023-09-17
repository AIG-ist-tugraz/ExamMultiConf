/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

import at.tugraz.ist.ase.exammulticonf.questions.QuestionProperty;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionCollection;
import org.chocosolver.util.objects.setDataStructures.ISet;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

public class ExamConstraintAggregator implements IExamConstraint
{
    private final QuestionProperty questionProperty;
    private final Supplier<Integer> aggregatorFunction;
    private final QuestionCollection questions;

    private ISet exam;

    public ExamConstraintAggregator(ExamConstraintAggregatorType aggregatorType, QuestionProperty questionProperty, QuestionCollection questions)
    {
        this.questionProperty = questionProperty;
        this.aggregatorFunction = setAggregatorFunction(aggregatorType);
        this.questions = questions;
    }

    private Supplier<Integer> setAggregatorFunction(ExamConstraintAggregatorType aggregatorType)
    {
        switch (aggregatorType)
        {
            case SUM:
                return this::sum;

            case AVG:
                return this::avg;

            case COUNT:
                return this::count;

            case DCOUNT:
                return this::dcount;

            default:
                throw new RuntimeException("Invalid aggregator type");
        }
    }

    private int sum()
    {
        int sum = 0;

        for (int d : exam)
            sum += questions.getQuestionProperty(d, questionProperty);

        return sum;
    }

    private int avg()
    {
        if (exam.isEmpty())
            return 0;

        int sum = sum();
        return sum / exam.size();
    }

    private int count()
    {
        return exam.size();
    }

    private int dcount()
    {
        Set<Integer> set = new TreeSet<>();

        for (int d : exam)
            set.add(questions.getQuestionProperty(d, questionProperty));

        return set.size();
    }

    @Override
    public int countHits(ISet exam)
    {
        this.exam = exam;
        return aggregatorFunction.get();
    }

    @Override
    public boolean hasOne(ISet exam)
    {
        return countHits(exam) > 0;
    }
}
