/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/31
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

import javax.xml.stream.XMLStreamException;

import org.jiemamy.JiemamyContext;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.serializer.stax2.DeserializationContext;
import org.jiemamy.serializer.stax2.JiemamyOutputContainer;
import org.jiemamy.serializer.stax2.JiemamyOutputElement;
import org.jiemamy.serializer.stax2.SerializationContext;
import org.jiemamy.serializer.stax2.SerializationDirector;
import org.jiemamy.serializer.stax2.SerializationHandler;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.DiagramQName;

/**
 * {@link DefaultDiagramModel}のシリアライズ処理実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DefaultNodeModelSerializationHandler extends SerializationHandler<DefaultNodeModel> {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param director 親となるディレクタ
	 */
	public DefaultNodeModelSerializationHandler(JiemamyContext context, SerializationDirector director) {
//		super(DefaultNodeModel.class, DiagramQName.NODE, director);
		super(director);
	}
	
	@Override
	public void handle(DefaultNodeModel model, SerializationContext sctx) throws SerializationException {
		JiemamyOutputContainer parent = sctx.peek();
		try {
			JiemamyOutputElement diagram = parent.addElement(DiagramQName.DIAGRAM);
			
			diagram.addElement(DiagramQName.CORE).addAttribute(CoreQName.REF, model.getCoreModelRef().getReferentId());
			
			sctx.push(diagram);
			getDirector().direct(model.getBoundary(), sctx);
			if (model.getColor() != null) {
				getDirector().direct(model.getColor(), sctx);
			}
			for (ConnectionModel connectionModel : model.getSourceConnections()) {
				getDirector().direct(connectionModel, sctx);
			}
			sctx.pop();
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@Override
	public DefaultNodeModel handle(DeserializationContext ctx) throws SerializationException {
		// TODO Auto-generated method stub
		return null;
	}
}
