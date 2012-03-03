/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2012/03/03
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

/**
 * 参照列に挿入された値は、被参照テーブルと被参照列の値に対して、指定した照合型で照会される。
 * 
 * <p>照合型には3種類があり、デフォルトは{@link #SIMPLE}照合型。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public enum MatchType {
	
	/** 外部キーの他の部分がNULLでない限り、外部キーの一部をNULLとなることを許可する。 */
	SIMPLE,
	
	/** 全ての外部キー列がNULLとなる場合を除き、複数列外部キーのある列がNULLとなることを許可しない。 */
	FULL,
	
	/** まだ実装されていない。 */
	PARTIAL;
}