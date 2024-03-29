/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.mee.source.c2;

import sun.misc.SharedSecrets;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Hash table based implementation of the <tt>Map</tt> interface.  This
 * implementation provides all of the optional map operations, and permits
 * <tt>null</tt> values and the <tt>null</tt> key.  (The <tt>HashMap</tt>
 * class is roughly equivalent to <tt>Hashtable</tt>, except that it is
 * unsynchronized and permits nulls.)  This class makes no guarantees as to
 * the order of the map; in particular, it does not guarantee that the order
 * will remain constant over time.
 * Map 接口的基于哈希表的实现。此实现提供所有可选的映射操作，并允许空值和空键。
 * （HashMap 类大致相当于 Hashtable，除了它是不同步的并且允许空值。）
 * 这个类不保证映射的顺序；特别是，它不保证订单会随着时间的推移保持不变。
 *
 * <p>This implementation provides constant-time performance for the basic
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * disperses the elements properly among the buckets.  Iteration over
 * collection views requires time proportional to the "capacity" of the
 * <tt>HashMap</tt> instance (the number of buckets) plus its size (the number
 * of key-value mappings).  Thus, it's very important not to set the initial
 * capacity too high (or the load factor too low) if iteration performance is
 * important.
 * 此实现为基本操作（get 和 put）提供恒定时间性能，假设哈希函数将元素正确地分散在桶中。
 * 对集合视图的迭代需要的时间与 HashMap 实例的“容量”（桶的数量）加上它的大小（键值映射的数量）成正比。
 * 因此，如果迭代性能很重要，则不要将初始容量设置得太高（或负载因子太低），这一点非常重要。
 *
 * <p>An instance of <tt>HashMap</tt> has two parameters that affect its
 * performance: <i>initial capacity</i> and <i>load factor</i>.  The
 * <i>capacity</i> is the number of buckets in the hash table, and the initial
 * capacity is simply the capacity at the time the hash table is created.  The
 * <i>load factor</i> is a measure of how full the hash table is allowed to
 * get before its capacity is automatically increased.  When the number of
 * entries in the hash table exceeds the product of the load factor and the
 * current capacity, the hash table is <i>rehashed</i> (that is, internal data
 * structures are rebuilt) so that the hash table has approximately twice the
 * number of buckets.
 * HashMap 的实例有两个影响其性能的参数：初始容量和负载因子。
 * 容量是哈希表中的桶数，初始容量只是哈希表创建时的容量。
 * 负载因子是哈希表在其容量自动增加之前允许达到的程度的度量。
 * 当哈希表中的条目数超过负载因子和当前容量的乘积时，对哈希表进行重新哈希（即重建内部数据结构），使哈希表的桶数大约增加一倍。
 *
 * <p>As a general rule, the default load factor (.75) offers a good
 * tradeoff between time and space costs.  Higher values decrease the
 * space overhead but increase the lookup cost (reflected in most of
 * the operations of the <tt>HashMap</tt> class, including
 * <tt>get</tt> and <tt>put</tt>).  The expected number of entries in
 * the map and its load factor should be taken into account when
 * setting its initial capacity, so as to minimize the number of
 * rehash operations.  If the initial capacity is greater than the
 * maximum number of entries divided by the load factor, no rehash
 * operations will ever occur.
 * 作为一般规则，默认负载因子 (.75) 在时间和空间成本之间提供了良好的折衷。
 * 较高的值会减少空间开销，但会增加查找成本（反映在 HashMap 类的大多数操作中，包括 get 和 put）。
 * 在设置其初始容量时，应考虑映射中的预期条目数及其负载因子，以尽量减少重新哈希操作的次数。
 * 如果初始容量大于最大条目数除以负载因子，则不会发生重新哈希操作。
 *
 * <p>If many mappings are to be stored in a <tt>HashMap</tt>
 * instance, creating it with a sufficiently large capacity will allow
 * the mappings to be stored more efficiently than letting it perform
 * automatic rehashing as needed to grow the table.  Note that using
 * many keys with the same {@code hashCode()} is a sure way to slow
 * down performance of any hash table. To ameliorate impact, when keys
 * are {@link Comparable}, this class may use comparison order among
 * keys to help break ties.
 * 如果要在一个 HashMap 实例中存储许多映射，则创建具有足够大容量的映射将比让它根据需要执行自动重新散列以增加表来更有效地存储映射。
 * 请注意，使用具有相同 hashCode() 的多个键是降低任何哈希表性能的可靠方法。
 * 为了改善影响，当键是 Comparable 时，此类可以使用键之间的比较顺序来帮助打破平局。
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a hash map concurrently, and at least one of
 * the threads modifies the map structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more mappings; merely changing the value
 * associated with a key that an instance already contains is not a
 * structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the map.
 *
 * If no such object exists, the map should be "wrapped" using the
 * {@link Collections#synchronizedMap Collections.synchronizedMap}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the map:<pre>
 *     请注意，此实现不同步。如果多个线程同时访问一个哈希映射，并且至少有一个线程在结构上修改了映射，则必须在外部进行同步。
 *     （结构修改是添加或删除一个或多个映射的任何操作；仅更改与实例已包含的键关联的值不是结构修改。）
 *     这通常通过在自然封装映射的某个对象上同步来完成.如果不存在这样的对象，
 *     则应使用 Collections.synchronizedMap 方法“包装”地图。这最好在创建时完成，以防止对地图的意外不同步访问：
 *   Map m = Collections.synchronizedMap(new HashMap(...));</pre>
 *
 * <p>The iterators returned by all of this class's "collection view methods"
 * are <i>fail-fast</i>: if the map is structurally modified at any time after
 * the iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> method, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the
 * future.
 * 所有此类的“集合视图方法”返回的迭代器都是快速失败的：如果在创建迭代器后的任何时间对映射进行结构修改，
 * 除了通过迭代器自己的 remove 方法之外，迭代器将抛出 ConcurrentModificationException .
 * 因此，面对并发修改，迭代器快速而干净地失败，而不是在未来不确定的时间冒任意的、非确定性的行为。
 * 也就是除了使用迭代器删除外，其他删除方式是一定会抛错
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 * 请注意，不能保证迭代器的快速失败行为，因为一般来说，在存在不同步的并发修改的情况下，
 * 不可能做出任何硬保证。快速失败的迭代器会尽最大努力抛出 ConcurrentModificationException。
 * 因此，编写一个依赖于这个异常的正确性的程序是错误的：迭代器的快速失败行为应该只用于检测错误。
 * => 就是并发错误并不能依此作为程序正确错误的判断。
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 * 此类是 Java 集合框架的成员。
 *
 * @param <K> the type of keys maintained by this map 此映射维护的键的类型
 * @param <V> the type of mapped values 映射值的类型
 *
 * @author  Doug Lea
 *  Douglas S. Lea是纽约州立大学奥斯威戈分校的计算机科学教授，现任计算机科学系主任，他专门研究并发编程和并发数据结构的设计。
 *  他是Java Community Process执行委员会的成员，并担任JSR 166的主席，该程序为Java编程语言添加了并发实用程序。
 *
 * @author  Josh Bloch
 *  约书亚·布洛克，美国著名程序员。他为Java平台设计并实作了许多的功能，曾担任Google的首席Java架构师。
 *
 * @author  Arthur van Hoff
 *  Java编程语言的早期贡献者，在斯特拉斯克莱德大学和Hogere Informatica Opleiding学习计算机科学后，Van Hoff加入了Sun Microsystems，
 *  担任分布式对象无处不在团队的工程师。1993年，他加入了Java开发团队，编写该语言的编译器，并负责1995年8月首次向Netscape发布该语言。
 *
 * @author  Neal Gafter
 *  约书亚·布洛克（英语：Joshua J. Bloch，1961年8月28日－），美国著名程序员。
 *  他为Java平台设计并实作了许多的功能，曾担任Google的首席Java架构师（Chief Java Architect）。
 *
 * @see     Object#hashCode()
 * @see     Collection
 * @see     Map
 * @see     TreeMap
 * @see     Hashtable
 * @since   1.2
 */
public class HashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable {

    private static final long serialVersionUID = 362498820763181265L;

    /*
     * Implementation notes. 实现说明。
     *
     * This map usually acts as a binned (bucketed) hash table, but
     * when bins get too large, they are transformed into bins of
     * TreeNodes, each structured similarly to those in
     * java.util.TreeMap. Most methods try to use normal bins, but
     * relay to TreeNode methods when applicable (simply by checking
     * instanceof a node).  Bins of TreeNodes may be traversed and
     * used like any others, but additionally support faster lookup
     * when overpopulated. However, since the vast majority of bins in
     * normal use are not overpopulated, checking for existence of
     * tree bins may be delayed in the course of table methods.
     * 此映射通常充当分箱（分桶）哈希表，但当箱变得太大时，它们会转换为 TreeNode 的箱，每个结构类似于 java.util.TreeMap 中的结构。
     * 大多数方法尝试使用正常的 bin，但在适用时中继到 TreeNode 方法（只需检查节点的实例）。
     * TreeNode 的 bin 可以像任何其他 bin 一样被遍历和使用，但在填充过多时还支持更快的查找。
     * 然而，由于绝大多数正常使用的 bin 并没有被过度填充，因此在 table 方法的过程中检查树 bin 的存在可能会被延迟。
     *
     * Tree bins (i.e., bins whose elements are all TreeNodes) are
     * ordered primarily by hashCode, but in the case of ties, if two
     * elements are of the same "class C implements Comparable<C>",
     * type then their compareTo method is used for ordering. (We
     * conservatively check generic types via reflection to validate
     * this -- see method comparableClassFor).  The added complexity
     * of tree bins is worthwhile in providing worst-case O(log n)
     * operations when keys either have distinct hashes or are
     * orderable, Thus, performance degrades gracefully under
     * accidental or malicious usages in which hashCode() methods
     * return values that are poorly distributed, as well as those in
     * which many keys share a hashCode, so long as they are also
     * Comparable. (If neither of these apply, we may waste about a
     * factor of two in time and space compared to taking no
     * precautions. But the only known cases stem from poor user
     * programming practices that are already so slow that this makes
     * little difference.)
     * 树箱（即元素都是 TreeNodes 的箱）主要按 hashCode 排序，但在平局的情况下，如果两个元素属于相同的“C 类实现 Comparable<C>”，
     * 则使用它们的 compareTo 方法订购。 （我们保守地通过反射检查泛型类型来验证这一点——参见方法 compatibleClassFor）。
     * 当键具有不同的哈希值或可排序时，树箱增加的复杂性在提供最坏情况 O(log n) 操作时是值得的，
     * 因此，在 hashCode() 方法返回的值很差的意外或恶意使用下，性能会优雅地下降分布式的，以及许多键共享一个 hashCode 的，
     * 只要它们也是 Comparable 的。 （如果这些都不适用，与不采取预防措施相比，我们可能会浪费大约两倍的时间和空间
     * 。但唯一已知的案例源于糟糕的用户编程实践，这些实践已经很慢，几乎没有什么区别。）
     *
     * Because TreeNodes are about twice the size of regular nodes, we
     * use them only when bins contain enough nodes to warrant use
     * (see TREEIFY_THRESHOLD). And when they become too small (due to
     * removal or resizing) they are converted back to plain bins.  In
     * usages with well-distributed user hashCodes, tree bins are
     * rarely used.  Ideally, under random hashCodes, the frequency of
     * nodes in bins follows a Poisson distribution
     * (http://en.wikipedia.org/wiki/Poisson_distribution) with a
     * parameter of about 0.5 on average for the default resizing
     * threshold of 0.75, although with a large variance because of
     * resizing granularity. Ignoring variance, the expected
     * occurrences of list size k are (exp(-0.5) * pow(0.5, k) /
     * factorial(k)). The first values are:
     * 因为 TreeNode 的大小大约是常规节点的两倍，所以我们仅在 bin 包含足够的节点以保证使用时才使用它们（请参阅 TREEIFY_THRESHOLD）。
     * 当它们变得太小（由于移除或调整大小）时，它们会被转换回普通垃圾箱。在具有良好分布的用户哈希码的使用中，很少使用树箱。
     * 理想情况下，在随机 hashCodes 下，bin 中节点的频率遵循泊松分布 (http:en.wikipedia.orgwikiPoisson_distribution)，
     * 默认调整大小阈值为 0.75，平均参数约为 0.5，尽管由于调整大小粒度而存在很大差异.忽略方差，
     * 列表大小 k 的预期出现是 (exp(-0.5) pow(0.5, k) factorial(k))。第一个值是：
     *
     * 0:    0.60653066
     * 1:    0.30326533
     * 2:    0.07581633
     * 3:    0.01263606
     * 4:    0.00157952
     * 5:    0.00015795
     * 6:    0.00001316
     * 7:    0.00000094
     * 8:    0.00000006
     * more: less than 1 in ten million
     * 更多：不到千万分之一
     *
     * The root of a tree bin is normally its first node.  However,
     * sometimes (currently only upon Iterator.remove), the root might
     * be elsewhere, but can be recovered following parent links
     * (method TreeNode.root()).
     * 树箱的根通常是它的第一个节点。但是，有时（目前仅在 Iterator.remove 上），根可能在其他地方，
     * 但可以在父链接之后恢复（方法 TreeNode.root()）。
     *
     * All applicable internal methods accept a hash code as an
     * argument (as normally supplied from a public method), allowing
     * them to call each other without recomputing user hashCodes.
     * Most internal methods also accept a "tab" argument, that is
     * normally the current table, but may be a new or old one when
     * resizing or converting.
     * 所有适用的内部方法都接受哈希码作为参数（通常由公共方法提供），允许它们相互调用而无需重新计算用户哈希码。
     * 大多数内部方法还接受“tab”参数，通常是当前表，但在调整大小或转换时可能是新表或旧表。
     *
     * When bin lists are treeified, split, or untreeified, we keep
     * them in the same relative access/traversal order (i.e., field
     * Node.next) to better preserve locality, and to slightly
     * simplify handling of splits and traversals that invoke
     * iterator.remove. When using comparators on insertion, to keep a
     * total ordering (or as close as is required here) across
     * rebalancings, we compare classes and identityHashCodes as
     * tie-breakers.
     * 当 bin 列表被树化、拆分或未树化时，我们将它们保持在相同的相对访问遍历顺序（即字段 Node.next）中，以更好地保留局部性，
     * 并稍微简化调用 iterator.remove 的拆分和遍历的处理。在插入时使用比较器时，为了在重新平衡之间保持总排序（或此处要求的接近），
     * 我们将类和 identityHashCodes 比较为决胜局。
     *
     * The use and transitions among plain vs tree modes is
     * complicated by the existence of subclass LinkedHashMap. See
     * below for hook methods defined to be invoked upon insertion,
     * removal and access that allow LinkedHashMap internals to
     * otherwise remain independent of these mechanics. (This also
     * requires that a map instance be passed to some utility methods
     * that may create new nodes.)
     * 由于子类 LinkedHashMap 的存在，普通模式与树模式之间的使用和转换变得复杂。
     * 请参阅下面定义为在插入、删除和访问时调用的钩子方法，这些方法允许 LinkedHashMap 内部保持独立于这些机制。
     * （这还需要将地图实例传递给一些可能创建新节点的实用程序方法。）
     *
     * The concurrent-programming-like SSA-based coding style helps
     * avoid aliasing errors amid all of the twisty pointer operations.
     * 类似并发编程的基于 SSA 的编码风格有助于避免在所有曲折的指针操作中出现别名错误。
     *
     */

    /**
     * The default initial capacity - MUST be a power of two.
     * 默认初始容量 - 必须是 2 的幂。 => 2^4
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     * 最大容量，如果一个更高的值由任何一个带参数的构造函数隐式指定时使用。必须是 2 <= 1<<30 的幂。
     *
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;// 最大容量

    /**
     * The load factor used when none specified in constructor.
     * 构造函数中未指定时使用的负载因子。
     *
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f; // 默认负载系数

    /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     * 使用树而不是列表的 bin 计数阈值。将元素添加到至少具有这么多节点的 bin 时，bin 将转换为树。
     * 该值必须大于 2 并且应该至少为 8，以便与树移除中关于在收缩时转换回普通 bin 的假设相吻合。
     *
     */
    static final int TREEIFY_THRESHOLD = 8; // 树形阈值

    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     * 在调整大小操作期间 untreeifying（拆分）bin 的 bin 计数阈值。
     * 应小于 TREEIFY_THRESHOLD，并且最多 6 以在移除时进行收缩检测。
     *
     */
    static final int UNTREEIFY_THRESHOLD = 6; // 取消阈值

    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.
     * 可对其进行树化的 bin 的最小表容量。 （否则，如果 bin 中有太多节点，则调整表的大小。）
     * 应至少为 4 TREEIFY_THRESHOLD 以避免调整大小和树化阈值之间的冲突。
     */
    static final int MIN_TREEIFY_CAPACITY = 64; // 最小树形容量

    /**
     * Basic hash bin node, used for most entries.  (See below for
     * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
     * 基本哈希 bin 节点，用于大多数条目。 （参见下面的 TreeNode 子类，以及 LinkedHashMap 中的 Entry 子类。）
     */
    static class Node<K,V> implements Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            // key的hashCode 与 value的hashCode做异或元素暗得到的Node的hashCode
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        // 设置值的时候是将旧值返回，新的替换旧的
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            // 对象是否相等，是否 K/V 相等
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Entry<?,?> e = (Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    /* ---------------- Static utilities （静态实用程序）-------------- */

    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.  Because the table uses power-of-two masking, sets of
     * hashes that vary only in bits above the current mask will
     * always collide. (Among known examples are sets of Float keys
     * holding consecutive whole numbers in small tables.)  So we
     * apply a transform that spreads the impact of higher bits
     * downward. There is a tradeoff between speed, utility, and
     * quality of bit-spreading. Because many common sets of hashes
     * are already reasonably distributed (so don't benefit from
     * spreading), and because we use trees to handle large sets of
     * collisions in bins, we just XOR some shifted bits in the
     * cheapest possible way to reduce systematic lossage, as well as
     * to incorporate impact of the highest bits that would otherwise
     * never be used in index calculations because of table bounds.
     * 计算 key.hashCode() 并将哈希的较高位传播（XOR）到较低位。由于该表使用二次幂掩码，因此仅在当前掩码之上位变化的散列集将始终发生冲突。
     * （已知的例子是在小表中保存连续整数的 Float 键集。）因此，我们应用了一种变换，将高位的影响向下传播。
     * 在位扩展的速度、实用性和质量之间存在折衷。因为许多常见的散列集已经合理分布（所以不要从传播中受益），
     * 并且因为我们使用树来处理 bin 中的大量冲突，我们只是以最便宜的方式对一些移位的位进行异或，以减少系统损失，
     * 以及合并最高位的影响，否则由于表边界，这些最高位将永远不会用于索引计算。
     *
     */
    public static final int hash(Object key) {
        // TODO 计算hash的方式 hashcode ^ hashcode>>>16位置
        // >>运算符是有符号的右移运算符,而>>>是无符号的右移运算符
        // 这个hash相对于自身hashcode的区别是它的高位几乎不会变动
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * Returns x's Class if it is of the form "class C implements
     * Comparable<C>", else null.
     * 如果 x 的类是“类 C 实现 Comparable<C>”的形式，则返回 x 的类，否则返回 null。
     */
    static Class<?> comparableClassFor(Object x) {
        // TODO 返回的是类C的原始类型，具体实现不大看得懂
        if (x instanceof Comparable) {
            Class<?> c;
            Type[] ts, as;
            Type t;
            ParameterizedType p; // 参数化类型
            if ((c = x.getClass()) == String.class) // bypass checks 绕过检查
                return c;
            // 从c获取通用接口
            // 返回表示由该对象表示的类或接口直接实现的接口的类型。
            if ((ts = c.getGenericInterfaces()) != null) {
                for (int i = 0; i < ts.length; ++i) {
                    // ParameterizedType： 参数化类型
                    // getRawType：获取原始类型
                    // getActualTypeArguments：获取实际类型参数
                    if (((t = ts[i]) instanceof ParameterizedType) &&
                            ((p = (ParameterizedType)t).getRawType() ==
                                    Comparable.class) &&
                            (as = p.getActualTypeArguments()) != null &&
                            as.length == 1 && as[0] == c) // type arg is c
                        return c;
                }
            }
        }
        return null;
    }

    /**
     * Returns k.compareTo(x) if x matches kc (k's screened comparable
     * class), else 0.
     * 比较可比物::如果 x 匹配 kc（k 的筛选可比类），则返回 k.compareTo(x)，否则返回 0。
     */
    @SuppressWarnings({"rawtypes","unchecked"}) // for cast to Comparable
    static int compareComparables(Class<?> kc, Object k, Object x) {
        // x的class类型不是kc返回0，否则x与k进行比较
        return (x == null || x.getClass() != kc ? 0 :
                ((Comparable)k).compareTo(x));
    }

    /**
     * Returns a power of two size for the given target capacity.
     * 返回给定目标容量的 2 次方。
     */
    static final int tableSizeFor(int cap) {
        // 表尺寸::根據初始容量算出这个初始容量对应的临界值，这个临界值就是实际的容量
        // 一般规律是 临界值为 这个初始容量相临近的且为2的幂次的值且>=這個初始容量 ，以下为一般规律
        // 且最大为2的30次方（MAXIMUM_CAPACITY），大于MAXIMUM_CAPACITY的均以MAXIMUM_CAPACITY作为临界值(实际容量)
        // initialCapacity=0 => threshold=1
        // initialCapacity=1 => threshold=1
        // initialCapacity=2 => threshold=2
        // initialCapacity=3 => threshold=4
        // initialCapacity=4 => threshold=4
        // initialCapacity=5 => threshold=8
        // initialCapacity=9 => threshold=16
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /* ---------------- Fields -------------- */

    /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * (We also tolerate length zero in some operations to allow
     * bootstrapping mechanics that are currently not needed.)
     * 该表在首次使用时初始化，并根据需要调整大小。分配时，长度始终是 2 的幂。
     * （我们还在某些操作中允许长度为零，以允许当前不需要的引导机制。）
     */
    transient Node<K,V>[] table;

    /**
     * Holds cached entrySet(). Note that AbstractMap fields are used
     * for keySet() and values().
     * 保存缓存的 entrySet()。请注意，AbstractMap 字段用于 keySet() 和 values()。
     * 也就是entrySet这个成员变量在HashMap中定义，他的keySet以及values是AbstractMap的重写
     */
    transient Set<Entry<K,V>> entrySet;

    /**
     * The number of key-value mappings contained in this map.
     * 此映射中包含的键值映射的数量。
     */
    transient int size;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     * 此 HashMap 已被结构修改的次数 结构修改是指更改 HashMap 中的映射数量或以其他方式修改其内部结构（例如，重新散列）的那些。
     * 该字段用于使 HashMap 的 Collection-views 上的迭代器快速失败。 （请参阅 ConcurrentModificationException）。
     */
    transient int modCount;

    /**
     * The next size value at which to resize (capacity * load factor).
     * 要调整大小的下一个大小值（容量负载因子）。
     *
     * @serial
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.)
    // （javadoc 描述在序列化时为真。此外，如果尚未分配表数组，则此字段保存初始数组容量，或零表示 DEFAULT_INITIAL_CAPACITY。）
    int threshold;

    /**
     * The load factor for the hash table.
     * 哈希表的负载因子。
     * @serial
     */
    final float loadFactor;

    /* ---------------- Public operations 公共运营 -------------- */

    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and load factor.
     * 构造一个具有指定初始容量和负载因子的空 HashMap
     *
     * @param  initialCapacity the initial capacity 初始容量
     * @param  loadFactor      the load factor  负载系数
     * @throws IllegalArgumentException if the initial capacity is negative 如果初始容量为负
     *         or the load factor is nonpositive 或负载因子为非正数
     */
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        // 限制最大容量
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        // 负载因子为数且值>0
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        // 负载因子直接赋值，不做检查,这个参数做什么用后续会说到
        this.loadFactor = loadFactor;
        // 阀值/临界点：目标容量(initialCapacity)的 2 次方。
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     * 构造一个具有指定初始容量和默认加载因子 (0.75) 的空 HashMap
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public HashMap(int initialCapacity) {
        // 应为要对initialCapacity进行处理，这里直接调用完整的构造方法(以上)
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     * 构造一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 HashMap
     */
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted 所有其他字段默认
    }

    /**
     * Constructs a new <tt>HashMap</tt> with the same mappings as the
     * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
     * default load factor (0.75) and an initial capacity sufficient to
     * hold the mappings in the specified <tt>Map</tt>.
     * 构造一个与指定 <tt>Map<tt> 具有相同映射的新 <tt>HashMap<tt>。
     * <tt>HashMap<tt> 是使用默认加载因子 (0.75) 和足以保存指定 <tt>Map<tt> 中的映射的初始容量创建的。
     *
     * @param   m the map whose mappings are to be placed in this map
     * @throws  NullPointerException if the specified map is null
     */
    public HashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        // 将传入的map放入当前map，具体的后续会讲
        putMapEntries(m, false);
    }

    /**
     * Implements Map.putAll and Map constructor.
     * 实现 Map.putAll 和 Map 构造函数。(意思是putAll也调用此方法)
     *
     * @param m the map
     * @param evict 驱逐 false when initially constructing this map, else
     * true (relayed to method afterNodeInsertion).
     * 最初构造此映射时为 false，否则为 true（与方法 afterNode Insertion 相关）。
     *
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        // m为空，则无需执行此段逻辑
        if (s > 0) {
            // 当前table为空时
            if (table == null) { // pre-size 预尺寸
                // 容量/负载因子(0.75F) + 1,这其实是算出一个目标容量大小,这里+1的问题是算出的容量是向上取整的
                float ft = ((float)s / loadFactor) + 1.0F;
                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                        (int)ft : MAXIMUM_CAPACITY);
                // 如果大于当前的阀值则需要重新计算阀值
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }
            else if (s > threshold)
                resize(); // 调整大小，这个后续会说
            // 参数一个个添加至当前map
            for (Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                // 添加元素具体执行方法，注意第一个参数为key的hash值，函数后续会讲解
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     * 返回此映射中键值映射的数量。
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        // 注意，这个值是当前map中k/v键值实际数量，跟threshold不一样！
        return size;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     * 如果此映射不包含键值映射，则返回 true。
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        // size是int基础类型，默认值就是0也为空
        return size == 0;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * 返回指定键映射到的值，如果此映射不包含该键的映射，则返回 null。
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * 更正式地说，如果此映射包含从键 k 到值 v 的映射，使得 (key==null ? k==null : key.equals(k))，
     * 则此方法返回 v；否则返回null。 （最多可以有一个这样的映射。）
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     * 返回值为 null 并不一定表示该映射不包含该键的映射；映射也可能将键显式映射为空。 containsKey 操作可用于区分这两种情况。另请参阅：放置（对象，对象）
     *
     * @see #put(Object, Object)
     */
    public V get(Object key) {
        Node<K,V> e;
        // 如果没有key这个node则返回null,否则返回节点的value,键值对是在同一个Node对象内
        // 重要对是 官方注释中也有说：返回对Node对象的value也有可能为null，所以并不能通过value是否为null来判断是否有存储这个null节点
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     * Implements Map.get and related methods.
     * 实现 Map.get 和相关方法。
     *
     * @param hash hash for key 键的哈希
     * @param key the key 键
     * @return the node, or null if none 节点，如果没有则为 null
     */
    final Node<K,V> getNode(int hash, Object key) {
        // tab: 为当前map的一个引用(table)
        // first: 为map第一个节点,具体节点获取方式为 tab[(tab.length-1) & hash]
        // e: 表示下一个节点，也是活动节点，从first的引用开始获取
        // n: tab的大小,也等于table.length
        // k: 为活动节点的key(会在循环内不断变动引用)，其值为对应活动节点(first或e)的key
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&  (first = tab[(n - 1) & hash]) != null) {
            // always check first node 总是检查第一个节点的hash，以及key
            // 其实也就是检查第一个node是否是目的(需返回的node)节点，是就无需往下找了～
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                // 以下分两种情况
                // 第一种：这个节点为树节点(具体为红黑树),通过树的形式找，找到即直接返回，
                //       对于此种情况，也说明了一种问题：如果当前map第一个节点为TreeNode则其他节点也为TreeNode
                // 第二种：这个节点为普通节点(具体为链表的形式)，通过其引用的(next)后继节点查找 类似于iterator的next
                if (first instanceof TreeNode)
                    // TreeNode也是Node接口的一个实现，可以进行强转，这里直接用TreeNode的实现进行查找
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    // 对于e的判断与first的判断是一致的
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * specified key.
     * 如果此映射包含指定键的映射，则返回 true。
     *
     * @param   key   The key whose presence in this map is to be tested
     *  要测试在此映射中是否存在的键
     * @return <tt>true</tt> if this map contains a mapping for the specified key.
     *  如果此映射包含指定键的映射，则为 true。
     *
     */
    public boolean containsKey(Object key) {
        // 当前方法是必要的，它弥补了get方法存在的问题，也就是null的问题
        // 因为null是可以保存为一个节点的，如果根本没有保存就不存在key=null的节点（node）
        return getNode(hash(key), key) != null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     * 将指定的值与此映射中的指定键相关联。如果映射先前包含键的映射，则替换旧值。
     *
     * @param key key with which the specified value is to be associated 与指定值关联的键
     * @param value value to be associated with the specified key 与指定键关联的值
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     * 与 key 关联的前一个值，如果没有 key 映射，则返回 null。 （返回 null 还可以指示映射先前将 null 与 key 关联。）
     */
    public V put(K key, V value) {
        // k/v相关联，并作为一个Node(可能普通Node也可能是TreeNode)
        // 如果存在key，则返回原value
        // 如果value=null或存在value的Node为null，均返回null
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * Implements Map.put and related methods.
     * 实现 Map.put 和相关方法。
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value 如果为真，则不要更改现有值
     * @param evict if false, the table is in creation mode. 如果为 false，则表处于创建模式。
     * @return previous value, or null if none  前一个值，如果没有，则为 null
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        // 如果table为null则先建一个
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        // 如果起始节点为空则新建一个起始节点
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e;  // 原node
            K k; // 活动节点的key
            if (p.hash == hash &&  ((k = p.key) == key || (key != null && key.equals(k))))
                e = p; // 原节点给e
            else if (p instanceof TreeNode)
                // 对于树节点 需采用树节点的处理方式 -> putTreeVal
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                // 以下为普通节点的处理方式(链表)
                for (int binCount = 0; ; ++binCount) {
                    // next节点为空 直接新增一个包含当前待插入到k/v的节点
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        // 普通链表与树之间会有一个阀值(TREEIFY_THRESHOLD),大于等于 TREEIFY_THRESHOLD-1 时需要进行树化
                        // TREEIFY_THRESHOLD：树形阈值=8
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash); // 树化
                        break;
                    }
                    // 找到这个节点只需要break即可，也不会再执行 p=e
                    if (e.hash == hash &&  ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e; // 因为e=p.next，进行到下一个节点之前需要将p指向e,p=e即为p更新到下一个节点
                }
            }
            // 注意，如果找到这个key所对应的老节点只会在节点内更新value即可，因为不存在map大小的变更，则版本(modCount)也不会变
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                // oh，明白，这个onlyIfAbsent代表在找到老节点后是否更新老节点的值，这个onlyIfAbsent一般是false(更新)
                // TODO 但是 oldValue==null 又会进入更新，这就不太理解咯，后面再看
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e); // TODO  允许 LinkedHashMap 后操作的回调
                return oldValue; // 返回老节点的值
            }
        }
        // 更新下版本，此时已经表明当前的k/v是插入到map中，而不是替换老节点
        ++modCount;
        // 更新size的同时也要检查是否需要调整大小->resize
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict); // TODO 允许 LinkedHashMap 后操作的回调
        return null; // 因为k/v是插入的，所以不存在老节点，故返回null
    }

    /**
     * 调整大小
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     * 初始化或加倍表大小。如果为空，则按照字段阈值中保存的初始容量目标进行分配。
     * 否则，因为我们使用二次幂展开，每个 bin 中的元素必须保持相同的索引，或者在新表中以二次幂的偏移量移动。
     * @return the table
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold; // 临界值
        int newCap, newThr = 0;
        if (oldCap > 0) {
            // 原容量如果已经大于 指定的临界值，则临界值就是Integer.MAX_VALUE,table也还是原table，容量也无需改变
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // 新的容量默认是往左移1位(<<1 就是x2)，同时它的临界值是 2^4(16)<=容量<2^30 这个范围内的时候临界值大小也往左移动一位(<<1 也即x2)
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold 临界值*2
        }
        else if (oldThr > 0) // initial capacity was placed in threshold 初始容量被置于阈值
            newCap = oldThr; // 新的容量等于老的临界值
        else {               // zero initial threshold signifies using defaults 零初始阈值表示使用默认值
            newCap = DEFAULT_INITIAL_CAPACITY; // 初始化容量是16
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY); //临界值=16*0.75（默认负载系数）=12
        }
        if (newThr == 0) {
            // 新的临界值
            float ft = (float)newCap * loadFactor;
            // 新的临界值以及容量均 < 2^30 时候，直接更新为新的临界值，否则新的临界值为Integer.MAX_VALUE
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        // TODO 以下功能没咋看懂，以后再具体解释
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e; // 活动的node，也就是当前节点，理解这一点很重要
                if ((e = oldTab[j]) != null) { // 这里这样判断实在没必要
                    oldTab[j] = null; // 清空这个索引位置的Node使之gc
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e; // 不存在下一个节点则新table直接赋值为e
                    else if (e instanceof TreeNode)
                        // 对于数节点则采用树的处理方式
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order 维护秩序
                        // 头或尾巴的高位或低位节点
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            // 一般是最后一个节点会进入if内，
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null); // 循环到最后这个e就是null
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead; // 这个 loTail 似乎与 loHead 是一样的，都是e
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead; // 因为Node[]扩容之后大小为之前到两倍，所以这里的j+oldCap是不存在抛错这种行为的
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /**
     * Replaces all linked nodes in bin at index for given hash unless
     * table is too small, in which case resizes instead.
     * 替换给定哈希索引处 bin 中的所有链接节点，除非表太小，在这种情况下调整大小。
     */
    final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        //  MIN_TREEIFY_CAPACITY: 最小树形容量
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize(); // 调整大小
        // 这里面(else if)的条件似乎是找到最后一个节点
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K,V> hd = null, tl = null;
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null); // 替换为树节点
                if (tl == null)
                    hd = p;
                else {
                    // TODO 这个似乎是在做双向绑定，整体还是不大明白
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);// 树化
        }
    }

    /**
     * Copies all of the mappings from the specified map to this map.
     * These mappings will replace any mappings that this map had for
     * any of the keys currently in the specified map.
     * 将所有映射从指定映射复制到此映射。这些映射将[替换]此映射对当前指定映射中的任何键的任何映射。
     *
     * @param m mappings to be stored in this map 要存储在此地图中的映射
     * @throws NullPointerException if the specified map is null 如果指定的地图为空
     */
    public void putAll(Map<? extends K, ? extends V> m) {
        // 当前功能很明显：将传入的m里面所有的kv放入到当前map内，相同的的映射将被替换
        // example:
        //  HashMap<String,String> m1 = new HashMap<String,String>();
        //  m1.put("A","1");
        //  m1.put("B","2");
        //  m1.put("C","3");
        //
        //  HashMap<String,String> m2 = new HashMap<String,String>();
        //  m2.put("A","K");
        //  m2.put("B","K");
        //  m2.put("D","4");
        //
        //  m1.putAll(m2);
        //
        //  System.out.println(m1); // {A=K, B=K, C=3, D=4}
        //  System.out.println(m2); // {A=K, B=K, D=4}
        putMapEntries(m, true);
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * 如果存在，则从此映射中删除指定键的映射。
     *
     * @param  key key whose mapping is to be removed from the map 要从映射中删除其映射的键
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     *         与 key 关联的前一个值，如果没有 key 映射，则返回 null。
     *         （返回 null 还可以指示映射先前将 null 与 key 关联。）
     *         TODO 这官方注释着实看不明白。。。
     */
    public V remove(Object key) {
        // 删除key，本质上就是删除节点，这个节点可能是普通节点也可能是树节点，返回key所对应的value，如果没有映射的value则返回null
        // TODO 额，具体移除还是看removeNode...
        Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    /**
     * Implements Map.remove and related methods. 实现 Map.remove 和相关方法。
     *
     * @param hash hash for key 键的哈希
     * @param key the key key自身
     * @param value the value to match if matchValue, else ignored  如果匹配值，则要匹配的值，否则忽略
     * @param matchValue if true only remove if value is equal  如果为真，则仅在值相等时删除：说人话就是为true时不会在找到Node(hash、key)后继续比较value是否一致
     *                                                                                  所以matchValue=false时 value传进来才有意义，本质上就是匹配值删除
     * @param movable if false do not move other nodes while removing   如果为假(false)，则在删除时不要移动其他节点: 也就是是否移动其他节点
     * @return the node, or null if none    节点，如果没有则为 null: 节点不存在返回null
     */
    final Node<K,V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable) {
        // tab=table,n=table.length     index=n-1,p=table[index & hash]
        // n是table的长度，p是当前活跃的Node，index为当前活跃的Node的索引
        Node<K,V>[] tab; Node<K,V> p; int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 && (p = tab[index = (n - 1) & hash]) != null) {
            // node可以理解为目标节点 即hash以及key都匹配的Node
            Node<K,V> node = null, e; K k; V v;
            // 比较Node的Hash是否与传入的hash是否相等，Node的key(k)是否与传入的key是否相等
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                // TODO 如果是树节点...
                if (p instanceof TreeNode)
                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
                else {
                    // 如果是普通节点
                    do {
                        // 依旧是比较hash以及key
                        if ( e.hash == hash &&  ((k = e.key) == key || (key != null && key.equals(k))) ) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            // 下面这个matchValue这个传入的变量默认是false，这个时候就无需再比较值(value),反之则需要比较值，这个就比较迷惑了...
            // matchValue简单理解为 是否继续比较value
            if (node != null && (!matchValue || (v = node.value) == value || (value != null && value.equals(v))) ) {
                // 树节点走树的删除
                if (node instanceof TreeNode)
                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);// movable 默认为true
                else if (node == p)
                    tab[index] = node.next;// node为桶的头节点
                else
                    p.next = node.next; // p是node的上一个节点的时候(do..while),一样将node节点空出来
                ++modCount; // 版本
                --size; // 实际容量
                afterNodeRemoval(node); // 这个是LinkedHash的实现，内部为切断node的引用
                return node;
            }
        }
        return null;
    }

    /**
     * Removes all of the mappings from this map.   从此map中删除所有映射。
     * The map will be empty after this call returns.   此调用返回后，map将为空。
     */
    public void clear() {
        Node<K,V>[] tab;
        modCount++; // 版本
        if ((tab = table) != null && size > 0) {
            size = 0; // 实际元素个数
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null; // 这里个人粗糙的理解为 仅仅切断了桶的引用，桶内的链表或树并不管。。。
        }
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     * 如果此映射将一个或多个键映射到指定值，则返回 true。
     *
     * @param value value whose presence in this map is to be tested    要测试其在此映射中的存在的值
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value
     *         如果此映射将一个或多个键映射到指定值，则为 true
     */
    public boolean containsValue(Object value) {
        Node<K,V>[] tab; V v;
        if ((tab = table) != null && size > 0) {
            for (int i = 0; i < tab.length; ++i) {
                // 循环每个桶，拿出每个node并比较值
                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                    // 如果是基本数据类型用==比较即可，如果符合类型则必须使用equal比较其内容才可
                    if ((v = e.value) == value || (value != null && value.equals(v))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     * 返回此映射中包含的键的Set视图。集合由贴图支持，因此对贴图的更改会反映在集合中，反之亦然。如果在对集合进行迭代时修改映射（迭代器自己的移除操作除外），则迭代的结果是未定义的。该集支持元素移除，
     * 通过Iterator.remove、set.remove，removeAll、retainAll和clear操作从映射中移除相应的映射。它不支持add或addAll操作。
     *
     * @return a set view of the keys contained in this map
     * 返回 此映射中包含的关键点的集合视图
     */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    final class KeySet extends AbstractSet<K> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        // 返回key的迭代对象
        public final Iterator<K> iterator()     { return new KeyIterator(); }
        // 这个最终还是调用HashMap的containsKey
        public final boolean contains(Object o) { return containsKey(o); }
        // 底层依然是HashMap实现的
        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }
        //分可以HashMap的key，这个后面会有分析
        public final Spliterator<K> spliterator() {
            return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        // 函数式变量,他是Iterable接口的实现,这个在ArrayList中也是一样的
        public final void forEach(Consumer<? super K> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    // 这个地方是根据容量循环，比如当前容量是16则会循环16次找每个桶位不为null的Node
                    for (Node<K,V> e = tab[i]; e != null; e = e.next){
                        // example: data.keySet().forEach(item->{
                        //           System.out.println(item);
                        //        });
                        // 这个accept应该就是应用每一项到匿名函数内，对应上方的example就是item,accept的就是item
                        action.accept(e.key);
                    }
                }
                // 存在并发修改时抛出错误，所以当前这个KeySet对象不是并发安全
                if (modCount != mc){
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * 返回此映射中包含的值的“集合”视图。集合由Map支持，因此对Map的更改会反映在集合中，反之亦然。
     * 如果在对集合进行迭代时修改了映射（迭代器自己的移除操作除外），则迭代的结果是未定义的。该集合支持元素移除，
     * 通过Iterator.remove、collection.remove，removeAll，retainAll和clear操作从映射中移除相应的映射。它不支持add或addAll操作。
     *
     * @return a view of the values contained in this map
     * 返回   此映射中包含的值的Map
     */
    public Collection<V> values() {
        // 返回值的集合
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
    }

    final class Values extends AbstractCollection<V> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        public final Iterator<V> iterator()     { return new ValueIterator(); }
        public final boolean contains(Object o) { return containsValue(o); }
        public final Spliterator<V> spliterator() {
            return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        public final void forEach(Consumer<? super V> action) {
            // 同上放keyset::forEach的实现是一致的
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.value);
                }
                // 同样需要检测并发（遍历时不支持元素的新增或修改）
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     * 返回此映射中包含的值的“集合”视图。集合由Map支持，因此对Map的更改会反映在集合中，反之亦然。
     * 如果在对集合进行迭代时修改了映射（迭代器自己的移除操作除外），则迭代的结果是未定义的。该集合支持元素移除，
     * 通过Iterator.remove、collection.remove，removeAll，retainAll和clear操作从映射中移除相应的映射。它不支持add或addAll操作。
     *
     * @return a set view of the mappings contained in this map
     * 此映射中包含的值的视图
     */
    public Set<Entry<K,V>> entrySet() {
        Set<Entry<K,V>> es;
        // 只要创建了HashMap在，不论是否为空总能返回一个EntitySet
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }

    final class EntrySet extends AbstractSet<Entry<K,V>> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        public final Iterator<Entry<K,V>> iterator() {
            // 这是个关于Node的Entity的迭代对象
            return new EntryIterator();
        }
        public final boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Entry<?,?> e = (Entry<?,?>) o;
            Object key = e.getKey();
            // 不管是新增还是查找总需要 关于key的hash值
            Node<K,V> candidate = getNode(hash(key), key);
            return candidate != null && candidate.equals(e);
        }
        public final boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Entry<?,?> e = (Entry<?,?>) o;
                Object key = e.getKey();
                Object value = e.getValue();
                // 删除会返回被删除的Node这是遵循HashMap的设计
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }
        public final Spliterator<Entry<K,V>> spliterator() {
            // 分割HashMap的Node，这个后续会分析
            return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        public final void forEach(Consumer<? super Entry<K,V>> action) {
            // 这个跟上面的keySet Values的实现没有什么不同，只是传递的是Node
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next){
                        // 这个
                        action.accept(e);
                    }
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    // Overrides of JDK8 Map extension methods

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        // 几乎任何时候从HashMap拿到Node都必须要有两个参数 1是key的hash 2key自身
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }

    // 如果缺席则放置
    @Override
    public V putIfAbsent(K key, V value) {
        // 如果key不存在则put进去 注意第四个参数
        return putVal(hash(key), key, value, true, true);
    }

    @Override
    public boolean remove(Object key, Object value) {
        // 根据 key value 匹配table中的node，匹配到则删除，注意第四个参数 是要匹配值的
        return removeNode(hash(key), key, value, true, true) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        // 匹配key以及老value后执行替换value的替换，只返回操作成功与否
        Node<K,V> e; V v;
        if ((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = newValue;
            // LinkedHashMap是有此实现，不知为什么这里只是一个空实现
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        Node<K,V> e;
        if ((e = getNode(hash(key), key)) != null) {
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            // 不同于上方的replace的实现，操作成功后会返回原value
            return oldValue;
        }
        return null;
    }

    // 缺席时计算
    // 这个计算是这样的根据传入的key若找到key所在的node则用function中value替换node中的value
    // 如果key对应的Node没有则新增一个node放入map，node的key即为传入的key，node的value则为function中给的value
    // 同时需要说明的是如果function给的是null则不做任何处理直接返回null
    // 返回的value总是function表达式中给的value
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (mappingFunction == null){
            throw new NullPointerException();
        }
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n, i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0){
            // 扩充节点大小
            n = (tab = resize()).length;
        }
        // 找到key所在链表或tree的头节点一步步往下找
        // 可索引大小 与 key的hash做或操作 以定位桶的位置
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
            else {
                Node<K,V> e = first; K k;
                do {
                    // 这里充分说明了即使是key的hash一致也不能保证key是一致的
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
            // 判断找到的这个节点以及value是否为null，不为null则返回这个node的value
            V oldValue;
            if (old != null && (oldValue = old.value) != null) {
                afterNodeAccess(old);
                return oldValue;
            }
        }
        V v = mappingFunction.apply(key);
        if (v == null) {
            return null;
        }else if(old != null) {
            old.value = v;
            afterNodeAccess(old);
            return v;
        }else if(t != null) {
            // node是一个树结构的需要调用树的处理方式
            t.putTreeVal(this, tab, hash, key, v);
        }else {
            // 新建一个Node，如果
            tab[i] = newNode(hash, key, v, first);
            // 桶的链表如果大于树化阈值则树化
            if (binCount >= TREEIFY_THRESHOLD - 1){
                treeifyBin(tab, hash);
            }
        }
        // 版本+1
        ++modCount;
        // Map容量+1
        ++size;
        // 插入之后所要做的事儿，这里是空函数
        afterNodeInsertion(true);
        return v;
    }

    // 计算如果存在 example: map.computeIfPresent("aa",(k,v)->null);
    // 根据key查找这个key所在的Node，如果function表达式给的value是null则直接删除node
    // 如果给的value不是null则替换node中的value
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null){
            throw new NullPointerException();
        }
        Node<K,V> e; V oldValue;
        int hash = hash(key);
        if ((e = getNode(hash, key)) != null && (oldValue = e.value) != null) {
            V v = remappingFunction.apply(key, oldValue);
            if (v != null) {
                e.value = v;
                afterNodeAccess(e);
                return v;
            }else{
                //  注意，这是一个移除操作
                removeNode(hash, key, null, false, true);
            }
        }
        return null;
    }

    @Override
    public V compute(K key,BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null){
            throw new NullPointerException();
        }
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n, i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        // 如果为空则先扩容
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0){
            n = (tab = resize()).length;
        }
        // 还是一样先找到key所在的hash桶
        if ((first = tab[i = (n - 1) & hash]) != null) {
            // 树节点则用树的处理方式找，链表则用链表方式找
            if (first instanceof TreeNode){
                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
            } else {
                Node<K,V> e = first; K k;
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        V oldValue = (old == null) ? null : old.value;
        V v = remappingFunction.apply(key, oldValue);
        if (old != null) {
            // 找到node
            // 还是一样，如果function表达式给的value不是null则替换，是null则删除node
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else{
                removeNode(hash, key, null, false, true);
            }
        }else if (v != null) {
            // 未找到node
            if (t != null){
                // 树
                t.putTreeVal(this, tab, hash, key, v);
            }else {
                // 链表?
                tab[i] = newNode(hash, key, v, first);
                if (binCount >= TREEIFY_THRESHOLD - 1){
                    treeifyBin(tab, hash);
                }
            }
            // 版本
            ++modCount;
            // 实际大小
            ++size;
            afterNodeInsertion(true);
        }
        return v;
    }

    // 合并 example: data.merge("aa",99,(k,v)->123);
    // 先找key所在的node，如果找到则用function的value覆盖
    // 如果没找到所在node，则用这个key&value组成一个新的node新增到map
    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new NullPointerException();
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K,V>[] tab; Node<K,V> first; int n, i;
        int binCount = 0;
        TreeNode<K,V> t = null;
        Node<K,V> old = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0){
            n = (tab = resize()).length;
        }
        // 上面已经说了...
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode){
                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
            } else {
                Node<K,V> e = first; K k;
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        if (old != null) {
            V v;
            // 不为null的才会替换未新value
            if (old.value != null){
                v = remappingFunction.apply(old.value, value);
            } else{
                v = value;
            }
            // 有值则替换无值则删除
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            }else{
                removeNode(hash, key, null, false, true);
            }
            return v;
        }
        // 这段代码如果注明 if(old==null) 则就好理解了
        if (value != null) {
            // key&value新增
            if (t != null){
                t.putTreeVal(this, tab, hash, key, value);
            }else {
                tab[i] = newNode(hash, key, value, first);
                if (binCount >= TREEIFY_THRESHOLD - 1){
                    treeifyBin(tab, hash);
                }
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return value;
    }

    // 循环
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Node<K,V>[] tab;
        if (action == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K,V> e = tab[i]; e != null; e = e.next){
                    // 这里的调用accept的两个参数一个是key另一个是value，下面是样例：
                    // map.forEach((k,v)->{
                    //     System.out.println(k+":"+v);
                    // });
                    action.accept(e.key, e.value);
                }
            }
            // 线程不安全的
            if (modCount != mc){
                throw new ConcurrentModificationException();
            }
        }
    }

    // 替换所有key对应的value为function表达式的值
    // example: map.replaceAll((k,v)->99);
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Node<K,V>[] tab;
        if (function == null){
            throw new NullPointerException();
        }
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                    // 顺带一提，apply是将参数应用于匿名函数
                    // example: replaceAll((k,v)-> {
                    //            return 99;
                    //          });
                    e.value = function.apply(e.key, e.value);
                }
            }
            if (modCount != mc){
                throw new ConcurrentModificationException();
            }
        }
    }

    /* ------------------------------------------------------------ */
    // Cloning and serialization

    /**
     * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
     * values themselves are not cloned.
     * 返回此HashMap实例的浅层副本：键和值本身不会被克隆。
     * @return a shallow copy of this map
     *          这个map的浅拷贝
     *
     * 这个拷贝返回的也是一个HashMap对象
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        HashMap<K,V> result;
        try {
            // 调用父类的native的克隆
            result = (HashMap<K,V>)super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        // 初始化配置参数
        result.reinitialize();
        // 将每一项重新put进去以生成配置参数( 见reinitialize() )
        result.putMapEntries(this, false);
        return result;
    }

    // These methods are also used when serializing HashSets
    // 序列化为HashSets会使用这些方法，只返回负载系数
    final float loadFactor() { return loadFactor; }

    // 内部用的方法，返回桶的容量
    final int capacity() {
        // 默认是table的实际容量大小（桶的数量）
        // 也是可以取阈值(threshold),阈值一般都是2的次幂(1、2、4、8、16...) 2^n
        // 最终也可以取到默认初始大小(DEFAULT_INITIAL_CAPACITY=16)
        return (table != null) ? table.length :
                ((threshold > 0) ? threshold : DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
     * serialize it).
     * 将HashMap实例的状态保存到流中（即序列化它）。
     *
     * @serialData The <i>capacity</i> of the HashMap (the length of the
     *             bucket array) is emitted (int), followed by the
     *             <i>size</i> (an int, the number of key-value
     *             mappings), followed by the key (Object) and value (Object)
     *             for each key-value mapping.  The key-value mappings are
     *             emitted in no particular order.
     *  HashMap的容量（bucket数组），然后是size一个int，键值的数目映射），然后是键（Object）和值（Object）
     *  对于每个键值映射。键值映射为不按特定顺序发射。
     *
     *  这个方法似乎没有地方用到呢。。。
     */
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        int buckets = capacity();
        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();
        // 挺奇怪这里函数writeInt入参两次，两个参数的计算方式几乎一致
        s.writeInt(buckets);
        s.writeInt(size);
        internalWriteEntries(s);
    }

    /**
     * Reconstitutes this map from a stream (that is, deserializes it).
     * 从流中重新构造此映射（即，对其进行反序列化）。
     *
     * @param s the stream
     * @throws ClassNotFoundException if the class of a serialized object could not be found
     *                                如果序列化对象不存在
     * @throws IOException if an I/O error occurs IO错误时
     *
     *  这个方法似乎么有调用的地方
     *  将一个序列化对象读取到当前map中
     */
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        // Read in the threshold (ignored), loadfactor, and any hidden stuff\
        // 读取阈值（忽略）、负载系数和任何隐藏的内容
        s.defaultReadObject();
        // 重置配置参数
        reinitialize();
        if (loadFactor <= 0 || Float.isNaN(loadFactor)){
            throw new InvalidObjectException("Illegal load factor: " + loadFactor);
        }
        s.readInt();                // Read and ignore number of buckets
        int mappings = s.readInt(); // Read number of mappings (size) 映射的键值对数量，下面会根据这个映射数读取key&value
        if (mappings < 0){
            throw new InvalidObjectException("Illegal mappings count: " + mappings);
        }else if (mappings > 0) { // (if zero, use defaults)
            // Size the table using given load factor only if within
            // range of 0.25...4.0
            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
            float fc = (float)mappings / lf + 1.0f;
            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
                    DEFAULT_INITIAL_CAPACITY :
                    (fc >= MAXIMUM_CAPACITY) ?
                            MAXIMUM_CAPACITY :
                            tableSizeFor((int)fc));
            float ft = (float)cap * lf;
            threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ? (int)ft : Integer.MAX_VALUE);
            // Check Map.Entry[].class since it's the nearest public type to
            // what we're actually creating.
            SharedSecrets.getJavaOISAccess().checkArray(s, Entry[].class, cap);
            @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] tab = (Node<K,V>[])new Node[cap];
            table = tab;

            // Read the keys and values, and put the mappings in the HashMap
            for (int i = 0; i < mappings; i++) {
                @SuppressWarnings("unchecked")
                K key = (K) s.readObject();
                @SuppressWarnings("unchecked")
                V value = (V) s.readObject();
                putVal(hash(key), key, value, false, false);
            }
        }
    }

    /* ------------------------------------------------------------ */
    // iterators
    // 这个迭代器只是个抽象类，目标是服务于 KeyIterator 、ValueIterator、EntryIterator
    abstract class HashIterator {
        Node<K,V> next;        // next entry to return
        Node<K,V> current;     // current entry
        int expectedModCount;  // for fast-fail
        int index;             // current slot 当前槽也即当前桶的索引

        HashIterator() {
            // 迭代时不允许外部修改删除
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry 前进到第一个条目
                // 反向思维只要取到一个node不是null即为第一个node(current)
                do {} while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            // 迭代时不允许外部删除
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            if (e == null){
                throw new NoSuchElementException();
            }
            // 这个地方有点儿弯弯绕（只是代码写的很凝练紧凑）
            // 做了两件事儿：
            // 1从当前node(节点)滑向下一个node
            // 2.因为bucket（桶）的存在，当当前bucket下的node均循环完了(node=null)则需要将current_node切换到下一个bucket的头节点(node)
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove() {
            Node<K,V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            // 调用的是map的remove
            removeNode(hash(key), key, null, false, false);
            // 这个是重点，为什么我说不允许外部删除
            expectedModCount = modCount;
        }
    }

    final class KeyIterator extends HashIterator implements Iterator<K> {
        public final K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator implements Iterator<V> {
        public final V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator implements Iterator<Entry<K,V>> {
        public final Entry<K,V> next() {
            return nextNode();
        }
    }

    /* ------------------------------------------------------------ */
    // spliterators
    // 这个如同上面的迭代器实现，这里其实也可以看作是一个抽象类是对 KeySpliterator、ValueSpliterator、EntrySpliterator的抽象实现
    static class HashMapSpliterator<K,V> {
        // 这个map是持有当前HashMap的一个引用而已
        final HashMap<K,V> map;
        Node<K,V> current;          // current node 当前节点
        int index;                  // current index, modified on advance/split 当前索引，提前/拆分时修改
        int fence;                  // one past last index  倒数第1个索引
        int est;                    // size estimate 规模估计
        int expectedModCount;       // for comodification checks 用于共聚检查

        HashMapSpliterator(HashMap<K,V> m, int origin,int fence, int est,int expectedModCount) {
            this.map = m;
            this.index = origin;
            // 围栏
            this.fence = fence;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        // 获取围栏
        final int getFence() { // initialize fence and size on first use 首次使用时初始化围栏和大小
            int hi;
            if ((hi = fence) < 0) {
                HashMap<K,V> m = map;
                est = m.size;
                expectedModCount = m.modCount;
                Node<K,V>[] tab = m.table;
                hi = fence = (tab == null) ? 0 : tab.length;
            }
            return hi;
        }

        // 获取大小
        public final long estimateSize() {
            getFence(); // force init
            return (long) est;
        }
    }

    // key拆分器
    static final class KeySpliterator<K,V> extends HashMapSpliterator<K,V> implements Spliterator<K> {
        KeySpliterator(HashMap<K,V> m, int origin, int fence, int est,int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        public KeySpliterator<K,V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new KeySpliterator<>(map, lo, index = mid, est >>>= 1,
                            expectedModCount);
        }

        public void forEachRemaining(Consumer<? super K> action) {
            // i是循环变量 hi表示桶的大小 mc是版本
            int i, hi, mc;
            // 动作不能为空
            if (action == null){
                throw new NullPointerException();
            }
            HashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            }else{
                mc = expectedModCount;
            }
            if (tab != null && tab.length >= hi && (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null){
                        p = tab[i++];
                    }else {
                        // 这里应用每一个key
                        action.accept(p.key);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                // 也是不允许迭代时修改
                if (m.modCount != mc){
                    throw new ConcurrentModificationException();
                }
            }
        }

        // 尝试前进
        public boolean tryAdvance(Consumer<? super K> action) {
            int hi;
            if (action == null){
                throw new NullPointerException();
            }
            Node<K,V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                // 如果当前map不是空，则while只循环两次，action只应用第一个key
                while (current != null || index < hi) {
                    if (current == null){
                        current = tab[index++];
                    } else {
                        K k = current.key;
                        current = current.next;
                        action.accept(k);
                        if (map.modCount != expectedModCount){
                            throw new ConcurrentModificationException();
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        // 特征
        public int characteristics() {
            // 结果是65
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }

    static final class ValueSpliterator<K,V> extends HashMapSpliterator<K,V> implements Spliterator<V> {
        ValueSpliterator(HashMap<K,V> m, int origin, int fence, int est, int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        // 尝试返回一个value的spliter,如果当前map不是空都是有返回的
        public ValueSpliterator<K,V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new ValueSpliterator<>(map, lo, index = mid, est >>>= 1, expectedModCount);
        }

        // 循环Set中每一个元素的value并应用与action
        public void forEachRemaining(Consumer<? super V> action) {
            int i, hi, mc;
            if (action == null){
                throw new NullPointerException();
            }
            HashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            } else{
                mc = expectedModCount;
            }
            if (tab != null && tab.length >= hi && (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p.value);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        // 尝试迭代一个节点，如果为空或者action为null则返回false，action只应用头节点的value
        public boolean tryAdvance(Consumer<? super V> action) {
            int hi;
            if (action == null){
                throw new NullPointerException();
            }
            Node<K,V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                while (current != null || index < hi) {
                    if (current == null)
                        current = tab[index++];
                    else {
                        V v = current.value;
                        current = current.next;
                        action.accept(v);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }

        // 这个应该是给父类使用 标识拆分大小默认64
        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
        }
    }

    // HashMap的entity的splitertor的实现
    static final class EntrySpliterator<K,V> extends HashMapSpliterator<K,V> implements Spliterator<Entry<K,V>> {
        EntrySpliterator(HashMap<K,V> m, int origin, int fence, int est, int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        public EntrySpliterator<K,V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new EntrySpliterator<>(map, lo, index = mid, est >>>= 1, expectedModCount);
        }

        public void forEachRemaining(Consumer<? super Entry<K,V>> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            HashMap<K,V> m = map;
            Node<K,V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            }
            else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K,V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super Entry<K,V>> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K,V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                while (current != null || index < hi) {
                    if (current == null)
                        current = tab[index++];
                    else {
                        Node<K,V> e = current;
                        current = current.next;
                        action.accept(e);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }

        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }

    /* ------------------------------------------------------------ */
    // LinkedHashMap support
    // 因为LinkedHashMap有继承HashMap，所以如下方法会被LinkedHashMap中的覆盖

    /*
     * The following package-protected methods are designed to be
     * overridden by LinkedHashMap, but not by any other subclass.
     * Nearly all other internal methods are also package-protected
     * but are declared final, so can be used by LinkedHashMap, view
     * classes, and HashSet.
     *  以下包装保护方法旨在被LinkedHashMap覆盖，但不被任何其他子类覆盖。
     *  几乎所有其他内部方法也受到包保护但是被声明为final，因此可以由LinkedHashMap、视图使用类和HashSet。
     */

    // Create a regular (non-tree) node
    // 创建一个非树的节点（普通节点）
    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    // For conversion from TreeNodes to plain nodes
    // 用于从树节点转换为普通节点
    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
        return new Node<>(p.hash, p.key, p.value, next);
    }

    // Create a tree bin node   创建树箱节点
    TreeNode<K,V> newTreeNode(int hash, K key, V value, Node<K,V> next) {
        return new TreeNode<>(hash, key, value, next);
    }

    // For treeifyBin 替换为树节点
    TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
        return new TreeNode<>(p.hash, p.key, p.value, next);
    }

    /**
     * Reset to initial default state.  Called by clone and readObject.
     *  重置为初始默认状态。由clone和readObject调用。
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

    // 以下三个方法均是空是实现，具体实现是在LinkedHashMap中
    // Callbacks to allow LinkedHashMap post-actions 回调以允许LinkedHashMap发布操作
    void afterNodeAccess(Node<K,V> p) { }
    void afterNodeInsertion(boolean evict) { }
    void afterNodeRemoval(Node<K,V> p) { }

    // Called only from writeObject, to ensure compatible ordering.
    // 仅从writeObject调用，以确保排序兼容。
    // 内部写入条目
    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        Node<K,V>[] tab;
        if (size > 0 && (tab = table) != null) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                    // 循环每个桶里面的每一个Node数组，获取其key&value并写入
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }

    /* ------------------------------------------------------------ */
    // Tree bins

    /**
     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
     * extends Node) so can be used as extension of either regular or
     * linked node.
     *  Tree bin的条目。扩展LinkedHashMap.Entry（反过来extends Node），因此可以用作正则或链接节点
     */
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links  红黑树链接
        TreeNode<K,V> left; // 左节点
        TreeNode<K,V> right; // 右节点
        TreeNode<K,V> prev;    // needed to unlink next upon deletion   删除后需要取消下一个链接
        boolean red;    // 红黑树
        TreeNode(int hash, K key, V val, Node<K,V> next) {
            super(hash, key, val, next);
        }

        /**
         * Returns root of tree containing this node.
         *  返回包含此节点的树的根
         */
        final TreeNode<K,V> root() {
            // 一级级网上找(parent),直到parent=null则返回这个root节点
            for (TreeNode<K,V> r = this, p;;) {
                if ((p = r.parent) == null){
                    return r;
                }
                r = p;
            }
        }

        /**
         * Ensures that the given root is the first node of its bin.
         *  确保给定的根是其bin的第一个节点
         */
        static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root) {
            int n;
            if (root != null && tab != null && (n = tab.length) > 0) {
                // 计算root所在桶的位置
                int index = (n - 1) & root.hash;
                TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
                // 对象是否一致是可以直接比较
                if (root != first) {
                    Node<K,V> rn;
                    // 重新赋值 ？？？
                    tab[index] = root;
                    TreeNode<K,V> rp = root.prev;
                    // root的前驱节点与root的后继节点相互绑定
                    if ((rn = root.next) != null){
                        ((TreeNode<K,V>)rn).prev = rp;
                    }
                    if (rp != null){
                        rp.next = rn;
                    }
                    // 节点做双向绑定 old_root(first)与root(传入的)
                    if (first != null){
                        first.prev = root;
                    }
                    root.next = first;
                    // 如果是桶的root节点自然它的前驱节点就是null
                    root.prev = null;
                }
                // 检查不变量 后面会说
                assert checkInvariants(root);
            }
        }

        /**
         * Finds the node starting at root p with the given hash and key.
         * The kc argument caches comparableClassFor(key) upon first use
         * comparing keys.
         *  查找从根p开始并具有给定哈希和密钥的节点。
         *  kc参数在首次使用时缓存comparableClassFor（键）比较键。
         *  h: 节点key的hash
         *  k: key本身
         *  kc: 传入的key的类Class对象
         *  当前方法是节点的查找方法,找不到返回null
         */
        final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
            TreeNode<K,V> p = this;
            do {
                int ph, dir; K pk;
                TreeNode<K,V> pl = p.left, pr = p.right, q;
                if ((ph = p.hash) > h) // 小于当前节点hash的取节点左子节点
                    p = pl;
                else if (ph < h)    // 大于当前节点hash的取右子节点
                    p = pr;
                else if ((pk = p.key) == k || (k != null && k.equals(pk))) // 找到了直接返回这个节点
                    return p;
                else if (pl == null) // 左子节点为null的取右子节点
                    p = pr;
                else if (pr == null) // 右子节点为null的取左子节点
                    p = pl;
                else if ( (kc != null || (kc = comparableClassFor(k)) != null) && // 先检查传入的key的（kc）类型是否为null
                          (dir = compareComparables(kc, k, pk)) != 0 )  // 主要比较k与pk（按ascii位比较相等为0）
                    p = (dir < 0) ? pl : pr; // dir小于零则k在p的左节点否则是右节点（寻找）
                else if ((q = pr.find(h, k, kc)) != null) //直接从右子节点寻找，找到则返回节点
                    return q;
                else
                    p = pl; // 置为左子节点
            } while (p != null);
            return null;
        }

        /**
         * Calls find for root node.
         *  调用查找根节点
         *  从根节点开始查找指定key所在的节点
         */
        final TreeNode<K,V> getTreeNode(int h, Object k) {
            return ((parent != null) ? root() : this).find(h, k, null);
        }

        /**
         * Tie-breaking utility for ordering insertions when equal
         * hashCodes and non-comparable. We don't require a total
         * order, just a consistent insertion rule to maintain
         * equivalence across rebalancings. Tie-breaking further than
         * necessary simplifies testing a bit.
         * 当hashCodes相等且不可比较时，用于排序插入的平局打破实用程序。
         * 我们不需要总顺序，只需要一个一致的插入规则来保持重新平衡的等价性。进一步打破平局比必要的简化了一点测试。
         * 平局决胜令??? 具体用处后面会说e
         */
        static int tieBreakOrder(Object a, Object b) {
            int d;
            // a或b为null的或类型一致的 计算hashcode并比较大小，最终返回的只可能是-1或1
            if (a == null || b == null || (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0){
                d = (System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1);
            }
            return d;
        }

        /**
         * Forms tree of the nodes linked from this node.
         *  形成从该节点链接的节点的树
         *  使树化
         */
        final void treeify(Node<K,V>[] tab) {
            TreeNode<K,V> root = null;
            // x始终是当前节点 next则是x的后继节点
            for (TreeNode<K,V> x = this, next; x != null; x = next) {
                next = (TreeNode<K,V>)x.next;
                // 重置x的左右子节点为null
                x.left = x.right = null;
                if (root == null) {
                    x.parent = null;
                    x.red = false;
                    root = x;
                } else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    // 从根节点开始~
                    for (TreeNode<K,V> p = root;;) {
                        int dir, ph;
                        K pk = p.key;
                        // 这几行判断主要计算dir值
                        // dir是后续节点位置的依据
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        else if ( (kc == null && (kc = comparableClassFor(k)) == null) ||
                                  (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);

                        TreeNode<K,V> xp = p; // p就是root？？？
                        if ( (p = (dir <= 0) ? p.left : p.right) == null ) {
                            x.parent = xp;
                            if (dir <= 0){
                                xp.left = x;
                            } else {
                                xp.right = x;
                            }
                            // 平衡插入
                            root = balanceInsertion(root, x);
                            break;
                        }
                    }
                }
            }
            // 调整根节点位置
            moveRootToFront(tab, root);
        }

        /**
         * Returns a list of non-TreeNodes replacing those linked from
         * this node.
         *   返回替换从此节点链接的非树节点的列表。
         *   是不是将树退化为链表结构??? todo
         */
        final Node<K,V> untreeify(HashMap<K,V> map) {
            Node<K,V> hd = null, tl = null;
            for (Node<K,V> q = this; q != null; q = q.next) {
                Node<K,V> p = map.replacementNode(q, null);
                if (tl == null)
                    hd = p;
                else
                    tl.next = p;
                tl = p;
            }
            // 返回的是第一个节点
            return hd;
        }

        /**
         * Tree version of putVal.
         *  putVal的树版本。
         *  向树添加一个节点
         */
        final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab, int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            // 获取树的根节点
            TreeNode<K,V> root = (parent != null) ? root() : this;
            for (TreeNode<K,V> p = root;;) {
                int dir, ph; K pk;
                // 这一片是计算节点的权重（dir）
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p; // 这个是返回原节点
                else if ((kc == null && (kc = comparableClassFor(k)) == null) ||
                        (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K,V> q, ch;
                        searched = true;
                        // 从左节点搜索q并直接返回
                        if (((ch = p.left) != null && (q = ch.find(h, k, kc)) != null) ||
                                ((ch = p.right) != null && (q = ch.find(h, k, kc)) != null)){
                            return q;
                        }
                    }
                    // 从当前节点的key以及传入的key计算dir（路径权重）
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K,V> xpn = xp.next;
                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
                    // 新节点的插入位置(by dir)
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    xp.next = x; // 插入到p节点之后
                    x.parent = x.prev = xp; // 新增节点的父节点也即它的前驱节点同时也是当前节点p(xp=p)
                    if (xpn != null){
                        ((TreeNode<K,V>)xpn).prev = x;
                    }
                    // 调整根节点同时平衡插入节点
                    moveRootToFront(tab, balanceInsertion(root, x));
                    // 新增插入返回的是null(没有重复节点时)
                    return null;
                }
            }
        }

        /**
         * Removes the given node, that must be present before this call.
         * This is messier than typical red-black deletion code because we
         * cannot swap the contents of an interior node with a leaf
         * successor that is pinned by "next" pointers that are accessible
         * independently during traversal. So instead we swap the tree
         * linkages. If the current tree appears to have too few nodes,
         * the bin is converted back to a plain bin. (The test triggers
         * somewhere between 2 and 6 nodes, depending on tree structure).
         *  删除给定的节点，该节点必须在此调用之前存在。这比典型的红黑删除代码更混乱，因为我们不能将内部节点的内容与叶后继节点交换，
         *  后者由遍历期间可独立访问的“下一个”指针固定。因此，我们交换了树链接。如果当前树的节点太少，则bin将转换回普通bin。
         *  （根据树结构，测试会在2到6个节点之间触发）。
         */
        final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab, boolean movable) {
            // 这个n即是桶的大小
            int n;
            if (tab == null || (n = tab.length) == 0){
                return;
            }
            // index是用于计算key所在桶的位置
            int index = (n - 1) & hash;
            TreeNode<K,V> first = (TreeNode<K,V>)tab[index], root = first, rl;
            TreeNode<K,V> succ = (TreeNode<K,V>)next, pred = prev;
            if (pred == null)
                tab[index] = first = succ;
            else
                pred.next = succ;
            if (succ != null)
                succ.prev = pred;
            if (first == null)
                return;
            if (root.parent != null)
                root = root.root();
            if (root == null || (
                    movable && (root.right == null || (rl = root.left) == null || rl.left == null) )) {
                // first为当前桶的首位置 ，这里应该是做的树退化为链表的操作.
                tab[index] = first.untreeify(map);  // too small
                return;
            }
            TreeNode<K,V> p = this, pl = left, pr = right, replacement;
            if (pl != null && pr != null) {
                TreeNode<K,V> s = pr, sl;
                // 左节点不为空s就是它
                while ((sl = s.left) != null) // find successor
                    s = sl;
                // s的颜色变为p的颜色 p则变为c的颜色
                boolean c = s.red; s.red = p.red; p.red = c; // swap colors 交换颜色
                TreeNode<K,V> sr = s.right;
                TreeNode<K,V> pp = p.parent;
                if (s == pr) { // p was s's direct parent   p是s的直接父母
                    // 调整节点位置
                    p.parent = s;
                    s.right = p;
                } else {
                    TreeNode<K,V> sp = s.parent;
                    if ((p.parent = sp) != null) {
                        // 调整p为sp的子节点
                        if (s == sp.left)
                            sp.left = p;
                        else
                            sp.right = p;
                    }
                    // s为根节点
                    if ((s.right = pr) != null)
                        pr.parent = s;
                }
                // 复杂的调整 todo...
                p.left = null;
                if ((p.right = sr) != null)
                    sr.parent = p;
                if ((s.left = pl) != null)
                    pl.parent = s;
                if ((s.parent = pp) == null)
                    root = s;
                else if (p == pp.left)
                    pp.left = s;
                else
                    pp.right = s;
                if (sr != null)
                    replacement = sr;
                else
                    replacement = p;
            }
            else if (pl != null)
                replacement = pl;
            else if (pr != null)
                replacement = pr;
            else
                replacement = p;
            if (replacement != p) {
                // 不是当前节点p的就要向左或者右子节点调整
                TreeNode<K,V> pp = replacement.parent = p.parent;
                if (pp == null)
                    root = replacement;
                else if (p == pp.left)
                    pp.left = replacement;
                else
                    pp.right = replacement;
                // 当前节点去掉连接关系
                p.left = p.right = p.parent = null;
            }

            TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);
            if (replacement == p) {  // detach 移除
                TreeNode<K,V> pp = p.parent;
                p.parent = null;
                // 移除p与pp的关系
                if (pp != null) {
                    if (p == pp.left)
                        pp.left = null;
                    else if (p == pp.right)
                        pp.right = null;
                }
            }
            if (movable){
                moveRootToFront(tab, r);
            }
        }

        /**
         * Splits nodes in a tree bin into lower and upper tree bins,
         * or untreeifies if now too small. Called only from resize;
         * see above discussion about split bits and indices.
         *  将树容器中的节点拆分为较低的树容器和较高的树容器，如果现在太小，则取消测试。仅从resize调用；请参阅上面关于拆分位和索引的讨论。
         *
         * @param map the map HashMap对象
         * @param tab the table for recording bin heads 记录仓头的表格
         * @param index the index of the table being split  要拆分的表的索引
         * @param bit the bit of hash to split on   要拆分的哈希位
         */
        final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
            TreeNode<K,V> b = this;
            // Relink into lo and hi lists, preserving order    重新链接到lo和hi列表，保持顺序
            TreeNode<K,V> loHead = null, loTail = null;
            TreeNode<K,V> hiHead = null, hiTail = null;
            int lc = 0, hc = 0;
            // 默认e和b都是this（TreeNode<K,V>）
            for ( TreeNode<K,V> e = b, next; e != null; e = next ) {
                next = (TreeNode<K,V>)e.next;
                e.next = null; // 解除关联
                if ((e.hash & bit) == 0) {
                    // 设置低位
                    if ((e.prev = loTail) == null){
                        loHead = e;
                    } else{
                        loTail.next = e;
                    }
                    loTail = e;
                    // 记录层数
                    ++lc;
                } else {
                    // 设置高位
                    if ((e.prev = hiTail) == null){
                        hiHead = e;
                    }else{
                        hiTail.next = e;
                    }
                    hiTail = e;
                    ++hc;
                }
            }

            if (loHead != null) {
                // UNTREEIFY_THRESHOLD 未试验阈值
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);
                else {
                    tab[index] = loHead;
                    if (hiHead != null) // (else is already treeified)  其他已被树化
                        loHead.treeify(tab);
                }
            }
            if (hiHead != null) {
                // 索引位置+拆分位
                if (hc <= UNTREEIFY_THRESHOLD){
                    tab[index + bit] = hiHead.untreeify(map);
                } else {
                    tab[index + bit] = hiHead;
                    if (loHead != null)
                        hiHead.treeify(tab);
                }
            }
        }

        /* ------------------------------------------------------------ */
        // Red-black tree methods, all adapted from CLR
        // 红黑树方法，全部改编自CLR

        // 红黑树左旋
        static <K,V> TreeNode<K,V> rotateLeft( TreeNode<K,V> root,TreeNode<K,V> p ) {
            TreeNode<K,V> r, pp, rl;
            // 只在右节点不为null时才需调整
            if (p != null && (r = p.right) != null) {
                if ((rl = p.right = r.left) != null)
                    rl.parent = p;
                // 红黑树左旋并不代表右侧节点不动
                if ((pp = r.parent = p.parent) == null)
                    (root = r).red = false; // 父节点/根节点为黑
                else if (pp.left == p)
                    pp.left = r;
                else
                    pp.right = r;
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        // 红黑树右旋
        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root, TreeNode<K,V> p) {
            TreeNode<K,V> l, pp, lr;
            if (p != null && (l = p.left) != null) {
                if ((lr = p.left = l.right) != null)
                    lr.parent = p;
                if ((pp = l.parent = p.parent) == null)
                    (root = l).red = false; // ??? todo...
                else if (pp.right == p)
                    pp.right = l;
                else
                    pp.left = l;
                l.right = p;
                p.parent = l;
            }
            return root;
        }

        // 平衡插入
        static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root, TreeNode<K,V> x) {
            x.red = true;
            for (TreeNode<K,V> xp, xpp, xppl, xppr;;) {
                if ((xp = x.parent) == null) {
                    x.red = false; // 根节点必须是黑
                    return x;
                } else if (!xp.red || (xpp = xp.parent) == null){ // xp.parent = x.parent
                    return root;
                }
                if (xp == (xppl = xpp.left)) {
                    // 右节点不为null的且当前xppr为红的需要重新染色
                    if ( (xppr = xpp.right) != null && xppr.red ) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {
                        // 向右插入时左旋
                        if (x == xp.right) {
                            root = rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                } else {
                    if (xppl != null && xppl.red) {
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {
                        // 左边插入的右旋
                        if (x == xp.left) {
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }

        // 删除后平衡树
        static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root, TreeNode<K,V> x) {
            for (TreeNode<K,V> xp, xpl, xpr;;) {
                if (x == null || x == root){
                    return root;
                } else if ((xp = x.parent) == null) {
                    x.red = false;// root node ?
                    return x;
                } else if (x.red) {
                    // 仅切换颜色
                    x.red = false;
                    return root;
                } else if ((xpl = xp.left) == x) {
                    if ((xpr = xp.right) != null && xpr.red) {
                        // 右节点为红节点时 调整节点为黑节点 同时左旋
                        xpr.red = false;
                        xp.red = true;
                        root = rotateLeft(root, xp);
                        xpr = (xp = x.parent) == null ? null : xp.right;
                    }
                    if (xpr == null){
                        x = xp;
                    } else {
                        // 右节点不为null时
                        TreeNode<K,V> sl = xpr.left, sr = xpr.right;
                        if ((sr == null || !sr.red) && (sl == null || !sl.red)) {
                            xpr.red = true; // 调整颜色
                            x = xp;
                        } else {
                            if (sr == null || !sr.red) {
                                // 右节点为空或节点为黑时 右旋
                                if (sl != null){
                                    sl.red = false;
                                }
                                xpr.red = true;
                                root = rotateRight(root, xpr);
                                xpr = (xp = x.parent) == null ? null : xp.right;
                            }
                            if (xpr != null) {
                                // 仅着色
                                xpr.red = (xp == null) ? false : xp.red;
                                if ((sr = xpr.right) != null){
                                    sr.red = false;
                                }
                            }
                            if (xp != null) {
                                // 当前节点着色并左移
                                xp.red = false;
                                root = rotateLeft(root, xp);
                            }
                            x = root;
                        }
                    }
                } else { // symmetric   对称的
                    if (xpl != null && xpl.red) {
                        xpl.red = false;
                        xp.red = true;
                        root = rotateRight(root, xp); // 右旋
                        // 重新赋值
                        xpl = (xp = x.parent) == null ? null : xp.left;
                    }
                    if (xpl == null){
                        x = xp;
                    } else {
                        TreeNode<K,V> sl = xpl.left, sr = xpl.right;
                        if ((sl == null || !sl.red) && (sr == null || !sr.red)) {
                            xpl.red = true;
                            x = xp;
                        }
                        else {
                            if (sl == null || !sl.red) {
                                if (sr != null){
                                    sr.red = false;
                                }
                                xpl.red = true;
                                root = rotateLeft(root, xpl);// 左旋
                                xpl = (xp = x.parent) == null ? null : xp.left;
                            }
                            if (xpl != null) {
                                xpl.red = (xp == null) ? false : xp.red;
                                if ((sl = xpl.left) != null)
                                    sl.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateRight(root, xp);// 右旋
                            }
                            x = root;
                        }
                    }
                }
            }
        }

        /**
         * Recursive invariant check
         *  递归不变检查
         *  检查不变量
         */
        static <K,V> boolean checkInvariants(TreeNode<K,V> t) {
            TreeNode<K,V> tp = t.parent,
                    tl = t.left, tr = t.right,
                    tb = t.prev, tn = (TreeNode<K,V>)t.next;
            if (tb != null && tb.next != t)
                return false;
            if (tn != null && tn.prev != t)
                return false;
            if (tp != null && t != tp.left && t != tp.right)
                return false;
            if (tl != null && (tl.parent != t || tl.hash > t.hash)) // hash用于确定寻找路径
                return false;
            if (tr != null && (tr.parent != t || tr.hash < t.hash))
                return false;
            if (t.red && tl != null && tl.red && tr != null && tr.red)
                return false;
            if (tl != null && !checkInvariants(tl))
                return false;
            if (tr != null && !checkInvariants(tr))
                return false;
            return true;
        }
    }

}
