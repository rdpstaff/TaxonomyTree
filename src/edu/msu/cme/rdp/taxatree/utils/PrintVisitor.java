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

package edu.msu.cme.rdp.taxatree.utils;

import edu.msu.cme.rdp.taxatree.Taxon;
import edu.msu.cme.rdp.taxatree.VisitInfo;
import edu.msu.cme.rdp.taxatree.interfaces.TreeVisitor;
import java.io.PrintStream;
import java.util.Arrays;

/**
 *
 * @author fishjord
 */
public class PrintVisitor<E extends Taxon> implements TreeVisitor<E> {

    private char indentCharacter;
    private int indent;
    private PrintStream out;
    
    public PrintVisitor() {
        this(4, ' ', System.out);
    }
    
    public PrintVisitor(int indent) {
        this(indent, ' ', System.out);
    }
    
    public PrintVisitor(int indent, char indentCharacter) {
        this(indent, indentCharacter, System.out);
    }

    public PrintVisitor(int indent, PrintStream out) {
        this(indent, ' ', out);
    }

    public PrintVisitor(PrintStream out) {
        this(4, ' ', out);
    }
    
    public PrintVisitor(int indent, char indentCharacter, PrintStream out) {
        this.indent = indent;
        this.indentCharacter = indentCharacter;
        this.out = out;
    }

    public boolean visitNode(VisitInfo<E> visitInfo) {
        char [] indentChars = new char[visitInfo.getDepth() * indent];
        Arrays.fill(indentChars, indentCharacter);
        String indentString = new String(indentChars);

        out.println(indentString + visitInfo.getTaxon().getName() + " rank=" + visitInfo.getTaxon().getRank() + " taxid=" + visitInfo.getTaxon().getTaxid());
        return true;
    }


}
