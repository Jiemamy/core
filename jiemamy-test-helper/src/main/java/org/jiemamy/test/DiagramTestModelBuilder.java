/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/22
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

import java.util.List;

import org.jiemamy.DiagramFacet;
import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DbObjectNode;
import org.jiemamy.model.JmConnection;
import org.jiemamy.model.JmDiagram;
import org.jiemamy.model.JmStickyNode;
import org.jiemamy.model.geometory.JmColor;
import org.jiemamy.model.geometory.JmPoint;
import org.jiemamy.model.geometory.JmRectangle;

/**
 * Jiemamy DIAGRAM仕様範囲を含めたJiemamyテストモデル1（EMP-DEPTテーブル）を組み立てるビルダ。
 * 
 * @author daisuke
 */
public class DiagramTestModelBuilder extends CoreTestModelBuilder {
	
	/** JmStickyNode for Test */
	protected JmStickyNode sticky;
	
	private static final int OFFSET = 50;
	
	
	/**
	 * インスタンスを生成する。
	 */
	public DiagramTestModelBuilder() {
		super();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param instruction 設定オブジェクト
	 */
	public DiagramTestModelBuilder(Instruction instruction) {
		super(instruction);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param instruction 設定オブジェクト
	 * @param jiemamy コンテキスト
	 */
	public DiagramTestModelBuilder(Instruction instruction, JiemamyContext jiemamy) {
		super(instruction, jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param jiemamy コンテキスト
	 */
	public DiagramTestModelBuilder(JiemamyContext jiemamy) {
		super(jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 */
	public DiagramTestModelBuilder(UuidStrategy uuid) {
		super(uuid);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param instruction 設定オブジェクト
	 */
	public DiagramTestModelBuilder(UuidStrategy uuid, Instruction instruction) {
		super(uuid, instruction);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param instruction 設定オブジェクト
	 * @param jiemamy コンテキスト
	 */
	public DiagramTestModelBuilder(UuidStrategy uuid, Instruction instruction, JiemamyContext jiemamy) {
		super(uuid, instruction, jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param jiemamy コンテキスト
	 */
	public DiagramTestModelBuilder(UuidStrategy uuid, JiemamyContext jiemamy) {
		super(uuid, jiemamy);
	}
	
	@Override
	public JiemamyContext build() {
		JiemamyContext jiemamy = super.build();
		// HACK ↑がFindBugsに「意味ない代入」って言われるので↓で無意味な呼び出しをしてみている。
		// createPresentations 前にsuper.build() 呼んでおかなきゃいけないっつのに。
		jiemamy.getClass();
		createPresentations();
		
		return jiemamy;
	}
	
	private void createPresentations() {
		if (context.hasFacet(DiagramFacet.class)) {
			DiagramFacet facet = context.getFacet(DiagramFacet.class);
			
			// ダイアグラム表現の生成・追加(1)
			JmDiagram presentation1 = new JmDiagram(uuid.get("1deca0e8-6153-47ad-abe8-ac764f768d96"));
			presentation1.setName("全部表示する");
			
			DbObjectNode empProfile =
					new DbObjectNode(uuid.get("aa7caa23-7958-4bd0-a356-8f09d4b74f08"), tableEmp.toReference());
			empProfile.setBoundary(new JmRectangle(360, 60)); // CHECKSTYLE IGNORE THIS LINE
			presentation1.store(empProfile);
			
			DbObjectNode deptProfile =
					new DbObjectNode(uuid.get("eb6506ef-4a25-4296-b6d7-c08741f19d5a"), tableDept.toReference());
			deptProfile.setBoundary(new JmRectangle(60, 60)); // CHECKSTYLE IGNORE THIS LINE
			presentation1.store(deptProfile);
			
			DbObjectNode viewProfile =
					new DbObjectNode(uuid.get("2387bfd0-7106-44ad-a34e-24231bbea6d5"), viewHighSal.toReference());
			viewProfile.setBoundary(new JmRectangle(60, 270)); // CHECKSTYLE IGNORE THIS LINE
			presentation1.store(viewProfile);
			
			sticky = new JmStickyNode(uuid.get("43beb884-2562-4480-8030-bb797f701783"));
			sticky.setContents("メモーー");
			sticky.setBoundary(new JmRectangle(360, 270)); // CHECKSTYLE IGNORE THIS LINE
			sticky.setColor(new JmColor(10, 11, 12));
			presentation1.store(sticky);
			
			JmConnection connectionProfile1 =
					new JmConnection(uuid.get("366b547f-a2a6-42b2-a3a4-38745436c425"), fkEmpEmp.toReference());
			List<JmPoint> bendpoints = connectionProfile1.breachEncapsulationOfBendpoints();
			JmRectangle boundary = presentation1.getNodeFor(tableEmp.toReference()).getBoundary();
			bendpoints.add(0, new JmPoint(Math.max(boundary.x - OFFSET, 0), boundary.y));
			bendpoints.add(1, new JmPoint(boundary.x, Math.max(boundary.y - OFFSET, 0)));
			presentation1.store(connectionProfile1);
			
			JmConnection connectionProfile2 =
					new JmConnection(uuid.get("e5b3d709-2aea-4e26-a90c-b8f438d2da9d"), fkEmpDept.toReference());
			presentation1.store(connectionProfile2);
			
			facet.store(presentation1);
			
			// ダイアグラム表現の生成・追加(2)
			JmDiagram presentation2 = new JmDiagram(uuid.get("53c7cdb7-1512-46c7-8ee3-aadec6007896"));
			presentation2.setName("一部表示する");
			
			DbObjectNode nodeProfile1 =
					new DbObjectNode(uuid.get("d71ff015-101c-4669-8745-312d1da34efe"), tableEmp.toReference());
			nodeProfile1.setBoundary(new JmRectangle(60, 60)); // CHECKSTYLE IGNORE THIS LINE
			presentation2.store(nodeProfile1);
			
			DbObjectNode nodeProfile2 =
					new DbObjectNode(uuid.get("2df7718f-d71e-4dbf-8327-e0ad36f03d5d"), viewHighSal.toReference());
			nodeProfile2.setBoundary(new JmRectangle(270, 270)); // CHECKSTYLE IGNORE THIS LINE
			presentation2.store(nodeProfile2);
			
			JmConnection connectionProfile =
					new JmConnection(uuid.get("b615f111-5143-4b12-bfdb-9a06806a9ace"), fkEmpDept.toReference());
			presentation2.store(connectionProfile);
			
			facet.store(presentation2);
		}
	}
}
