/*
 * Solving Multi-Configuration Problems: A Performance Analysis with Choco Solver
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology, Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.exammulticonf.constraints;

import org.chocosolver.util.objects.setDataStructures.ISet;

public interface IExamConstraint
{
    int countHits(ISet exam);

    boolean hasOne(ISet exam);
}
