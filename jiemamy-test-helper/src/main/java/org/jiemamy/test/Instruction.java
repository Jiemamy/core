/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/23
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
package org.jiemamy.test;

/**
 * {@link CoreTestModelBuilder}等に対する指示クラス。
 * 
 * @author daisuke
 */
public class Instruction {
	
	/** ドメインを使用しない場合は{@code true}、そうでない場合は{@code false}を設定する */
	public boolean supressUseDomain;
	
	/** 外部キーを使用しない場合は{@code true}、そうでない場合は{@code false}を設定する */
	public boolean supressUseForeignKey;
	
	/** DataSetを使用しない場合は{@code true}、そうでない場合は{@code false}を設定する */
	public boolean supressUseDataSet;
	
}
