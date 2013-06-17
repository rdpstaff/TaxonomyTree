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
 * Main tree node that can be extended
 *
 * @author fishjord
 */
public class Taxon extends Node {

    public static int getUnclassifiedId(int id) {
        return (-id) - 1;
    }

    public static Taxon ROOT_TAXON = new Taxon(0, "Root", "no rank");

    private String name;
    private String rank;
    private boolean unclassified = false;

    public Taxon(int taxid, String name, String rank) {
        this(taxid, name, rank, false);
    }

    public Taxon(Taxon copy) {
        this(copy.getTaxid(), copy.getName(), copy.getRank(), copy.isUnclassified());
    }

    public Taxon(int taxid, String name, String rank, boolean unclassified) {
        super(taxid);
        this.name = name;
        this.rank = rank;
        this.unclassified = unclassified;
    }

    @Override
    public int getTaxid() {
        if(unclassified)
            return getUnclassifiedId(taxid);
        else
            return taxid;
    }

    public String getName() {
        if(unclassified)
            return "unclassified_" + name;
        else
            return name;
    }

    public String getRank() {
        if(unclassified)
            return "";
        else
            return rank;
    }

    public boolean isUnclassified() {
        return unclassified;
    }
}
