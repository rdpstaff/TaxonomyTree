/*
 * Copyright (C) 2012 Michigan State University <rdpstaff at msu.edu>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.msu.cme.rdp.taxatree.interfaces;

import edu.msu.cme.rdp.taxatree.Node;
import edu.msu.cme.rdp.taxatree.VisitInfo;

/**
 *
 * @author fishjord
 */
public interface TreeVisitor<E extends Node> {

    /**
     * Visits a node
     * @param taxon
     * @return true - tells the TreeBuilder to keep traversing the tree, false - causes the TreeBuilder to return
     */
    public boolean visitNode(VisitInfo<E> visitInfo);
}
