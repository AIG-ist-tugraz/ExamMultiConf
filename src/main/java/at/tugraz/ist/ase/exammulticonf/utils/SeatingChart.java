/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.utils;

import java.util.ArrayList;
import java.util.List;

public class SeatingChart
{
    private final int rows;
    private final int columns;

    public SeatingChart(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
    }

    public int[] getNeighbours(int examID, boolean includeDiagonals)
    {
        if (examID < 0 || examID > (rows * columns - 1))
            throw new RuntimeException("ExamID not found in exams");

        List<Integer> neighbours = new ArrayList<>();

        boolean hasLeftNeighbour = hasLeftNeighbour(examID);
        boolean hasBottomNeighbour = hasBottomNeighbour(examID);
        boolean hasRightNeighbour = hasRightNeighbour(examID);
        boolean hasTopNeighbour = hasTopNeighbour(examID);

        if (hasLeftNeighbour)
            neighbours.add(examID - 1);

        if (hasBottomNeighbour)
            neighbours.add(examID + columns);

        if (hasRightNeighbour)
            neighbours.add(examID + 1);

        if (hasTopNeighbour)
            neighbours.add(examID - columns);

        if (includeDiagonals)
        {
            if (hasLeftNeighbour && hasTopNeighbour)
                neighbours.add(examID - 1 - columns);

            if (hasLeftNeighbour && hasBottomNeighbour)
                neighbours.add(examID - 1 + columns);

            if (hasRightNeighbour && hasBottomNeighbour)
                neighbours.add(examID + 1 + columns);

            if (hasRightNeighbour && hasTopNeighbour)
                neighbours.add(examID + 1 - columns);
        }

        return neighbours.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean hasLeftNeighbour(int examID)
    {
        return examID % columns != 0;
    }

    private boolean hasBottomNeighbour(int examID)
    {
        return examID < ((rows - 1) * columns);
    }

    private boolean hasRightNeighbour(int examID)
    {
        return examID % columns != columns - 1;
    }

    private boolean hasTopNeighbour(int examID)
    {
         return examID > columns - 1;
    }
}
