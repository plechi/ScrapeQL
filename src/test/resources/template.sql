/*
 * The MIT License
 *
 * Copyright 2015 Lukas Plechinger.
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

LOAD "URL";
SELECT 'selector' AS mainselector FROM ':root'; --same as SELECT FIRST
SELECT FIRST 'h1' AS header FROM genericname; 
SELECT EVERY 'selector' AS name1, 'selector' AS name2, CONCAT('selector1','selector2',"FIXSTRING") AS name3, header AS name4 IN 'selector' FROM genericname INTO listvariable;

OUTPUT listvariable;



--Sprachkonstrukte:

--LOAD 'url' - Lädt HTML von url und erzeugt daraus ein neues Dokument,
--SELECT (FIRST) 'selector' AS elementname FROM ('selector'|otherelementname)
--SELECT EVERY ('selector|otherelementname) AS elementname (, ...) FROM ('selector'|otherelementname) INTO element

--OUTPUT elementname