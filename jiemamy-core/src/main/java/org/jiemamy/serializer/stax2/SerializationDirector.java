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

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.DefaultContextMetadata;
import org.jiemamy.DefaultContextMetadataSerializationHandler;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextSerializationHandler;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.model.column.DefaultColumnModel;
import org.jiemamy.model.column.DefaultColumnModelSerializationHandler;
import org.jiemamy.model.constraint.DefaultCheckConstraintModel;
import org.jiemamy.model.constraint.DefaultCheckConstraintModelSerializationHandler;
import org.jiemamy.model.constraint.DefaultDeferrabilityModel;
import org.jiemamy.model.constraint.DefaultDeferrabilityModelSerializationHandler;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultForeignKeyConstraintModelSerializationHandler;
import org.jiemamy.model.constraint.DefaultNotNullConstraintModel;
import org.jiemamy.model.constraint.DefaultNotNullConstraintModelSerializationHandler;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultPrimaryKeyConstraintModelSerializationHandler;
import org.jiemamy.model.constraint.DefaultUniqueKeyConstraintModel;
import org.jiemamy.model.constraint.DefaultUniqueKeyConstraintModelSerializationHandler;
import org.jiemamy.model.dataset.DefaultDataSetModel;
import org.jiemamy.model.dataset.DefaultDataSetModelSerializationHandler;
import org.jiemamy.model.dataset.DefaultRecordModel;
import org.jiemamy.model.dataset.DefaultRecordModelSerializationHandler;
import org.jiemamy.model.datatype.DefaultDataType;
import org.jiemamy.model.datatype.DefaultDataTypeSerializationHandler;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.DefaultTableModelSerializationHandler;
import org.jiemamy.model.view.DefaultViewModel;
import org.jiemamy.model.view.DefaultViewModelSerializationHandler;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.JiemamyQName;

/**
 * シリアライズ・デシリアライズ処理の指揮をとり、各 {@link SerializationHandler} に処理を委譲するクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SerializationDirector {
	
	private static Logger logger = LoggerFactory.getLogger(SerializationDirector.class);
	
	final Map<String, SerializationHandler<?>> handlersWithFqcnKey = Maps.newHashMap();
	
	final Map<QName, SerializationHandler<?>> handlersWithQNameKey = Maps.newHashMap();
	
	private final DummyHandler dummy;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SerializationDirector(JiemamyContext context) {
		Validate.notNull(context);
		
		dummy = new DummyHandler(this); // これがケツ持ちをしてる
		
		addHandler(JiemamyContext.class, CoreQName.JIEMAMY, new JiemamyContextSerializationHandler(this));
		addHandler(DefaultContextMetadata.class, CoreQName.META, new DefaultContextMetadataSerializationHandler(this));
		addHandler(DefaultViewModel.class, CoreQName.VIEW, new DefaultViewModelSerializationHandler(this));
		addHandler(DefaultTableModel.class, CoreQName.TABLE, new DefaultTableModelSerializationHandler(this));
		addHandler(DefaultColumnModel.class, CoreQName.COLUMN, new DefaultColumnModelSerializationHandler(this));
		addHandler(DefaultDataType.class, CoreQName.DATA_TYPE, new DefaultDataTypeSerializationHandler(this));
		addHandler(DefaultNotNullConstraintModel.class, CoreQName.NOT_NULL,
				new DefaultNotNullConstraintModelSerializationHandler(this));
		addHandler(DefaultPrimaryKeyConstraintModel.class, CoreQName.PRIMARY_KEY,
				new DefaultPrimaryKeyConstraintModelSerializationHandler(this));
		addHandler(DefaultForeignKeyConstraintModel.class, CoreQName.FOREIGN_KEY,
				new DefaultForeignKeyConstraintModelSerializationHandler(this));
		addHandler(DefaultUniqueKeyConstraintModel.class, CoreQName.UNIQUE_KEY,
				new DefaultUniqueKeyConstraintModelSerializationHandler(this));
		addHandler(DefaultCheckConstraintModel.class, CoreQName.CHECK,
				new DefaultCheckConstraintModelSerializationHandler(this));
		addHandler(DefaultDeferrabilityModel.class, CoreQName.DEFERRABILITY,
				new DefaultDeferrabilityModelSerializationHandler(this));
		
		addHandler(DefaultDataSetModel.class, CoreQName.DATASET, new DefaultDataSetModelSerializationHandler(this));
		addHandler(DefaultRecordModel.class, CoreQName.RECORDS, new DefaultRecordModelSerializationHandler(this));
		
		for (JiemamyFacet jiemamyFacet : context.getFacets()) {
			jiemamyFacet.prepareSerializationHandlers(this);
		}
	}
	
	public <T>void addHandler(Class<T> clazz, JiemamyQName jQName, SerializationHandler<T> handler) {
		Validate.notNull(clazz);
		Validate.notNull(jQName);
		Validate.notNull(handler);
		handlersWithFqcnKey.put(clazz.getName(), handler);
		handlersWithQNameKey.put(jQName.getQName(), handler);
	}
	
	public <T>T direct(DeserializationContext ctx) throws SerializationException {
		Validate.notNull(ctx);
		SerializationHandler<T> handler = findHandler(ctx.peek());
		return handler.handleDeserialization(ctx);
	}
	
	public <T>void direct(T target, SerializationContext sctx) throws SerializationException {
		Validate.notNull(target);
		Validate.notNull(sctx);
		SerializationHandler<T> handler = findHandler(target);
		handler.handleSerialization(target, sctx);
	}
	
	@SuppressWarnings("unchecked")
	private <T>SerializationHandler<T> findHandler(JiemamyCursor cursor) throws SerializationException {
		Validate.notNull(cursor);
		
		try {
			QName qName = cursor.getQName();
			
			if (cursor.hasAttr(CoreQName.CLASS)) {
				String className = cursor.getAttrValue(CoreQName.CLASS);
				return findHandler(className);
			}
			
			SerializationHandler<T> handler = (SerializationHandler<T>) handlersWithQNameKey.get(qName);
			
			if (handler == null) {
				// 例えば JiemamyContext が DiagramFacet を持っていないのに<diagram:*>要素が来た時など、このハンドラが相手をする。
				logger.warn("can not found deserialization handler : " + qName);
				handler = (SerializationHandler<T>) dummy;
			}
			
			return handler;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T>SerializationHandler<T> findHandler(String targetName) {
		Validate.notNull(targetName);
		SerializationHandler<T> handler = (SerializationHandler<T>) handlersWithFqcnKey.get(targetName);
		
		if (handler == null) {
			logger.warn("can not found serialization handler : " + targetName);
			handler = (SerializationHandler<T>) dummy;
		}
		
		return handler;
	}
	
	private <T>SerializationHandler<T> findHandler(T target) {
		Validate.notNull(target);
		return findHandler(target.getClass().getName());
	}
}
