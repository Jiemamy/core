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
package org.jiemamy.validator;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dialect.Dialect;

/**
 * 複数のバリデーションを同時に行うための、バリデータの集合クラス。
 * 
 * @author daisuke
 */
public class CompositeValidator extends AbstractValidator {
	
	/** 内蔵されたバリデータのリスト */
	private List<Validator> validators = Lists.newArrayList();
	

	/**
	 * 内蔵されたバリデータのリストを取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link List}を直接操作することで、
	 * このオブジェクトのフィールドとして保持される{@link List}を変更することができる。</p>
	 * 
	 * <p>上記操作は、{@link Dialect}等が独自のバリデーションロジックを追加するためのものであり、
	 * APIユーザにより操作を行うことは想定されていない。</p>
	 * 
	 * @return 内蔵されたバリデータのリスト
	 */
	public List<Validator> getValidators() {
		return validators;
	}
	
	public Collection<? extends Problem> validate(JiemamyContext context) {
		Collection<Problem> result = Lists.newArrayList();
		for (Validator validator : validators) {
			result.addAll(validator.validate(context));
		}
		return result;
	}
	
}
