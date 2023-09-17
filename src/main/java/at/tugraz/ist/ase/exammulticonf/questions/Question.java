/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.questions;

public class Question
{
    private final int id;
    private final int topic;
    private final int level;
    private final int durationMin;
    private final int durationMax;
    private final int type;
    private final int points;

    private final int[] properties;

    public Question(int id, int topic, int level, int durationMin, int durationMax, int type, int points)
    {
        this.id = id;
        this.topic = topic;
        this.level = level;
        this.durationMin = durationMin;
        this.durationMax = durationMax;
        this.type = type;
        this.points = points;

        this.properties = new int[] { id, topic, level, durationMin, durationMax, type, points };
    }

    public int getID()
    {
        return id;
    }

    public int getTopic()
    {
        return topic;
    }

    public int getLevel()
    {
        return level;
    }

    public int getDurationMin()
    {
        return durationMin;
    }

    public int getDurationMax()
    {
        return durationMax;
    }

    public int getType()
    {
        return type;
    }

    public int getPoints()
    {
        return points;
    }

    public int getProperty(QuestionProperty property)
    {
        return this.properties[property.ordinal()];
    }

    @Override
    public String toString()
    {
        return String.format("%2d|%2d|%2d|%2d|%2d|%2d|%2d\n", id, topic, level, durationMin, durationMax, type, points);
    }
}
