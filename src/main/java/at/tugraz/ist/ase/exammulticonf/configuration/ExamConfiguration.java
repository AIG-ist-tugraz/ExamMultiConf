/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.configuration;

import at.tugraz.ist.ase.exammulticonf.configuration.utils.GlobalAggregationConstraintPropagatorBuilder;
import at.tugraz.ist.ase.exammulticonf.configuration.utils.GlobalQuestionConstraintPropagatorBuilder;
import at.tugraz.ist.ase.exammulticonf.configuration.utils.IGlobalAggregationConstraintPropagatorBuilder;
import at.tugraz.ist.ase.exammulticonf.configuration.utils.IGlobalQuestionConstraintPropagatorBuilder;
import at.tugraz.ist.ase.exammulticonf.constraints.*;
import at.tugraz.ist.ase.exammulticonf.propagators.crossexam.CrossExamPropagatorAtLeast;
import at.tugraz.ist.ase.exammulticonf.propagators.crossexam.CrossExamPropagatorAtMost;
import at.tugraz.ist.ase.exammulticonf.propagators.crossexam.CrossExamPropagatorBetween;
import at.tugraz.ist.ase.exammulticonf.propagators.crossexam.CrossExamPropagatorExact;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorBetween;
import at.tugraz.ist.ase.exammulticonf.propagators.exam.ExamPropagatorScope;
import at.tugraz.ist.ase.exammulticonf.questions.Question;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionCollection;
import at.tugraz.ist.ase.exammulticonf.questions.QuestionProperty;
import at.tugraz.ist.ase.exammulticonf.utils.SeatingChart;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.SetVar;

import java.util.*;

public class ExamConfiguration
{
    private final int examCount;
    private final int[] questions;
    private final QuestionCollection questionCollection;

    private final Model model;
    private final Solver solver;
    private final Solution solution;
    private final SetVar[] exams;

    public ExamConfiguration(int examCount, List<Question> questions)
    {
        this.examCount = examCount;
        this.questionCollection = new QuestionCollection(questions);
        this.questions = questions.stream().mapToInt(Question::getID).toArray();

        this.model = new Model();
        this.solver = this.model.getSolver();
        this.solution = new Solution(model);
        this.exams = new SetVar[examCount];

        initExamConfiguration();
    }

    private void initExamConfiguration()
    {
        for (int i = 0; i < examCount; i++)
            exams[i] = model.setVar("exam" + i, new int[]{}, this.questions);
    }

    /**
     * Adds a new intra-exam constraint to the exam configuration
     * @param scope Constraint scope that restricts how many questions per exam need to satisfy this constraint
     * @param constraintDef Constraint definition that specifies which question properties are constrained to which values
     */
    public void addGlobalQuestionConstraint(QuestionScope scope, QuestionConstraintDefinition constraintDef)
    {
        IGlobalQuestionConstraintPropagatorBuilder propagatorBuilder = getQuestionConstraintPropagatorBuilder(scope);

        for (SetVar exam : exams)
        {
            Propagator<SetVar> propagator = propagatorBuilder.buildGlobalQuestionConstraintPropagator(exam, scope, buildLogicalConnectiveConstraint(constraintDef));
            model.post(new Constraint(UUID.randomUUID().toString(), propagator));
        }
    }

    /**
     * Adds a new student specific intra-exam constraint to the exam configuration
     * @param scope Constraint scope that restricts how many questions of the student's exam need to satisfy this constraint
     * @param constraintDef Constraint definition that specifies which question properties are constrained to which values
     * @param examID ID of the student's exam that is to be constrained
     */
    public void addStudentQuestionConstraint(QuestionScope scope, QuestionConstraintDefinition constraintDef, int examID)
    {
        IGlobalQuestionConstraintPropagatorBuilder propagatorBuilder = getQuestionConstraintPropagatorBuilder(scope);

        SetVar exam = exams[examID];

        Propagator<SetVar> propagator = propagatorBuilder.buildGlobalQuestionConstraintPropagator(exam, scope, buildLogicalConnectiveConstraint(constraintDef));
        model.post(new Constraint(UUID.randomUUID().toString(), propagator));
    }

