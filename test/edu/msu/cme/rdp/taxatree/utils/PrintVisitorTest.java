/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.msu.cme.rdp.taxatree.utils;

import edu.msu.cme.rdp.taxatree.Taxon;
import edu.msu.cme.rdp.taxatree.TaxonHolder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class PrintVisitorTest {

    public PrintVisitorTest() {
    }

    @Test
    public void testPrintVisitor() throws IOException {
        Taxon root = new Taxon(0, "root", "root_rank");
        Taxon phylum1 = new Taxon(1, "Proteobacteria", "phylum");        
        Taxon phylum2 = new Taxon(2, "Acidobacteria", "phylum");
        Taxon class1 = new Taxon(3, "Alphaproteobacteria", "class");
        Taxon class2 = new Taxon(4, "Betaproteobacteria", "class");
        
        TaxonHolder rootHolder = new TaxonHolder(root);
        rootHolder.addChild(phylum1, 0);
        rootHolder.addChild(phylum2, 0);
        rootHolder.addChild(class1, 1);
        rootHolder.addChild(class2, 1);
        
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        PrintVisitor visitor = new PrintVisitor(new PrintStream(outstream));
        
        System.out.println("test topDownVisit()");
        rootHolder.topDownVisit(visitor);

        String[] lines = outstream.toString().split("\n");
        assertEquals(lines[0], "root rank=root_rank taxid=0");
        assertEquals(lines[3], "        Betaproteobacteria rank=class taxid=4");
        assertEquals(lines[4], "    Acidobacteria rank=phylum taxid=2");
       
    }
}