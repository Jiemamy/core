/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/26
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
package org.jiemamy.validator.impl;

import java.util.Collection;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.EntityNotFoundException;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * {@link EntityRef}が全て正常に解決できるかどうか調べるバリデータ。
 * 
 * <ul>
 *   <li>モデルに含まれる {@link EntityRef}は、そのモデルが属する{@link JiemamyContext}で全て解決できなければならない。</li>
 * </li>
 * 
 * @author daisuke
 */
public class ReferenceValidator extends AbstractValidator {
	
	private static Logger logger = LoggerFactory.getLogger(ReferenceValidator.class);
	

	public Collection<? extends Problem> validate(JiemamyContext context) {
		Validate.notNull(context);
		Collection<Problem> problems = Lists.newArrayList();
		// TODO
		return problems;
	}
	
	void check(JiemamyContext context, EntityRef<?> elementReference, String pos, Collection<Problem> problems) {
		try {
			context.resolve(elementReference);
		} catch (EntityNotFoundException e) {
			logger.error("reference(" + pos + ") resolve error");
			problems.add(new ReferenceProblem(elementReference, pos));
		}
	}
	

	static class ReferenceProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * @param pos 現在位置を表す文字列
		 * @param elementReference 参照の切れた参照オブジェクト
		 */
		private ReferenceProblem(EntityRef<?> elementReference, String pos) {
			super(null, "F0040");
			setArguments(new Object[] {
				pos,
				elementReference.getReferentId().toString()
			});
		}
	}
	
}
