/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/05
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
package org.jiemamy.serializer.stax2;

import org.codehaus.staxmate.in.SMHierarchicCursor;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DeserializationContext {
	
	private final JiemamyCursor cursor;
	

	public DeserializationContext(JiemamyCursor cursor) {
		this.cursor = cursor;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param cursor
	 */
	public DeserializationContext(SMHierarchicCursor cursor) {
		this.cursor = new JiemamyCursor(cursor);
	}
	
	/**
	 * somethingを取得する。 TODO for daisuke
	 * @return the cursor
	 */
	public JiemamyCursor getCursor() {
		return cursor;
	}
	
}
