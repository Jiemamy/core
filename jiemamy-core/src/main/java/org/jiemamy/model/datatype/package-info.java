/**
 * Jiemamyモデルのうち、主に「データ型」を定義するパッケージ。
 * 
 * <p>{@link org.jiemamy.model.datatype.DataType}を基底にして、
 * 組み込みデータ型やドメインなどのサブインターフェイスを定義している。</p>
 * 
 * <p>データ型は、Jiemamyモデルの中で最も複雑な構成となっている。declarationとreferenceの概念について
 * （{@link org.jiemamy.model.JiemamyElement}参照）理解した上で把握するとよい。</p>
 * 
 * <p>{@link org.jiemamy.model.entity.TableRef}は{@link org.jiemamy.model.entity.TableModel}のreferenceである。
 * これと同じ考え方で、 {@link org.jiemamy.model.datatype.DomainRef}は {@link org.jiemamy.model.datatype.DomainModel}
 * のreferenceである。</p>
 * 
 * <p>DB組み込みデータ型がカラムの型として設定される際、組み込みデータ型は定義されるわけではなく、組み込みデータ型を参照するだけで
 * あるため、{@link org.jiemamy.model.attribute.ColumnModel#getDataType()}により取得できるデータ型は、
 * {@link org.jiemamy.model.datatype.BuiltinDataType}となり、この{@link org.jiemamy.model.datatype.BuiltinDataType}が
 * {@link org.jiemamy.model.datatype.DataType}のサブインターフェイスとなっている。 </p>
 * 
 * <p>ドメインがカラムの型として設定される際、ドメインは定義されるわけではなく、ドメイン定義を参照するだけであるため、
 * {@link org.jiemamy.model.attribute.ColumnModel#getDataType()}により取得できるデータ型は、{@link org.jiemamy.model.datatype.DomainRef}
 * となり、この{@link org.jiemamy.model.datatype.DomainRef}が{@link org.jiemamy.model.datatype.DataType}のサブ
 * インターフェイスとなっている。 </p>
 * 
 * <p>表にまとめると、以下の通り</p>
 * 
 * <table border>
 *   <tr>
 *     <th>概念</th>
 *     <th>宣言型 declaration</th>
 *     <th>参照型 reference</th>
 *   </tr>
 *   <tr>
 *     <td>テーブル</td>
 *     <td>{@link org.jiemamy.model.entity.TableModel}</td>
 *     <td>{@link org.jiemamy.model.entity.TableRef}</td>
 *   </tr>
 *   <tr>
 *     <td>ドメイン</td>
 *     <td>{@link org.jiemamy.model.datatype.DomainModel}</td>
 *     <td>{@link org.jiemamy.model.datatype.DomainRef} implements {@link org.jiemamy.model.datatype.DataType}</td>
 *   </tr>
 *   <tr>
 *     <td>組み込みデータ型</td>
 *     <td>(オブジェクト化されていない)</td>
 *     <td>{@link org.jiemamy.model.datatype.BuiltinDataType} implements {@link org.jiemamy.model.datatype.DataType}</td>
 *   </tr>
 * </table>
 * 
 * <p>全体把握の際は、このパッケージの次に{@code org.jiemamy.model.index}パッケージを把握するとよい。</p>
 * 
 * @author daisuke
 * @see org.jiemamy.model.JiemamyElement
 * @see org.jiemamy.model.ElementReference
 */
package org.jiemamy.model.datatype;

