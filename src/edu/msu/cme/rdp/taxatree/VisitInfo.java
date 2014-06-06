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

package edu.msu.cme.rdp.taxatree;

/**
 *
 * @author fishjord
 */
public class VisitInfo<E extends Node> {

    public enum VisitType { down, up };

    private TaxonHolder<E> holder;
    private VisitType visitType;

    public VisitInfo(TaxonHolder<E> holder, VisitType type) {
        this.holder = holder;
        this.visitType = type;
    }

    public int getDepth() {
        int depth = 0;

        TaxonHolder<E> parent = holder.getParent();
        while(parent != null) {
            depth++;
            parent = parent.getParent();
        }

        return depth;
    }

    public E getTaxon() {
        return holder.getTaxon();
    }

    public boolean isLastChild() {
        if(holder.getParent() == null)
            return true;

        return holder.getParent().getChildHolders().indexOf(holder) == holder.getParent().getChildHolders().size() - 1;
    }

    public boolean hasChildren() {
        return holder.getChildHolders().size() != 0;
    }

    public VisitType getVisitType() {
        return visitType;
    }
}