    private IGlobalQuestionConstraintPropagatorBuilder getQuestionConstraintPropagatorBuilder(QuestionScope scope)
    {
        IGlobalQuestionConstraintPropagatorBuilder propagatorBuilder;

        if (scope.isPercent())
            propagatorBuilder = GlobalQuestionConstraintPropagatorBuilder::buildQuestionConstraintPropagatorPercent;
        else if (scope.getRange() == ExamPropagatorScope.EXACT)
            propagatorBuilder = GlobalQuestionConstraintPropagatorBuilder::buildQuestionConstraintPropagatorExact;
        else if (scope.getRange() == ExamPropagatorScope.ATLEAST)
            propagatorBuilder = GlobalQuestionConstraintPropagatorBuilder::buildQuestionConstraintPropagatorAtLeast;
        else if(scope.getRange() == ExamPropagatorScope.BETWEEN)
            propagatorBuilder = GlobalQuestionConstraintPropagatorBuilder::buildQuestionConstraintPropagatorBetween;
        else
            propagatorBuilder = GlobalQuestionConstraintPropagatorBuilder::buildQuestionConstraintPropagatorAtMost;

        return propagatorBuilder;
    }

    /**
     * Adds a new intra-exam constraint to the exam configuration that supports aggregation
     * @param aggregatorType Aggregation function to be used
     * @param property Question property that is input of the aggregation function
     * @param op Operator used to compare the result of the aggregation function
     * @param value Value that is compared to the result of the aggregation function
     */
    public void addGlobalQuestionAggregationConstraint(ExamConstraintAggregatorType aggregatorType, QuestionProperty property, AggregationConstraintComparisonOperator op, int value)
    {
        IGlobalAggregationConstraintPropagatorBuilder propagatorBuilder;

        if (op == AggregationConstraintComparisonOperator.EXACT)
            propagatorBuilder = GlobalAggregationConstraintPropagatorBuilder::buildAggregatorConstraintPropagatorExact;
        else if (op == AggregationConstraintComparisonOperator.ATLEAST)
            propagatorBuilder = GlobalAggregationConstraintPropagatorBuilder::buildAggregatorConstraintPropagatorAtLeast;
        else
            propagatorBuilder = GlobalAggregationConstraintPropagatorBuilder::buildAggregatorConstraintPropagatorAtMost;

        for (SetVar exam : exams)
        {
            Propagator<SetVar> propagator = propagatorBuilder.buildGlobalQuestionConstraintPropagator(exam, value, aggregatorType, property, questionCollection);
            model.post(new Constraint(UUID.randomUUID().toString(), propagator));
        }
    }

    /**
     * Adds a new intra-exam constraint to the exam configuration that supports aggregation
     * @param aggregatorType Aggregation function to be used
     * @param property Question property that is input of the aggregation function
     * @param valueLower Minimum value of the aggregation function's outcome
     * @param valueUpper Maximum value of the aggregation function's outcome
     */
    public void addGlobalQuestionAggregationConstraint(ExamConstraintAggregatorType aggregatorType, QuestionProperty property, int valueLower, int valueUpper)
    {
        for (SetVar exam : exams)
        {
            Propagator<SetVar> propagator = new ExamPropagatorBetween(exam, new ExamConstraintAggregator(aggregatorType, property, questionCollection), valueLower, valueUpper);
            model.post(new Constraint(UUID.randomUUID().toString(), propagator));
        }
    }

    /**
     * Adds a new inter-exam constraint to the exam configuration
     * @param scope Constraint scope that restricts how many exams need to satisfy this constraint
     * @param constraintDef Constraint definition that specifies which question properties are constrained to which value
     */
    public void addCrossExamConstraint(QuestionScope scope, QuestionConstraintDefinition constraintDef)
    {
        Propagator<SetVar> propagator;

        if (scope.isPercent())
            throw new RuntimeException("Cross exam constraints do not support scope percentages");
        else if (scope.getRange() == ExamPropagatorScope.EXACT)
            propagator = new CrossExamPropagatorExact(exams, buildLogicalConnectiveConstraint(constraintDef), scope.getValue());
        else if (scope.getRange() == ExamPropagatorScope.ATLEAST)
            propagator = new CrossExamPropagatorAtLeast(exams, buildLogicalConnectiveConstraint(constraintDef), scope.getValue());
        else if (scope.getRange() == ExamPropagatorScope.BETWEEN)
            propagator = new CrossExamPropagatorBetween(exams, buildLogicalConnectiveConstraint(constraintDef), scope.getValue(), scope.getValue2());
        else
            propagator = new CrossExamPropagatorAtMost(exams, buildLogicalConnectiveConstraint(constraintDef), scope.getValue());

        model.post(new Constraint(UUID.randomUUID().toString(), propagator));
    }

