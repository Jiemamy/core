/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/21
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
package org.jiemamy.validator;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.UUIDEntity;

/**
 * {@link Problem}の骨格実装クラス。
 * 
 * @author daisuke
 */
public abstract class AbstractProblem implements Problem {
	
	static final String BUNDLE_NAME = "org/jiemamy/validator/ErrorMessages";
	
	private Object[] arguments = ArrayUtils.EMPTY_OBJECT_ARRAY;
	
	private final String errorCode;
	
	private final String baseName;
	
	private final UUID targetId;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param target 不正な{@link Entity} may be {@code null}
	 * @param errorCode エラーコード
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractProblem(UUIDEntity target, String errorCode) {
		this(target, errorCode, BUNDLE_NAME, ArrayUtils.EMPTY_OBJECT_ARRAY);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param target 不正な{@link Entity} may be {@code null}
	 * @param errorCode エラーコード
	 * @param arguments メッセージ引数の配列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractProblem(UUIDEntity target, String errorCode, Object[] arguments) {
		this(target, errorCode, BUNDLE_NAME, arguments);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param target 不正な{@link Entity} may be {@code null}
	 * @param errorCode エラーコード
	 * @param baseName {@link ResourceBundle}のベース名
	 * @param arguments メッセージ引数の配列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	AbstractProblem(UUIDEntity target, String errorCode, String baseName, Object[] arguments) {
		Validate.notNull(errorCode);
		Validate.notNull(baseName);
		targetId = target == null ? null : target.getId();
		this.errorCode = errorCode;
		this.baseName = baseName;
		this.arguments = arguments;
	}
	
	public boolean canQuickFix() {
		return false;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public String getMessage() {
		return getMessage(Locale.getDefault());
	}
	
	public String getMessage(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
		return MessageFormat.format(bundle.getString(errorCode), arguments);
//		return String.format(bundle.getString(errorCode), arguments);
	}
	
	public Severity getSeverity() {
		return Severity.fromErrorCode(errorCode);
	}
	
	public UUID getTargetId() {
		return targetId;
	}
	
	public void quickFix(JiemamyContext context) {
		throw new CannotFixException();
	}
	
	@Override
	public String toString() {
		return errorCode + " " + getMessage();
	}
}
