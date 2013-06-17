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
import edu.msu.cme.rdp.taxatree.VisitInfo.VisitType;
import edu.msu.cme.rdp.taxatree.interfaces.TreeVisitor;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 *
 * @author fishjord
 */
public class NewickPrintVisitor<E extends Taxon> implements TreeVisitor<E> {

    public static interface NewickDistanceFactory {
        public float getDistance(int taxid);
    }

    private PrintStream writer;
    private NewickDistanceFactory distFactory;
    private boolean printInnerNodeLabels;

    public NewickPrintVisitor(PrintStream writer) {
        this(writer, true, new NewickDistanceFactory() {
            public float getDistance(int taxid) {
                return 1;
            }

        });
    }

    public NewickPrintVisitor(PrintStream writer, boolean innerNodeLabels) {
        this(writer, innerNodeLabels, new NewickDistanceFactory() {
            public float getDistance(int taxid) {
                return 1;
            }

        });
    }
    
    public NewickPrintVisitor(PrintStream writer, boolean innerNodeLabels, NewickDistanceFactory factory) {
        this.writer = writer;
        this.distFactory = factory;
        this.printInnerNodeLabels = innerNodeLabels;
    }

    public boolean visitNode(VisitInfo<E> visitInfo) {
//        char [] indentChars = new char[visitInfo.getDepth() * 4];
//        Arrays.fill(indentChars, ' ');
        String indent = "";//new String(indentChars);

        if(visitInfo.getVisitType() == VisitType.down) {
            if(visitInfo.hasChildren())
                writer.print(indent + "(");
        } else if(visitInfo.getVisitType() == VisitType.up) {
            String paren = "";
            if(visitInfo.hasChildren())
                paren = ")";

            String label = "";
            if(printInnerNodeLabels || !visitInfo.hasChildren()) {
                label = visitInfo.getTaxon().getName();
                if(label.contains(" "))
                    label = "\"" + label + "\"";
            }

            if(visitInfo.getDepth() > 0)
                writer.print(indent + paren + label + ":" + distFactory.getDistance(visitInfo.getTaxon().getTaxid()));
            else
                writer.print(indent + paren + label + ";");

            if(!visitInfo.isLastChild())
                writer.print(",");
//            else
//                writer.print();
        }


        return true;
    }

}
