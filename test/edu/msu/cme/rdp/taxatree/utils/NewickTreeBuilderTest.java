/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.msu.cme.rdp.taxatree.utils;

import edu.msu.cme.rdp.taxatree.ConcretRoot;
import edu.msu.cme.rdp.taxatree.Taxon;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jordan Fish <fishjord at msu.edu>
 */
public class NewickTreeBuilderTest {
    
    public static class TestTaxon extends Taxon{
        private float bl;
        public TestTaxon(int taxid, String name, String rank, float bl) {
            super(taxid, name, rank, false);
            this.bl = bl;
        }
        
        public float getBl() {
            return bl;
        }
    }

    public NewickTreeBuilderTest() {
    }

    @Test
    public void testNewickBuilder() throws IOException {
        TestTaxon root = new TestTaxon(0, "root", "root_rank", 0);
        String newick = "((Alphaproteobacteria:0.01,Betaproteobacteria:1.0)Proteobacteria:1.0,Acidobacteria:1.0)root;" ;
        InputStream is = new ByteArrayInputStream(newick.getBytes("UTF-8"));
        NewickTreeBuilder builder = new NewickTreeBuilder(root, is, new NewickTreeBuilder.NewickTaxonFactory<TestTaxon>() {

            public TestTaxon buildTaxon(int taxid, String name, float distance) {
                return new TestTaxon(taxid, name, "", distance);
            }
        });
        
       ConcretRoot<TestTaxon> rootTaxon = builder.getRoot();
       assertEquals(root.getTaxid(), rootTaxon.getRootTaxid());
       assertEquals(2, (int)builder.getTaxidByName("Proteobacteria"));
       assertEquals(4, (int)builder.getTaxidByName("Betaproteobacteria"));
       assertEquals(5, (int)builder.getTaxidByName("Acidobacteria"));
       
       // test branch length
       TestTaxon alphaTaxon = rootTaxon.getChildTaxon(3);           
       assertEquals(0.01f, alphaTaxon.getBl(), 0.01);
    }
}