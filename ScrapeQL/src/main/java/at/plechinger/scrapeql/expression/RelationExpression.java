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

package at.plechinger.scrapeql.expression;

import at.plechinger.scrapeql.ScrapeQLException;
import at.plechinger.scrapeql.context.Context;
import at.plechinger.scrapeql.loader.Entity;
import at.plechinger.scrapeql.relation.Relation;
import at.plechinger.scrapeql.relation.Selector;
import at.plechinger.scrapeql.value.EntityValue;
import at.plechinger.scrapeql.value.RelationValue;
import at.plechinger.scrapeql.value.Value;
import at.plechinger.scrapeql.value.ValueConverter;
import at.plechinger.scrapeql.util.Map;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lukas on 04.08.15.
 */
public class RelationExpression implements Expression {

    private List<Selector> selectors = Lists.newLinkedList();

    private Optional<String> name = Optional.absent();

    private Expression entityExpression;

    private Optional<Selector> baseSelector = Optional.absent();

    public RelationExpression(Selector... selectors) {
        this.selectors = Lists.newArrayList(selectors);
    }

    public RelationExpression(List<Selector> selectors){
        this.selectors=selectors;
    }

    public RelationExpression as(String name) {
        this.name = Optional.fromNullable(name);
        return this;
    }

    public RelationExpression from(Expression entityExpression) {
        return from(entityExpression, null);
    }

    public RelationExpression from(Expression entityExpression, Selector selector) {
        this.entityExpression = entityExpression;
        this.baseSelector = Optional.fromNullable(selector);
        return this;
    }

    @Override
    public Value<Relation> evaluate(Context ctx) throws ScrapeQLException {
        Entity entity = ValueConverter.toEntityValue(entityExpression.evaluate(ctx)).getValue();

        List<Entity> entities;
        if (baseSelector.isPresent()) {
            entities = entity.select(baseSelector.get().getSelector());
        } else {
            entities = Lists.newArrayList(entity);
        }

        final Map.MapFn<Entity, Value> entityValueMapFn = new Map.MapFn<Entity, Value>() {
            @Override
            public Value map(Entity from) {
                return new EntityValue(from);
            }
        };

        String prefix = "";

        if (name.isPresent()) {
            prefix = name.get() + '.';
        }

        Relation relation = new Relation();

        for (Selector column : selectors) {
            List<Entity> columnValues = Lists.newLinkedList();
            for (Entity row : entities) {
                columnValues.addAll(row.select(column.getSelector()));
            }
            relation.addColumn(prefix + column.getAlias(), Map.map(columnValues, entityValueMapFn));
        }
        if (name.isPresent()) {
            ctx.addRelation(name.get(), relation);
        }
        return new RelationValue(relation);
    }
}
