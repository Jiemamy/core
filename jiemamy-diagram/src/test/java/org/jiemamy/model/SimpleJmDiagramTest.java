/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/13
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
package org.jiemamy.model;

import static org.jiemamy.utils.RandomUtil.enume;
import static org.jiemamy.utils.RandomUtil.integer;
import static org.jiemamy.utils.RandomUtil.strNullable;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import org.jiemamy.model.table.JmTable;

/**
 * {@link SimpleJmDiagram}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SimpleJmDiagramTest {
	
	/**
	 * 適当な {@link SimpleJmDiagram} のインスタンスを作る。
	 * 
	 * @param tables 表示候補の {@link JmTable} の集合
	 * @return {@link SimpleJmDiagram}
	 */
	public static SimpleJmDiagram random(Collection<JmTable> tables) {
		SimpleJmDiagram model = new SimpleJmDiagram();
		model.setLevel(enume(Level.class));
		model.setMode(enume(Mode.class));
		model.setName(strNullable());
		
		if (tables.size() > 1) {
			int count = integer(tables.size() - 1) + 1;
			Iterator<JmTable> itr = tables.iterator();
			for (int i = 0; i < count; i++) {
				model.store(SimpleDbObjectNodeTest.random(itr.next()));
			}
		}
		int stickNodes = integer(5);
		for (int i = 0; i < stickNodes; i++) {
			model.store(JmStickyNodeTest.random());
		}
		return model;
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
