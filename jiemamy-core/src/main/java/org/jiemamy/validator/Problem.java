/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import java.util.Locale;

import org.jiemamy.JiemamyContext;

/**
 * バリデータに指摘された問題インターフェイス。
 * 
 * @since 0.2
 * @author daisuke
 */
public interface Problem {
	
	/**
	 * クイックフィックスが可能な問題かどうかを調べる。
	 * 
	 * @return 可能な場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.2
	 */
	boolean canQuickFix();
	
	/**
	 * エラーコードを取得する。
	 * 
	 * <p>{@code null}を返してはならない。</p>
	 * 
	 * @return エラーコード
	 * @since 0.2
	 */
	String getErrorCode();
	
	/**
	 * 指摘事項を説明するメッセージをデフォルトのロケールで取得する。
	 * 
	 * <p>デフォルトロケールにおけるメッセージがなかった場合は、 {@link Locale#US} におけるメッセージを返す。</p>
	 * 
	 * <p>{@code null}を返してはならない。</p>
	 * 
	 * @return 指摘事項を説明するメッセージ
	 * @since 0.2
	 */
	String getMessage();
	
	/**
	 * 指摘事項を説明するメッセージを指定のロケールで取得する。
	 * 
	 * <p>指定ロケールにおけるメッセージがなかった場合は、デフォルトロケール, {@link Locale#US} におけるメッセージを返す。</p>
	 * 
	 * <p>{@code null}を返してはならない。</p>
	 * 
	 * @param locale ロケール
	 * @return 指摘事項を説明するメッセージ
	 * @since 0.2
	 */
	String getMessage(Locale locale);
	
	/**
	 * 問題の重要度を取得する。
	 * 
	 * <p>{@code null}を返してはならない。</p>
	 * 
	 * @return 問題の重要度
	 * @since 0.2
	 */
	Severity getSeverity();
	
	/**
	 * クイックフィックスを施し、問題を自動修正する。
	 * 
	 * @param context コンテキスト
	 * @throws CannotFixException クイックフィックスが不可能な問題である場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void quickFix(JiemamyContext context);
	

	/**
	 * 問題の重要度を表す列挙型。
	 * 
	 * @since 0.2
	 * @author daisuke
	 */
	public enum Severity {
		
		/** 致命的なエラー: 正常にモデルをパースできない・SQLに変換できない状態 */
		FATAL(5),

		/** 一般的なエラー: このままSQL化してもDBがsyntax error等を出すであろう状態 */
		ERROR(4),

		/** 警告: SQL化してDBに適用可能だが、RDB理論的に望ましくない状態 */
		WARN(3),

		/** 通知: RDB理論的に問題はないけど実務的にアレだよね系の状態 */
		NOTICE(2),

		/** 情報: 問題でなないが、何かしらの情報を表現したい場合 */
		INFO(1);
		
		public static Severity fromErrorCode(String code) {
			for (Severity severity : values()) {
				if (code.startsWith(Character.toString(severity.name().charAt(0)))) {
					return severity;
				}
			}
			return null;
		}
		

		/** 重要度を数値化した値 */
		private final int value;
		

		Severity(int value) {
			this.value = value;
		}
		
		/**
		 * 重要度を数値化した値を取得する。
		 * 
		 * @return 重要度を数値化した値
		 * @since 0.2
		 */
		public int getValue() {
			return value;
		}
	}
}
