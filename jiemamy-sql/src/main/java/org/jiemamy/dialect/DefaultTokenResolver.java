/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/12
 *
 * This file is part of Jiemamy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jiemamy.dialect;

import java.util.List;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.model.constraint.DeferrabilityModel.InitiallyCheckTime;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel.MatchType;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel.ReferentialAction;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.model.index.IndexColumnModel.SortOrder;
import org.jiemamy.model.index.IndexModel;
import org.jiemamy.model.sql.Keyword;
import org.jiemamy.model.sql.Literal;
import org.jiemamy.model.sql.Separator;
import org.jiemamy.model.sql.Token;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.model.view.ViewModel;

/**
 * {@link TokenResolver}の標準実装クラス。
 * 
 * @author daisuke
 */
public class DefaultTokenResolver implements TokenResolver {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultTokenResolver.class);
	

	public List<Token> resolve(Object value) {
		List<Token> tokens = Lists.newArrayListWithCapacity(2);
		if (value instanceof InitiallyCheckTime) {
			InitiallyCheckTime initiallyCheckTime = (InitiallyCheckTime) value;
			if (initiallyCheckTime == InitiallyCheckTime.IMMEDIATE) {
				tokens.add(Keyword.INITIALLY);
				tokens.add(Keyword.IMMEDIATE);
			} else if (initiallyCheckTime == InitiallyCheckTime.DEFERRED) {
				tokens.add(Keyword.INITIALLY);
				tokens.add(Keyword.DEFERRED);
			}
		} else if (value instanceof MatchType) {
			MatchType matchType = (MatchType) value;
			if (matchType == MatchType.SIMPLE) {
				tokens.add(Keyword.MATCH);
				tokens.add(Keyword.SIMPLE);
			} else if (matchType == MatchType.FULL) {
				tokens.add(Keyword.MATCH);
				tokens.add(Keyword.FULL);
			} else if (matchType == MatchType.PARTIAL) {
				tokens.add(Keyword.MATCH);
				tokens.add(Keyword.PARTIAL);
			}
		} else if (value instanceof ReferentialAction) {
			ReferentialAction referentialAction = (ReferentialAction) value;
			if (referentialAction == ReferentialAction.CASCADE) {
				tokens.add(Keyword.CASCADE);
			} else if (referentialAction == ReferentialAction.SET_NULL) {
				tokens.add(Keyword.SET);
				tokens.add(Keyword.NULL);
			} else if (referentialAction == ReferentialAction.SET_DEFAULT) {
				tokens.add(Keyword.SET);
				tokens.add(Keyword.DEFAULT);
			} else if (referentialAction == ReferentialAction.RESTRICT) {
				tokens.add(Keyword.RESTRICT);
			} else if (referentialAction == ReferentialAction.NO_ACTION) {
				tokens.add(Keyword.NO);
				tokens.add(Keyword.ACTION);
			}
		} else if (value instanceof SortOrder) {
			SortOrder sortOrder = (SortOrder) value;
			if (sortOrder == SortOrder.ASC) {
				tokens.add(Keyword.ASC);
			} else if (sortOrder == SortOrder.DESC) {
				tokens.add(Keyword.DESC);
			}
		} else if (value instanceof TableModel) {
			tokens.add(Keyword.TABLE);
		} else if (value instanceof ViewModel) {
			tokens.add(Keyword.VIEW);
		} else if (value instanceof IndexModel) {
			tokens.add(Keyword.INDEX);
		} else if (value instanceof TypeVariant) {
			tokens.addAll(resolveType((TypeVariant) value));
		} else {
			logger.warn("unknown object: " + value.getClass());
		}
		return tokens;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param type
	 * @return
	 */
	protected List<Token> resolveType(TypeVariant type) {
		List<Token> result = Lists.newArrayList();
		TypeReference typeReference = type.getTypeReference();
		result.add(Keyword.of(typeReference.getTypeName()));
		if (type.getParam(TypeParameterKey.SIZE) != null) {
			result.add(Separator.LEFT_PAREN);
			result.add(Literal.of(type.getParam(TypeParameterKey.SIZE)));
			result.add(Separator.RIGHT_PAREN);
		} else if (type.getParam(TypeParameterKey.PRECISION) != null) {
			result.add(Separator.LEFT_PAREN);
			result.add(Literal.of(type.getParam(TypeParameterKey.PRECISION)));
			if (type.getParam(TypeParameterKey.SCALE) != null) {
				result.add(Separator.COMMA);
				result.add(Literal.of(type.getParam(TypeParameterKey.SCALE)));
			}
			result.add(Separator.RIGHT_PAREN);
		} else if (type.getParam(TypeParameterKey.WITH_TIMEZONE) != null) {
			if (type.getParam(TypeParameterKey.WITH_TIMEZONE)) {
				result.add(Keyword.WITH);
			} else {
				result.add(Keyword.WITHOUT);
			}
			result.add(Keyword.TIMEZONE);
		}
		
		return result;
	}
	
}
