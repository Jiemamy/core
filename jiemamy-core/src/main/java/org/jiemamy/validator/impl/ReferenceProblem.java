/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/04
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

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.validator.AbstractProblem;

class ReferenceProblem extends AbstractProblem {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param target {@code reference}を持っている {@link Entity}
	 * @param reference 参照の切れた参照オブジェクト
	 */
	ReferenceProblem(Entity target, EntityRef<? extends JmColumn> reference) {
		super(target, "F0190", new Object[] {
			reference.getReferentId().toString()
		});
	}
	
	/**
	 * インスタンスを生成する。
	 * @param pos 現在位置を表す文字列
	 * @param elementReference 参照の切れた参照オブジェクト
	 */
	@Deprecated
	ReferenceProblem(EntityRef<?> elementReference, String pos) {
		super(null, "F0190", new Object[] {
			pos,
			elementReference.getReferentId().toString()
		});
	}
}
