/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/19
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
package org.jiemamy.utils;

import org.apache.commons.lang.Validate;

import org.jiemamy.ServiceLocator;

/**
 * 複数の {@link ServiceLocator} を内包し、1つの {@link ServiceLocator} に見せるクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class CompositeServiceLocator implements ServiceLocator {
	
	private final ServiceLocator[] locators;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param locators リゾルバ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public CompositeServiceLocator(ServiceLocator... locators) {
		Validate.noNullElements(locators);
		this.locators = locators;
	}
	
	public <T>T getService(Class<T> clazz, String fqcn) throws ClassNotFoundException {
		for (ServiceLocator locator : locators) {
			T service = null;
			try {
				service = locator.getService(clazz, fqcn);
			} catch (ClassNotFoundException e) {
				// ignore
			}
			if (service != null) {
				return service;
			}
		}
		throw new ClassNotFoundException();
	}
	
}