    /**
     * Adds a new inter-exam constraint to the exam configuration that restricts the degree of question overlap that exams have in common
     * @param examIDs IDs of exams to include in this constraint
     * @param scope Operator used to compare the degree of question overlap
     * @param value Value that is compared to the degree of question overlap
     */
    public void addSharedQuestionRestrictionConstraint(int[] examIDs, ExamPropagatorScope scope, int value)
    {
        SetVar[] exams = new SetVar[examIDs.length];

        for (int i = 0; i < examIDs.length; i++)
        {
            exams[i] = this.exams[examIDs[i]];
        }

        SpecialConstraint.restrictSharedQuestions(model, questions, exams, scope, value);
    }

    /**
     * Adds a new inter-exam constraint to the exam configuration that restricts the degree of question overlap that neighboring exams have in common
     * @param scope Operator used to compare the degree of question overlap
     * @param value Value that is compared to the degree of question overlap
     * @param seatRows Number of rows in the lecture hall
     * @param seatCols Number of seats per row
     * @param includeDiagonals Include diagonally adjacent exams
     */
    public void restrictSharedQuestionWithSeatingChart(ExamPropagatorScope scope, int value, int seatRows, int seatCols, boolean includeDiagonals)
    {
        SeatingChart seatingChart = new SeatingChart(seatRows, seatCols);

        for (int i = 0; i < examCount; i++)
        {
            int examID = i;
            int[] ns = seatingChart.getNeighbours(examID, includeDiagonals);
            int[] nsUpper = Arrays.stream(ns).filter(t -> t > examID && t < examCount).toArray();

            SetVar[] neighbours = new SetVar[nsUpper.length];
            SetVar master = exams[examID];

            for (int j = 0; j < neighbours.length; j++)
            {
                neighbours[j] = exams[nsUpper[j]];
            }

            SpecialConstraint.restrictSharedQuestions(model, questions, master, neighbours, scope, value);
        }
    }

    private QuestionConstraint buildLogicalConnectiveConstraint(QuestionConstraintDefinition constraintDef)
    {
        if (!(constraintDef instanceof QuestionConstraintLogicalConnectiveDefinition))
            return new QuestionConstraint(constraintDef, questionCollection);

        QuestionConstraintLogicalConnectiveDefinition constraintLogicDef = (QuestionConstraintLogicalConnectiveDefinition)constraintDef;

        return new QuestionConstraintLogicalConnective(
                buildLogicalConnectiveConstraint(constraintLogicDef.getQuestionConstraintDefinition1()),
                buildLogicalConnectiveConstraint(constraintLogicDef.getQuestionConstraintDefinition2()),
                constraintLogicDef.isDisjunction());
    }

    /**
     * Tries to find a solution for the exam configuration
     * @return true if a solution was found, otherwise false
     */
    public boolean hasNextSolution()
    {
        if (solver.solve())
        {
            solution.record();
            return true;
        }

        return false;
    }

    /**
     * Returns all instantiated exams of the current solution
     * @return list of exams
     */
    public List<Exam> getConfigurations()
    {
        List<Exam> ex = new ArrayList<>();

        for (int i = 0; i < examCount; i++)
        {
            Exam exam = getConfiguration(i);
            ex.add(exam);
        }

        return ex;
    }

    /**
     * Returns a single instantiated exam of the current solution
     * @param examID ID of the exam to return
     * @return exam
     */
    public Exam getConfiguration(int examID)
    {
        List<Question> questions = new ArrayList<>();

        for (int qID : solution.getSetVal(exams[examID]))
        {
            questions.add(questionCollection.get(qID));
        }

        return new Exam(examID, questions);
    }
}
