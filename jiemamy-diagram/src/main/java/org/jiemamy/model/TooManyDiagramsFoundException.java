/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/05/11
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

/**
 * クエリの結果、該当する {@link JmDiagram} が複数見つかったことを表す例外クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
@SuppressWarnings("serial")
public class TooManyDiagramsFoundException extends ModelConsistencyException {
	
	private final Iterable<JmDiagram> diagrams;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param diagrams 見つかった複数のダイアグラムの集合
	 */
	public TooManyDiagramsFoundException(Iterable<JmDiagram> diagrams) {
		this.diagrams = diagrams;
	}
	
	/**
	 * 見つかったダイアグラムの集合を返す。
	 * 
	 * @return 見つかったダイアグラムの集合
	 */
	public Iterable<JmDiagram> getDiagrams() {
		return diagrams;
	}
}
