/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.msu.cme.rdp.taxatree.utils;

import edu.msu.cme.rdp.taxatree.Taxon;
import edu.msu.cme.rdp.taxatree.TaxonHolder;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class NewickPrintVisitorTest {

    public NewickPrintVisitorTest() {
    }

    @Test
    public void testNewickVisitor() {
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
        NewickPrintVisitor visitor = new NewickPrintVisitor(new PrintStream(outstream));
        
        System.out.println("test biDirectionDepthFirst()");
        rootHolder.biDirectionDepthFirst(visitor);

        String[] lines = outstream.toString().split("\n");
        assertEquals(lines[0], "((Alphaproteobacteria:1.0,Betaproteobacteria:1.0)Proteobacteria:1.0,Acidobacteria:1.0)root;");

    }
}