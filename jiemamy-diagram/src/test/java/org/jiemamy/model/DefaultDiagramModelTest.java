/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
import java.util.UUID;

import org.junit.Test;

import org.jiemamy.model.table.TableModel;

/**
 * {@link DefaultDiagramModel}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultDiagramModelTest {
	
	/**
	 * 適当な {@link DefaultDiagramModel} のインスタンスを作る。
	 * 
	 * @param tables 表示候補の {@link TableModel} の集合
	 * @return {@link DefaultDiagramModel}
	 */
	public static DefaultDiagramModel random(Collection<TableModel> tables) {
		DefaultDiagramModel model = new DefaultDiagramModel(UUID.randomUUID());
		model.setLevel(enume(Level.class));
		model.setMode(enume(Mode.class));
		model.setName(strNullable());
		
		if (tables.size() > 1) {
			int count = integer(tables.size() - 1) + 1;
			Iterator<TableModel> itr = tables.iterator();
			for (int i = 0; i < count; i++) {
				model.store(DefaultDatabaseObjectNodeModelTest.random(itr.next()));
			}
		}
		int stickNodes = integer(5);
		for (int i = 0; i < stickNodes; i++) {
			model.store(StickyNodeModelTest.random());
		}
		return model;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		
	}
}
