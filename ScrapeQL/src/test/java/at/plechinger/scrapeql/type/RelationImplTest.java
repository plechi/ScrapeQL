/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lukas Plechinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package at.plechinger.scrapeql.type;

import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.RelationImpl;
import at.plechinger.scrapeql.relation.RowImpl;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.RelationImpl;
import at.plechinger.scrapeql.relation.RowImpl;
import com.google.common.io.CharStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by lukas on 06.08.15.
 */
public class RelationImplTest {

    private Relation<String> relation1=new RelationImpl<>();
    private Relation<String> relation2=new RelationImpl<>();

    public void before(String oneCol, String twoCol){

        relation1=new RelationImpl<>();
        relation2=new RelationImpl<>();

        for(int i=0;i<2;i++){
            Relation.Row<String> row=new RowImpl<>();
            for(int j=0;j<3;j++){
                row.setValue(oneCol+"_"+j,"col_"+oneCol+"_"+i+"_"+j);
            }
            relation1.addRow(row);
        }

        for(int i=0;i<3;i++){
            Relation.Row<String> row=new RowImpl<>();
            for(int j=1;j<2;j++){
                row.setValue(twoCol+"_"+j,"col_"+twoCol+"_"+i+"_"+j);
            }
            relation2.addRow(row);
        }
    }

    @Test
    public void testUnion() throws Exception {
        before("one", "one");
        Relation relation=relation1.union(relation2);
        Assert.assertEquals(loadSample(new File("src/test/resources/relation/union.sample")), relation.toString());
    }

    @Test
    public void testCartesian() throws Exception {
        before("one","two");
        Relation relation=relation1.cartesian(relation2);
        Assert.assertEquals(loadSample(new File("src/test/resources/relation/cartesian.sample")), relation.toString());
    }


    private String loadSample(File sample){
        try {
            return CharStreams.toString(new FileReader(sample));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}