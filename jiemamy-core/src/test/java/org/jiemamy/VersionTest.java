/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/09/15
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * {@link Version}のテストクラス。
 * 
 * @since 0.3
 * @author daisuke
 */
public class VersionTest {
	
	/**
	 * 文字列を意図通りにパースでき_デシリアライズ互換性をチェックできる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_文字列を意図通りにパースでき_デシリアライズ互換性をチェックできる() throws Exception {
		Version v1 = Version.parse("9.8.7");
		assertThat(v1.getMajor(), is(9));
		assertThat(v1.getMinor(), is(8));
		assertThat(v1.getRelease(), is(7));
		assertThat(v1.isSnapshot(), is(false));
		assertThat(v1.toString(), is("9.8.7"));
		assertThat(v1.toStringSpec(), is("9.8"));
		assertThat(v1.canDeserialize(v1), is(true)); // 9.8.7 は 9.8 データを読める（完全一致）
		
		Version v2 = Version.parse("10.11.12-SNAPSHOT");
		assertThat(v2.getMajor(), is(10));
		assertThat(v2.getMinor(), is(11));
		assertThat(v2.getRelease(), is(12));
		assertThat(v2.isSnapshot(), is(true));
		assertThat(v2.toString(), is("10.11.12-SNAPSHOT"));
		assertThat(v2.toStringSpec(), is("10.11"));
		assertThat(v2.canDeserialize(v2), is(true)); // 10.11.12-SNAPSHOT は 10.11 データを読める（完全一致）
		assertThat(v2.canDeserialize(v1), is(false)); // 10.11.12-SNAPSHOT は 9.8.7 データを読めない（Major-verUpで互換完全喪失）
		assertThat(v1.canDeserialize(v2), is(false)); // 9.8.7 は 10.11 データを読めない（Major-verUpで互換喪失）
		
		Version v3 = Version.parse("9.7");
		assertThat(v3.getMajor(), is(9));
		assertThat(v3.getMinor(), is(7));
		assertThat(v3.getRelease(), is(0));
		assertThat(v3.isSnapshot(), is(false));
		assertThat(v3.toString(), is("9.7.0"));
		assertThat(v3.toStringSpec(), is("9.7"));
		assertThat(v3.canDeserialize(v3), is(true)); // 9.7 は 9.7 データを読める（完全一致）
		assertThat(v3.canDeserialize(v2), is(false)); // 9.7 は 10.11 データを読めない（Major-verUpで互換完全喪失）
		assertThat(v3.canDeserialize(v1), is(false)); // 9.7 は 9.8 データを読めない（上位互換はあるが下位互換はない）
		assertThat(v2.canDeserialize(v3), is(false)); // 10.11.12-SNAPSHOT は 9.7 データを読めない（Major-verUpで互換完全喪失）
		assertThat(v1.canDeserialize(v3), is(true)); // 9.8.7 は 9.7 データを読める（上位互換あり）
		
		Version v4 = Version.parse("9.8-SNAPSHOT");
		assertThat(v4.getMajor(), is(9));
		assertThat(v4.getMinor(), is(8));
		assertThat(v4.getRelease(), is(0));
		assertThat(v4.isSnapshot(), is(true));
		assertThat(v4.toString(), is("9.8.0-SNAPSHOT"));
		assertThat(v4.toStringSpec(), is("9.8"));
		assertThat(v4.canDeserialize(v4), is(true)); // 9.8-SNAPSHOT は 9.8 データを読める（完全一致）
		assertThat(v4.canDeserialize(v3), is(true)); // 9.8-SNAPSHOT は 9.7 データを読める（上位互換あり）
		assertThat(v4.canDeserialize(v2), is(false)); // 9.8-SNAPSHOT は 10.11 データを読めない（Major-verUpで互換完全喪失）
		assertThat(v4.canDeserialize(v1), is(true)); // 9.8-SNAPSHOT は 9.8 データを読める（上位互換あり） …微妙？
		assertThat(v3.canDeserialize(v4), is(false)); // 9.7 は 9.8 データを読めない（上位互換はあるが下位互換はない）
		assertThat(v2.canDeserialize(v4), is(false)); // 10.11.12-SNAPSHOT は 9.8 データを読めない（Major-verUpで互換完全喪失）
		assertThat(v1.canDeserialize(v4), is(true)); // 9.8.7 は 9.8 データを読める（互換あり）
		
	}
	
	/**
	 * 不正な文字列をパースする場合の例外確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_不正な文字列をパースする場合の例外確認() throws Exception {
		try {
			Version.parse("10");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		try {
			Version.parse("10.11.12.13");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		try {
			Version.parse("10.11.12-HOGE");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	/**
	 * {@link #equals(Object)}と {@link #hashCode()}が仕様に従って実装されていること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_equalsとhashCodeが仕様に従って実装されていること() throws Exception {
		Version v1 = new Version(1, 2, 3, false);
		Version v2 = new Version(1, 2, 3, false);
		Version v3 = new Version(1, 2, 4, false);
		Version v4 = new OtherVersionImpl(1, 2, 3, false);
		Version v5 = new Version(1, 3, 3, false);
		Version v6 = new Version(2, 2, 3, false);
		Version v7 = new Version(1, 2, 3, true);
		
		// equalsのチェック 兼 推移性のチェック
		assertThat(v1.equals(v2), is(true));
		assertThat(v1.equals(v3), is(false));
		assertThat(v1.equals(v4), is(true));
		assertThat(v2.equals(v3), is(false));
		assertThat(v2.equals(v4), is(true));
		assertThat(v3.equals(v4), is(false));
		assertThat(v1.equals(v5), is(false));
		assertThat(v1.equals(v6), is(false));
		assertThat(v1.equals(v7), is(false));
		assertThat(v1.equals(null), is(false));
		assertThat(v2.equals(null), is(false));
		assertThat(v3.equals(null), is(false));
		assertThat(v4.equals(null), is(false));
		assertThat(v4.equals(new Object()), is(false));
		
		assertThat(v1.hashCode(), is(v2.hashCode()));
		assertThat(v1.hashCode(), is(not(v3.hashCode())));
		assertThat(v1.hashCode(), is(v4.hashCode()));
		assertThat(v2.hashCode(), is(not(v3.hashCode())));
		assertThat(v2.hashCode(), is(v4.hashCode()));
		assertThat(v3.hashCode(), is(not(v4.hashCode())));
		assertThat(v1.hashCode(), is(not(v7.hashCode())));
		
		List<Version> list = Arrays.asList(v1, v2, v3, v4);
		for (Version version : list) {
			
			// 反射性
			assertThat(version.equals(version), is(true));
			
			// 対称性
			for (Version version2 : list) {
				assertThat(version.equals(version2), is(version2.equals(version)));
			}
		}
	}
	

	private static final class OtherVersionImpl extends Version {
		
		Version delegate;
		

		OtherVersionImpl(int major, int minor, int release, boolean snapshot) {
			super(0, 0, 0, false);
			delegate = new Version(major, minor, release, snapshot);
		}
		
		@Override
		public boolean canDeserialize(Version target) {
			return delegate.canDeserialize(target);
		}
		
		@Override
		public boolean equals(Object obj) {
			return delegate.equals(obj);
		}
		
		@Override
		public int getMajor() {
			return delegate.getMajor();
		}
		
		@Override
		public int getMinor() {
			return delegate.getMinor();
		}
		
		@Override
		public int getRelease() {
			return delegate.getRelease();
		}
		
		@Override
		public int hashCode() {
			return delegate.hashCode();
		}
		
		@Override
		public boolean isSnapshot() {
			return delegate.isSnapshot();
		}
		
		@Override
		public String toString() {
			return delegate.toString();
		}
		
		@Override
		public String toStringSpec() {
			return delegate.toStringSpec();
		}
	}
}
