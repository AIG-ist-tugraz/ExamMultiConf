/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.configuration;

import at.tugraz.ist.ase.exammulticonf.questions.Question;

import java.util.List;

public class Exam
{
    private final int id;
    private final List<Question> questions;

    public Exam(int id, List<Question> questions)
    {
        this.id = id;
        this.questions = questions;
    }

    public int getID()
    {
        return id;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }
}
