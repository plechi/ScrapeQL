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

import at.plechinger.scrapeql.expression._
import at.plechinger.scrapeql.filter._
import at.plechinger.scrapeql.query.SelectQuery
import at.plechinger.scrapeql.relation.Selector
import at.plechinger.scrapeql.value.{FloatValue, IntegerValue, StringValue, Value}

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.util.parsing.combinator.JavaTokenParsers



class ScrapeParser extends JavaTokenParsers {

  def query: Parser[SelectQuery] = KW_SELECT ~> repsep(expressionAndColumnSelect, ",") ~ KW_FROM ~ repsep(relation, ",") ~ opt(where) ^^ {
    case c ~ _ ~ r ~ w => new SelectQuery(new ListBuffer ++ c).from(new ListBuffer ++ r).where(w.getOrElse(null))
  }

  //Expressions
  def expression: Parser[Expression] = function | valueExpression |columnSelect;

  def valueExpression: Parser[ValueExpression] = value ^^ (v => new ValueExpression(v));

  def expressionAndColumnSelect: Parser[Expression] = starSelect | starAll | expression;

  def columnSelect: Parser[VariableExpression] = identifier ^^ (s => new VariableExpression(s));

  def starSelect: Parser[StarExpression] = "(.+)\\.\\*".r ^^ (s => new StarExpression(s.replaceAll("(.+)\\.\\*","$1")))

  def starAll: Parser[StarExpression] = "*" ^^ (s => new StarExpression())

  def alias: Parser[AliasExpression] = expression ~ KW_AS ~ identifier ^^ {
    case e ~ _ ~ i => new AliasExpression(e, i)
  }

  def function: Parser[FunctionExpression] = identifier ~ "(" ~ expressionList <~ ")" ^^ {
    case i ~ _ ~ p => new FunctionExpression(i, new ListBuffer ++ p)
  };

  def expressionList: Parser[List[Expression]] = repsep(expression, ",");

  //where clause

  def where: Parser[Filter] = KW_WHERE ~> filter ^^ (f => f)

  def filter: Parser[Filter] = equals | and | or | not

  def and: Parser[AndFilter] = filter ~ KW_AND ~ filter ^^ { case f ~ _ ~ g => new AndFilter(f, g) }

  def or: Parser[OrFilter] = filter ~ KW_OR ~ filter ^^ { case f ~ _ ~ g => new OrFilter(f, g) }

  def not: Parser[NotFilter] = KW_NOT ~> filter ^^ (n => new NotFilter(n))

  def equals: Parser[EqualsFilter] = expression ~ (KW_IS | "=" | "<>") ~ expression ^^ { case e ~ _ ~ f => new EqualsFilter(e, f) }

  //Relation
  def relation: Parser[RelationExpression] = "(" ~ KW_RELATION ~> relationSelectorList ~ KW_FROM ~ function ~
    opt(relationSelector) ~ ")" ~ opt(opt(KW_AS) ~ identifier) ^^ {
    case l ~ _ ~ f ~ b ~ _ ~ i => new RelationExpression(new ListBuffer ++ l)
      .from(f, b.getOrElse(null)).as(i.get._2)
  };

  def relationSelectorList: Parser[List[Selector]] = repsep(relationSelector, ',');

  def relationSelector: Parser[Selector] = "$('" ~ string ~ "')" ~ opt(KW_AS ~> identifier) ^^ {
    case _ ~ c ~ _ ~ e => new Selector(c, e)
  }

  //Values
  def value: Parser[Value[_]] = stringValue | decimalValue | intValue;

  def stringValue: Parser[StringValue]=basicString | bigStringValue;
  //def basicString:Parser[StringValue]=stringLiteral ^^(s=>new StringValue(s));

  def basicString: Parser[StringValue] = "'" ~> string <~ "'" ^^ (s=>new StringValue(s));
  def bigStringValue: Parser[StringValue]="TXT>>>"~>"""(?s).*?(?=<<<TXT)""".r<~"<<<TXT" ^^(s=>new StringValue(s));

  def decimalValue: Parser[FloatValue] = floatingPointNumber ^^ (s => new FloatValue(s.toDouble));
  def intValue: Parser[IntegerValue] = wholeNumber ^^ (s => new IntegerValue(s.toLong));

  //terminal
  private val identifier: Parser[String] = "[a-zA-z_\\.][a-zA-Z0-9_\\.]*".r;
  private val string: Parser[String] = "((?:\\\\'|[^'])*)".r;

  private val KW_AS = "AS";
  private val KW_RELATION = "RELATION"
  private val KW_FROM = "FROM"
  private val KW_SELECT = "SELECT"
  private val KW_WHERE = "WHERE"
  private val KW_AND = "AND"
  private val KW_OR = "OR"
  private val KW_NOT = "NOT"
  private val KW_IS = "IS"


  def parse(s: String): ParseResult[SelectQuery] = {
    parseAll(query, s)
  }
}