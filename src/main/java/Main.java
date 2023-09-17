/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

import at.tugraz.ist.ase.exammulticonf.configuration.AggregationConstraintComparisonOperator;
import at.tugraz.ist.ase.exammulticonf.configuration.Exam;
import at.tugraz.ist.ase.exammulticonf.configuration.ExamConfiguration;
import at.tugraz.ist.ase.exammulticonf.configuration.QuestionScope;
import at.tugraz.ist.ase.exammulticonf.constraints.ExamConstraintAggregatorType;
import at.tugraz.ist.ase.exammulticonf.constraints.QuestionConstraintDefinition;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorScope;
import at.tugraz.ist.ase.exammulticonf.questions.Question;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionProperty;

import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        //configuration for two exams and ten questions
        ExamConfiguration examConfiguration = new ExamConfiguration(2, getQuestions());

        //each exam should have exactly two questions belonging to topic 1
        examConfiguration.addGlobalQuestionConstraint(new QuestionScope(ExamPropagatorScope.EXACT, 2), new QuestionConstraintDefinition(QuestionProperty.TOPIC, "=", 1));

        //each exam should have between four and five questions
        examConfiguration.addGlobalQuestionAggregationConstraint(ExamConstraintAggregatorType.COUNT, QuestionProperty.ID, 4, 5);

        //each exam should consist of at least three different topics
        examConfiguration.addGlobalQuestionAggregationConstraint(ExamConstraintAggregatorType.DCOUNT, QuestionProperty.TOPIC, AggregationConstraintComparisonOperator.ATLEAST, 3);

        //exams may share at most only one question
        examConfiguration.addSharedQuestionRestrictionConstraint(new int[] { 0, 1 }, ExamPropagatorScope.ATMOST, 1);

        //between 20% and 40% of all questions of each exam must be of level 2
        examConfiguration.addGlobalQuestionConstraint(new QuestionScope(20, 40, true), new QuestionConstraintDefinition(QuestionProperty.LEVEL, "=", 2));

        //the question with ID 8 needs to be part of exactly one exam
        examConfiguration.addCrossExamConstraint(new QuestionScope(ExamPropagatorScope.EXACT, 1), new QuestionConstraintDefinition(QuestionProperty.ID, "=", 8));

        printExams(examConfiguration);
    }

    public static List<Question> getQuestions()
    {
        List<Question> questions = new ArrayList<>();

        //                         ID TOPIC LEVEL DUR_MIN DUR_MAX TYPE POINTS
        questions.add(new Question( 1,    4,    1,      4,     10,   3,     2));
        questions.add(new Question( 2,    1,    2,      4,     12,   1,     5));
        questions.add(new Question( 3,    3,    2,      4,     13,   2,     7));
        questions.add(new Question( 4,    1,    3,      2,     11,   2,     2));
        questions.add(new Question( 5,    1,    1,      3,     12,   2,     6));
        questions.add(new Question( 6,    4,    3,      5,     11,   3,     7));
        questions.add(new Question( 7,    1,    2,      2,     11,   1,     8));
        questions.add(new Question( 8,    3,    1,      4,     14,   3,     7));
        questions.add(new Question( 9,    2,    3,      3,     15,   1,     2));
        questions.add(new Question(10,    2,    2,      5,     10,   1,     1));

        return questions;
    }

    private static void printExams(ExamConfiguration conf)
    {
        Scanner sc = new Scanner(System.in);
        int solutionCounter = 1;

        while (conf.hasNextSolution())
        {
            System.out.println("\n========================================");
            System.out.printf("Solution %d:\n", solutionCounter++);
            System.out.println("========================================");

            List<Exam> exams = conf.getConfigurations();

            for (Exam exam : exams)
            {
                System.out.printf("Exam ID: %d\n", exam.getID());
                System.out.println("    ID|TP|LV|DI|DA|TY|PT");

                for (int i = 0; i < exam.getQuestions().size(); i++)
                {
                    Question q = exam.getQuestions().get(i);

                    System.out.printf("%2d: %s", i + 1, q);
                }

                System.out.println();
            }

            System.out.print("Press ENTER to see the next solution...");
            sc.nextLine();
        }

        System.out.println("\nDone");
    }
}
