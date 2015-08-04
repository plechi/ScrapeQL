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

package at.plechinger.scrapeql

import at.plechinger.scrapeql.expression.join.InnerJoin
import at.plechinger.scrapeql.expression.value.{Value, DecimalValue, IntegerValue, StringValue}
import at.plechinger.scrapeql.expression._
import scala.collection.JavaConversions._
import scala.util.parsing.combinator.JavaTokenParsers
;


class ScrapeParser extends JavaTokenParsers{

  def query=KW_SELECT ~> repsep(expressionAndColumnSelect,",") ~ repsep(relation,",") ~ from

  def from:Parser[FromExpression] = KW_FROM ~> identifier ~ opt(join) ^^{
    case i~j => j match{
      case join:FromExpression => new FromExpression(i,join)
      case Nil => new FromExpression(i);
    }
  }

  def join:Parser[FromExpression]=cartesianJoin|innerJoin;

  def cartesianJoin:Parser[FromExpression]= KW_JOIN ~>identifier ^^(s=>new FromExpression(s))

  def innerJoin:Parser[FromExpression]=KW_INNER ~ KW_JOIN ~> identifier ~opt(join) ^^ {
    case i~j => j match{
      case join:FromExpression => new FromExpression(i,new InnerJoin,join)
      case Nil => new FromExpression(i);
    }
  }

  //Expressions
  def expression:Parser[Expression]=function|valueExpression;

  def valueExpression:Parser[ValueExpression]=value ^^(v=>new ValueExpression(v));

  def expressionAndColumnSelect:Parser[Expression]=expression|columnSelect;

  def columnSelect:Parser[ColumnSelectExpression] = (identifier|"*") ^^(s=>new ColumnSelectExpression(s));

  def alias:Parser[AliasExpression] = expression ~ KW_AS ~ identifier ^^{case e~_~i => new AliasExpression(e,i)}
  def function:Parser[FunctionExpression]=identifier~"("~expressionList<~")" ^^{case i~_~p=>new FunctionExpression(i,p.toList)};

  def expressionList:Parser[List[Expression]]=repsep(expression,",");

  //Relation
  def relation=KW_RELATION ~> expressionList ~ KW_FROM ~ expression ~ KW_AS ~ identifier;

  //Values
  def value:Parser[Value[_]]= stringValue|decimalValue|intValue;

  def stringValue:Parser[StringValue]="'"~>"[.]*".r <~"'" ^^ (new StringValue(_));
  def decimalValue:Parser[DecimalValue] = "[-]?[0-9]+\\.[0-9]+".r ^^(s=> new DecimalValue(s.toDouble));
  def intValue:Parser[IntegerValue]="[1-9][0-9]*|0|-[1-9][0-9]*".r ^^(s=>new IntegerValue(s.toLong));

  //terminal
  private val identifier:Parser[String]="[a-zA-z_][a-zA-Z0-9_]*".r;

  private val KW_AS="AS";
  private val KW_RELATION="RELATION"
  private val KW_FROM="FROM"
  private val KW_SELECT="SELECT"
  private val KW_ON="ON"
  private val KW_INNER="INNER"
  private val KW_JOIN="JOIN"
}