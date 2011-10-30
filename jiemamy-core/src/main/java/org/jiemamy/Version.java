/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/03/03
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

import org.apache.commons.lang.Validate;

/**
 * Jiemamyのバージョンを表すインターフェイス。
 * 
 * @since 0.3
 * @author daisuke
 */
public class Version {
	
	static final Version INSTANCE = new Version(0, 3, 4, false);
	
	
	/**
	 * 文字列をパースして、バージョンオブジェクトを生成する。
	 * 
	 * @param versionString バージョン文字列
	 * @return バージョンオブジェクト
	 * @throws IllegalArgumentException 引数がバージョン番号として無効な場合
	 * @since 0.3
	 */
	public static Version parse(String versionString) {
		int major = 0;
		int minor = 0;
		int release = 0;
		boolean snapshot = false;
		if (versionString.endsWith("-SNAPSHOT")) {
			snapshot = true;
			versionString = versionString.substring(0, versionString.length() - "-SNAPSHOT".length()); // CHECKSTYLE IGNORE THIS LINE
		}
		
		String[] elements = versionString.split("[\\.]");
		try {
			switch (elements.length) {
				case 3:
					release = Integer.parseInt(elements[2]);
					// $FALL-THROUGH$
					
				case 2:
					major = Integer.parseInt(elements[0]);
					minor = Integer.parseInt(elements[1]);
					break;
				
				default:
					throw new IllegalArgumentException(versionString);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(versionString, e);
		}
		
		return new Version(major, minor, release, snapshot);
	}
	
	
	private final int major;
	
	private final int minor;
	
	private final int release;
	
	private final boolean snapshot;
	
	
	Version(int major, int minor, int release, boolean snapshot) {
		this.major = major;
		this.minor = minor;
		this.release = release;
		this.snapshot = snapshot;
	}
	
	/**
	 * このバージョンの実装が、指定したバージョンのXMLを読み込めるかどうかを調べる。
	 * 
	 * @param target 読み込むXMLのバージョン
	 * @return 読み込み可能な場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	public boolean canDeserialize(Version target) {
		Validate.notNull(target);
		if (major != target.getMajor()) {
			return false;
		}
		if (minor < target.getMinor()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 引数{@code obj}がVersionであり、全てのプロパティが一致するかどうかを調べる。
	 * 
	 * @param obj 比較対象オブジェクト
	 * @return 全てのプロパティが一致した場合は{@code true}、そうでない場合は{@code false}
	 * @since 0.3
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (Version.class.isAssignableFrom(obj.getClass()) == false) {
			return false;
		}
		Version other = (Version) obj;
		if (major != other.getMajor()) {
			return false;
		}
		if (minor != other.getMinor()) {
			return false;
		}
		if (release != other.getRelease()) {
			return false;
		}
		if (snapshot != other.isSnapshot()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 仕様及び実装のメジャーバージョン番号を取得する。
	 * 
	 * @return 仕様及び実装のメジャーバージョン番号
	 * @since 0.3
	 */
	public int getMajor() {
		return major;
	}
	
	/**
	 * 仕様及び実装のマイナーバージョン番号を取得する。
	 * 
	 * @return 仕様及び実装のマイナーバージョン番号
	 * @since 0.3
	 */
	public int getMinor() {
		return minor;
	}
	
	/**
	 * 実装のリリース番号を取得する。
	 * 
	 * @return 実装のリリース番号
	 * @since 0.3
	 */
	public int getRelease() {
		return release;
	}
	
	/**
	 * ハッシュコード値を取得する。
	 * 
	 * <p>ハッシュコード値は、以下の式で求める。</p>
	 * 
	 * <pre>
	 * (((31 + major) * 31 + minor) * 31 + release) * 31 + (snapshot ? 1231 : 1237)
	 * </pre>
	 * 
	 * @return ハッシュコード値
	 * @since 0.3
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
		result = prime * result + release;
		result = prime * result + (snapshot ? 1231 : 1237); // CHECKSTYLE IGNORE THIS LINE
		return result;
	}
	
	/**
	 * スナップショット版であるかどうかを取得する。
	 * 
	 * <p>仕様または実装のいずれかがスナップショット版であれば、{@code true}を返す。両者とも正式版の時のみ、{@code false}を返す。</p>
	 * 
	 * @return スナップショット版であれば{@code true}
	 * @since 0.3
	 */
	public boolean isSnapshot() {
		return snapshot;
	}
	
	/**
	 * 実装バージョン表記文字列に変換する。
	 * 
	 * @return バージョン表記文字列
	 * @since 0.3
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(major);
		sb.append(".");
		sb.append(minor);
		sb.append(".");
		sb.append(release);
		if (snapshot) {
			sb.append("-SNAPSHOT");
		}
		return sb.toString();
	}
	
	/**
	 * 仕様バージョン表記文字列に変換する。
	 * 
	 * <p>SNAPSHOTであっても、末尾に"-SNAPSHOT"はつかない。</p>
	 * 
	 * @return バージョン表記文字列
	 * @since 0.3
	 */
	public String toStringSpec() {
		StringBuilder sb = new StringBuilder();
		sb.append(major);
		sb.append(".");
		sb.append(minor);
		return sb.toString();
	}
}
