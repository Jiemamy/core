/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.xml.stream.XMLEventWriter;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.Entity;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyError;
import org.jiemamy.serializer.JiemamyXmlWriter;

/**
 * {@link Entity}の骨格実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractEntity implements Entity {
	
	private final UUID id;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractEntity.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public AbstractEntity(UUID id) {
		Validate.notNull(id);
		this.id = id;
	}
	
	@Override
	public AbstractEntity clone() {
		try {
			return (AbstractEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new JiemamyError("clone not supported", e);
		}
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Entity == false) {
			return false;
		}
		return id.equals(((Entity) obj).getId());
	}
	
	public final UUID getId() {
		return id;
	}
	
	public Collection<? extends Entity> getSubEntities() {
		return Collections.emptyList();
	}
	
	public JiemamyXmlWriter getWriter(JiemamyContext context) { // FIXME このメソッド消しちゃえ
		return new JiemamyXmlWriter() {
			
			public void writeTo(XMLEventWriter writer) {
				logger.error("EMPTY WRITER");
			}
		};
	}
	
	@Override
	public final int hashCode() {
		return id.hashCode();
	}
}
