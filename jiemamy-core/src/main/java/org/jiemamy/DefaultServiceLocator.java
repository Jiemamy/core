/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/08
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
package org.jiemamy;

import java.util.Iterator;

import javax.imageio.spi.ServiceRegistry;

import org.apache.commons.lang.Validate;

/**
 * デフォルトの {@link ServiceLocator} 実装クラス。
 * 
 * <p><a href="http://java.sun.com/j2se/1.3/ja/docs/ja/guide/jar/jar.html">JARファイル仕様</a>の
 * サービスプロバイダの仕様に従って、サービスを読み込む。サービスが見つからなかった場合は、クラスローダからインスタンスの生成を試みる。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public class DefaultServiceLocator implements ServiceLocator {
	
	public <T>T getService(Class<T> clazz, String fqcn) throws ClassNotFoundException {
		Validate.notNull(clazz);
		Validate.notNull(fqcn);
		
		Iterator<T> providers = ServiceRegistry.lookupProviders(clazz);
		while (providers.hasNext()) {
			T serviceProvider = providers.next();
			if (serviceProvider.getClass().getName().equals(fqcn)) {
				return serviceProvider;
			}
		}
		
		// FIXME テストクラスから実行されると、サービスロケータによってserviceを読み込めない。
		// このtryブロックがなければMySqlEmitterTestがコケる。なぜだろうか。
		// (v0.2で確認した事象。v0.3以降では未検証)
		try {
			@SuppressWarnings("unchecked")
			T result = (T) Class.forName(fqcn).newInstance();
			return result;
		} catch (InstantiationException e) {
			// ignore
		} catch (IllegalAccessException e) {
			// ignore
		}
		
		throw new ClassNotFoundException(fqcn);
	}
}
