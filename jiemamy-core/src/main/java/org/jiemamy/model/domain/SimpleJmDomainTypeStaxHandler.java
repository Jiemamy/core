/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.model.domain;

import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMEvent;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.domain.SimpleJmDomain.DomainType;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax.DeserializationContext;
import org.jiemamy.serializer.stax.JiemamyCursor;
import org.jiemamy.serializer.stax.JiemamyOutputContainer;
import org.jiemamy.serializer.stax.JiemamyOutputElement;
import org.jiemamy.serializer.stax.SerializationContext;
import org.jiemamy.serializer.stax.StaxDirector;
import org.jiemamy.serializer.stax.StaxHandler;
import org.jiemamy.xml.CoreQName;

/**
 * {@link SimpleJmDomain.DomainType}をシリアライズ/デシリアライズするハンドラ。
 * 
 * @version $Id$
 * @author yamkazu
 */
public final class SimpleJmDomainTypeStaxHandler extends StaxHandler<SimpleJmDomain.DomainType> {
	
//	private static Logger logger = LoggerFactory.getLogger(SimpleJmDomainTypeStaxHandler.class);
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param director 親となるディレクタ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SimpleJmDomainTypeStaxHandler(StaxDirector director) {
		super(director);
	}
	
	@Override
	public SimpleJmDomain.DomainType handleDeserialization(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		try {
			Validate.isTrue(dctx.peek().getCurrEvent() == SMEvent.START_ELEMENT);
			Validate.isTrue(dctx.peek().isQName(CoreQName.TYPE_DESC));
			
			JiemamyCursor cursor = dctx.peek();
			
			String idStr = cursor.getAttrValue(CoreQName.REF);
			UUID refid = dctx.getContext().toUUID(idStr);
			
			JiemamyContext context = dctx.getContext();
			SimpleJmDomain domain = (SimpleJmDomain) context.resolve(refid); // 解決出来ない場合EntityNotFoundException
			
			// Handlerのインタフェース縛りなので仕方なくキャスト
			// いい対処方法があったら教えてください
			return (DomainType) domain.asType(context);
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public void handleSerialization(SimpleJmDomain.DomainType model, SerializationContext sctx)
			throws SerializationException {
		Validate.notNull(model);
		Validate.notNull(sctx);
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement element = parent.addElement(CoreQName.TYPE_DESC);
			element.addAttribute(CoreQName.CLASS, model.getClass());
			element.addAttribute(CoreQName.REF, model.getReferentId());
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
}
