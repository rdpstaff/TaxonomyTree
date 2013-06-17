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

import edu.msu.cme.rdp.taxatree.interfaces.TreeVisitor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fishjord
 */
public class ConcretRoot<E extends Node> {
    protected TaxonHolder<E> root;
    protected Map<Integer, TaxonHolder<E>> taxonMap = new HashMap();

    public ConcretRoot(E rootTaxon) {
        this.root = new TaxonHolder<E>(rootTaxon);
        taxonMap.put(rootTaxon.getTaxid(), root);
    }

    public int getRootTaxid() {
        return root.getTaxon().getTaxid();
    }

    public TaxonHolder<E> getChild(int txid) {
        return taxonMap.get(txid);
    }

    public E getChildTaxon(int txid) {
        TaxonHolder<E> holder = taxonMap.get(txid);
        if(holder == null)
            return null;
        return holder.getTaxon();
    }

    public void addChild(E child, int parentId) {
        if(taxonMap.containsKey(parentId)) {
            TaxonHolder<E> parentHolder = taxonMap.get(parentId);
            TaxonHolder<E> childHolder = new TaxonHolder<E>(child, parentHolder);

            parentHolder.addChild(childHolder);
            taxonMap.put(child.getTaxid(), childHolder);
        } else
            throw new IllegalArgumentException("Failed to find parent with taxid=" + parentId + " in tree");
    }

    public void topDownVisit(TreeVisitor<E> visitor) {
        root.topDownVisit(visitor);
    }

    public void biDirectionDepthFirst(TreeVisitor visitor) {
        root.biDirectionDepthFirst(visitor);
    }
}
