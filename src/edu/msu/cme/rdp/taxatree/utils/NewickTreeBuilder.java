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

import edu.msu.cme.rdp.taxatree.ConcretRoot;
import edu.msu.cme.rdp.taxatree.Taxon;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author fishjord
 */
public class NewickTreeBuilder<E extends Taxon> {

    public static interface NewickTaxonFactory<E extends Taxon> {
        public E buildTaxon(int taxid, String name, float distance);
    }

    private static class IdGen {
        private static int id = 1;

        public static int nextId() {
            return id++;
        }
    }

    private static class TaxaNameGen {
        private static int id = 1;

        public static String nextLabel() {
            return "Unknown_Taxon_" + (id++);
        }
    }

    private static class NewickTaxon<E extends Taxon> {
        E t;
        int parent;
    }

    private PushbackReader reader;
    private NewickTaxonFactory<E> taxonFactory;
    private ConcretRoot<E> root;
    private int lineno = 1, col = 1;

    private Map<String, Integer> nameToIdMap = new HashMap();

    public NewickTreeBuilder(E rootTaxon, InputStream is, NewickTaxonFactory<E> taxonFactory) throws IOException {
        root = new ConcretRoot<E>(rootTaxon);
        reader = new PushbackReader(new InputStreamReader(is));
        this.taxonFactory = taxonFactory;
        parseNewick();
    }

    public NewickTreeBuilder(ConcretRoot<E> root, InputStream is, NewickTaxonFactory<E> taxonFactory) throws IOException {
        this.root = root;
        reader = new PushbackReader(new InputStreamReader(is));
        this.taxonFactory = taxonFactory;
        parseNewick();
    }

    public ConcretRoot<E> getRoot() {
        return root;
    }

    public Integer getTaxidByName(String name) {
        return nameToIdMap.get(name);
    }

    private char read() throws IOException {
        int ret = ' ';

        do {
            ret = reader.read();
            if(ret == '\n') {
                lineno++;
                col = 1;
            } else {
                col++;
            }

        } while(ret > 0 && Character.isWhitespace(ret));

        if(ret == -1)
            throw new IOException("Unexpected end of file");

        return (char)ret;
    }

    private void parseNewick() throws IOException {
        Stack<NewickTaxon<E>> taxaStack = new Stack();

        parseTaxon(root.getRootTaxid(), taxaStack);
        char next = read();
        if(next != ';')
            doError("Unexpected " + next + ", expected ;");

        while(!taxaStack.empty()) {
            NewickTaxon<E> taxon = taxaStack.pop();
            root.addChild(taxon.t, taxon.parent);
            
            nameToIdMap.put(taxon.t.getName(), taxon.t.getTaxid());
        }
    }

    private void parseTaxon(int parentId, Stack<NewickTaxon<E>> taxa) throws IOException {
        int taxid = IdGen.nextId();
        char next = read();
        if(next == '(') {

            while(true) {
                parseTaxon(taxid, taxa);

                next = read();
                if(next == ')')
                    break;
                else if(next == ',')
                    continue;
                else
                    doError("Unexpected " + next + ", expected ) or ,");
            }
        } else
            reader.unread(next);

        NewickTaxon taxon = new NewickTaxon();
        taxon.t = parseTaxonObject(taxid);
        taxon.parent = parentId;
        taxa.push(taxon);
    }

    private Taxon parseTaxonObject(int taxid) throws IOException {
        StringBuffer buf = new StringBuffer();
        float dist = 0;

        boolean singleQuote = false, doubleQuote = false;
        while(true) {
            char next = read();
            if(!singleQuote && !doubleQuote && next == ':') {
                dist = parseDistance();
                break;
            } else if(!singleQuote && !doubleQuote && (next == ',' || next == ';')) {
                reader.unread(next);
                break;
            } else if(next == '\'' && !doubleQuote)
                singleQuote = !singleQuote;
             else if(next == '"' && !singleQuote)
                doubleQuote = !doubleQuote;

            buf.append(next);
        }

        String name = buf.toString();
        if(name.length() == 0)
            name = TaxaNameGen.nextLabel();

        return taxonFactory.buildTaxon(taxid, name, dist);
    }

    private float parseDistance() throws IOException {
        StringBuffer ret = new StringBuffer();
        char next = read();

        while(Character.isDigit(next) || next == '.' || next == '-') {
            ret.append(next);
            next = read();
        }

        reader.unread(next);

        float dist = 0.0f;
        try {
            dist = Float.parseFloat(ret.toString());
            if(dist < 0)
                dist = 0.0f;
        } catch(NumberFormatException e) {
            doError("Invalid distance value \"" + ret.toString() + "\"");
        }

        return dist;
    }

    private void doError(String message) throws IOException {
        throw new IOException("Error line=" + lineno + " col=" + col + ": " + message);
    }
}
