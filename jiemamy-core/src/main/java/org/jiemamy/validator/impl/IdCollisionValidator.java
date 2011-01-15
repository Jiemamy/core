/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/21
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

import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.Problem;

/**
 * モデルIDが衝突しているかどうかを調べるバリデータ。
 * 
 * <ul>
 *   <li>全てのモデルは、IDが重複していてはならない。</li>
 * </uL>
 * 
 * @author daisuke
 */
public class IdCollisionValidator extends AbstractTraversalValidator<UUID> {
	
	private static Logger logger = LoggerFactory.getLogger(IdCollisionValidator.class);
	

	@Override
	void check(Object element, String pos) {
		if (element instanceof Entity) {
			Entity entity = (Entity) element;
			if (ids.containsKey(entity.getId())) {
				result.add(new IdCollisionProblem(entity.getId(), ids.get(entity.getId()), pos));
				logger.warn("collision : " + ArrayUtils.toString(new RuntimeException().getStackTrace()));
			}
			ids.put(entity.getId(), pos);
		}
	}
	

	/**
	 * ID名が衝突していることを示す{@link Problem}オブジェクト。
	 * 
	 * @author daisuke
	 */
	static class IdCollisionProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param id 衝突したID
		 * @param prev 前回見つかった位置を示す文字列
		 * @param current 現在位置を表す文字列
		 */
		public IdCollisionProblem(UUID id, String prev, String current) {
			super("F0010");
			setArguments(new Object[] {
				id,
				prev,
				current
			});
		}
	}
	
}
