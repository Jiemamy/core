/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.EntityRef;
import org.jiemamy.JiemamyContext;
import org.jiemamy.validator.AbstractProblem;

/**
 * {@link EntityRef}が全て正常に解決できるかどうか調べるバリデータ。
 * 
 * <ul>
 *   <li>モデルに含まれる {@link EntityRef}は、そのモデルが属する{@link JiemamyContext}で全て解決できなければならない。</li>
 * </li>
 * 
 * @author daisuke
 */
public class ReferenceValidator extends AbstractTraversalValidator<Void> {
	
	private static Logger logger = LoggerFactory.getLogger(ReferenceValidator.class);
	

	@Override
	void check(Object element, String pos) {
		if (element instanceof EntityRef<?>) {
			EntityRef<?> elementReference = (EntityRef<?>) element;
			if (context.resolve(elementReference) == null) {
				logger.error("reference(" + pos + ") resolve error");
				result.add(new ReferenceProblem(elementReference, pos));
				
			}
		}
	}
	

	static class ReferenceProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * @param pos 現在位置を表す文字列
		 * @param elementReference 参照の切れた参照オブジェクト
		 */
		private ReferenceProblem(EntityRef<?> elementReference, String pos) {
			super("F0040");
			setArguments(new Object[] {
				pos,
				elementReference.getReferentId().toString()
			});
		}
	}
	
}