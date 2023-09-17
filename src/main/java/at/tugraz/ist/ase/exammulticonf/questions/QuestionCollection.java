/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.questions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionCollection
{
    private final Map<Integer, Question> questions;

    public QuestionCollection(List<Question> questions)
    {
        this.questions = new HashMap<>();

        questions.forEach(t -> this.questions.put(t.getID(), t));
    }

    public Question get(int questionID)
    {
        return questions.get(questionID);
    }

    public int getQuestionProperty(int questionID, QuestionProperty property)
    {
        return get(questionID).getProperty(property);
    }
}
