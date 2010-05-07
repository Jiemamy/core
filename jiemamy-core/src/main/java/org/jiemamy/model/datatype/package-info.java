/**
 * Jiemamyモデルのうち、主に「データ型」を定義するパッケージ。
 * 
 * <p>{@link org.jiemamy.model.datatype.DataType}を基底にして、
 * 組み込みデータ型やドメインなどのサブインターフェイスを定義している。</p>
 * 
 * <p>データ型は、Jiemamyモデルの中で最も複雑な構成となっている。declarationとreferenceの概念について
 * （{@link org.jiemamy.model.Entity}参照）理解した上で把握するとよい。</p>
 * 
 * <p>DB組み込みデータ型がカラムの型として設定される際、組み込みデータ型は定義されるわけではなく、組み込みデータ型を参照するだけで
 * あるため、{@link org.jiemamy.model.attribute.ColumnModel#getDataType()}により取得できるデータ型は、
 * {@link org.jiemamy.model.datatype.BuiltinDataType}となり、この{@link org.jiemamy.model.datatype.BuiltinDataType}が
 * {@link org.jiemamy.model.datatype.DataType}のサブインターフェイスとなっている。 </p>
 * 
 * <p>全体把握の際は、このパッケージの次に{@code org.jiemamy.model.index}パッケージを把握するとよい。</p>
 * 
 * @author daisuke
 * @see org.jiemamy.model.Entity
 * @see org.jiemamy.model.EntityRef
 */
package org.jiemamy.model.datatype;

