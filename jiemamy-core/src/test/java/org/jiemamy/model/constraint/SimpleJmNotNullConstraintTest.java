/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/15
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
package org.jiemamy.model.constraint;

import static org.jiemamy.utils.RandomUtil.enumeNullable;
import static org.jiemamy.utils.RandomUtil.strNullable;

import org.junit.Test;

import org.jiemamy.model.column.JmColumn;

/**
 * {@link SimpleJmNotNullConstraint}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmNotNullConstraintTest {
	
	/**
	 * 適当な {@link SimpleJmNotNullConstraint} のインスタンスを作る。
	 * 
	 * @param column 対象カラム
	 * @return {@link SimpleJmNotNullConstraint}
	 */
	public static SimpleJmNotNullConstraint random(JmColumn column) {
		SimpleJmNotNullConstraint nn = new SimpleJmNotNullConstraint();
		nn.setName(strNullable());
		nn.setLogicalName(strNullable());
		nn.setDeferrability(enumeNullable(SimpleJmDeferrability.class));
		nn.setDescription(strNullable());
		nn.setColumn(column.toReference());
		return nn;
	}
	
	/**
	 * 特にまだテストしたいことはないけどテストメソッドが1つもないとエラーが発生するので仕方なく置いてやってるメソッド。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
	}
}
