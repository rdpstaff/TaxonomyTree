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

import edu.msu.cme.rdp.taxatree.VisitInfo.VisitType;
import edu.msu.cme.rdp.taxatree.interfaces.TreeVisitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * This class handles the main work of the actual tree structure
 * It maintains a reference to a taxon representing the actual data of the node
 * and a list of holders that are the children
 *
 * @author fishjord
 */
public class TaxonHolder<E extends Node> {
    final private E taxon;
    private TaxonHolder<E> parent;
    private List<TaxonHolder<E>> children = new ArrayList();

    public TaxonHolder(E taxon) {
        this.taxon = taxon;
    }

    TaxonHolder(E taxon, TaxonHolder<E> parent) {
        this.taxon = taxon;
        this.parent = parent;
    }

    public E getTaxon() {
        return taxon;
    }

    public void addChild(E child, int parentId) {
        TaxonHolder<E> parentHolder = getChild(parentId);

        if(parentHolder != null) {
            TaxonHolder<E> childHolder = new TaxonHolder<E>(child, parentHolder);
            parentHolder.addChild(childHolder);
        }
    }

    public Collection<E> getChildren() {
        List<E> ret = new ArrayList();
        for(TaxonHolder<E> child : children)
            ret.add(child.getTaxon());

        return ret;
    }

    public List<TaxonHolder<E>> getChildHolders() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Convenience method that returns the enclosed taxon
     * instead of the taxonholder for the given id
     *
     * @param idToFind
     * @return the taxon if found, null otherwise
     */
    public E getChildTaxon(int idToFind) {
        TaxonHolder<E> result = getChild(idToFind);

        if(result == null)
            return null;

        return result.getTaxon();
    }

    /*
     * returns only the immediate child taxon with matching name
     */
    public TaxonHolder<E> getImediateChildTaxon(String name){
        for (  TaxonHolder<E> child: children){
            if (  ((Taxon)child.getTaxon()).getName().equals(name)){
                return child;
            }
        }
        return null;
    }
    /**
     * Find a taxon with the given id in the sub tree
     * rooted at this node
     *
     * @param idToFind
     * @return the holder that contains the taxon with the specified id
     * null otherwise
     */
    public TaxonHolder<E> getChild(int idToFind) {
        return findSubNode(idToFind);
    }

    void setParent(TaxonHolder<E> parent) {
        this.parent = parent;
    }

    public TaxonHolder<E> getParent() {
        return parent;
    }

    void addChild(TaxonHolder<E> childHolder) {
        children.add(childHolder);
    }

    private TaxonHolder<E> findSubNode(int taxid) {
        if(taxid == taxon.getTaxid())
            return this;

        TaxonHolder<E> ret = null;

        for(TaxonHolder<E> child : children) {
            if((ret = child.findSubNode(taxid)) != null)
                return ret;
        }

        return null;
    }

    /**
     *
     * Operations
     *
     */

    /**
     * Visits all the nodes in the tree in top down depth first order
     * @param visitor
     */
    public void topDownVisit(TreeVisitor<E> visitor) {
        topDownVisit(this, visitor, 0);
    }

    private static boolean topDownVisit(TaxonHolder t, TreeVisitor v, int depth) {
        if(!v.visitNode(new VisitInfo(t, VisitType.down)))
            return false;

        for(TaxonHolder child : (Collection<TaxonHolder>)t.getChildHolders())
            if(!topDownVisit(child, v, depth + 1))
                return false;

        return true;
    }


    /**
     * Visits all the nodes in the tree once on the way down
     * and then again on the way back up (on the way up the depth is
     * negative of the way down)
     * @param visitor
     */
    public void biDirectionDepthFirst(TreeVisitor visitor) {
        biDirectionDepthFirst(this, visitor, 0);
    }

    private static boolean biDirectionDepthFirst(TaxonHolder t, TreeVisitor v, int depth) {
        if(!v.visitNode(new VisitInfo(t, VisitType.down)))
            return false;

        for(TaxonHolder child : (Collection<TaxonHolder>)t.getChildHolders())
            if(!biDirectionDepthFirst(child, v, depth + 1))
                return false;

        if(!v.visitNode(new VisitInfo(t, VisitType.up)))
            return false;

        return true;
    }
}
