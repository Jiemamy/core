/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/20
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
package org.jiemamy.test.mock;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import org.jiemamy.JiemamyFacet;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.OnMemoryEntityResolver;
import org.jiemamy.dddbase.OnMemoryRepository;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.xml.JiemamyNamespace;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public final class MockFacet implements JiemamyFacet {
	
	public Set<? extends Entity> getEntities() {
		return Collections.emptySet();
	}
	
	public JiemamyNamespace[] getNamespaces() {
		return new JiemamyNamespace[0];
	}
	
	public OnMemoryEntityResolver<?> getResolver() {
		return new OnMemoryRepository<Entity>();
	}
	
	public URL getSchema() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void prepareStaxHandlers(StaxDirector staxDirector) {
		// do nothing
	}
}
