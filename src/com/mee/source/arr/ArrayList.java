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

package com.mee.source.arr;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.SharedSecrets;

/**
 * Resizable-array implementation of the <tt>List</tt> interface.  Implements
 * all optional list operations, and permits all elements, including
 * <tt>null</tt>.  In addition to implementing the <tt>List</tt> interface,
 * this class provides methods to manipulate the size of the array that is
 * used internally to store the list.  (This class is roughly equivalent to
 * <tt>Vector</tt>, except that it is unsynchronized.)
 * List 接口的可调整大小的数组实现。实现所有可选列表操作，并允许所有元素，包括 null。除了实现 List 接口之外，该类还提供了一些方法来操作内部用于存储列表的数组的大小。
 * （这个类大致相当于 Vector，只是它是不同步的。）
 *
 * <p>The <tt>size</tt>, <tt>isEmpty</tt>, <tt>get</tt>, <tt>set</tt>,
 * <tt>iterator</tt>, and <tt>listIterator</tt> operations run in constant
 * time.  The <tt>add</tt> operation runs in <i>amortized constant time</i>,
 * that is, adding n elements requires O(n) time.  All of the other operations
 * run in linear time (roughly speaking).  The constant factor is low compared
 * to that for the <tt>LinkedList</tt> implementation.
 * size、isEmpty、get、set、iterator 和 listIterator 操作在恒定时间内运行。添加操作在摊销常数时间内运行，即添加 n 个元素需要 O(n) 时间。
 * 所有其他操作都以线性时间运行（粗略地说）。与 LinkedList 实现相比，常数因子较低。
 *
 * <p>Each <tt>ArrayList</tt> instance has a <i>capacity</i>.  The capacity is
 * the size of the array used to store the elements in the list.  It is always
 * at least as large as the list size.  As elements are added to an ArrayList,
 * its capacity grows automatically.  The details of the growth policy are not
 * specified beyond the fact that adding an element has constant amortized
 * time cost.
 * 每个 ArrayList 实例都有一个容量。容量是用于存储列表中元素的数组的大小。它总是至少与列表大小一样大。随着元素被添加到 ArrayList，它的容量会自动增长。
 * 除了添加元素具有恒定的摊销时间成本这一事实之外，没有指定增长策略的细节。
 *
 * <p>An application can increase the capacity of an <tt>ArrayList</tt> instance
 * before adding a large number of elements using the <tt>ensureCapacity</tt>
 * operation.  This may reduce the amount of incremental reallocation.
 * 应用程序可以在使用 ensureCapacity 操作添加大量元素之前增加 ArrayList 实例的容量。这可以减少增量重新分配的数量。
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access an <tt>ArrayList</tt> instance concurrently,
 * and at least one of the threads modifies the list structurally, it
 * <i>must</i> be synchronized externally.  (A structural modification is
 * any operation that adds or deletes one or more elements, or explicitly
 * resizes the backing array; merely setting the value of an element is not
 * a structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the list.
 *
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:<pre>
 *   List list = Collections.synchronizedList(new ArrayList(...));</pre>
 * 注意，此实现不同步(就是不保证多线程安全)。 如果多个线程同时访问一个 ArrayList 实例，并且至少有一个线程在结构上修改了列表，则必须在外部进行同步。
 * （结构修改是添加或删除一个或多个元素，或显式调整后备数组大小的任何操作；仅设置元素的值不是结构修改。）这通常通过同步一些自然封装的对象来完成 列表。
 * 如果不存在这样的对象，则应使用 Collections.synchronizedList 方法“包装”该列表。 这最好在创建时完成，以防止对列表的意外不同步访问：
 *  List list = Collections.synchronizedList(new ArrayList(...));
 * 笔记：ArrayList不保证线程安全，如果需要线程安全请使用原生封装的对象来操作(比如CopyOnWriteArrayList)
 *      如果，没有上述所说的安全对象则可以通过Collections.synchronizedList(列表对象)来实现外部同步
 *      这个同步主要是针对于结构同步，这个结构同步是什么呢：就是对数组大小有影响的操作(添加元素不包含在内)
 *
 * <p><a name="fail-fast">
 * The iterators returned by this class's {@link #iterator() iterator} and
 * {@link #listIterator(int) listIterator} methods are <em>fail-fast</em>:</a>
 * if the list is structurally modified at any time after the iterator is
 * created, in any way except through the iterator's own
 * {@link ListIterator#remove() remove} or
 * {@link ListIterator#add(Object) add} methods, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of
 * concurrent modification, the iterator fails quickly and cleanly, rather
 * than risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 * 此类的 iterator 和 listIterator 方法返回的迭代器是快速失败的(个人理解是并发抛错)：如果在创建迭代器后的任何时间对列表进行结构修改，
 * 除了通过迭代器自己的 remove 或 add 方法之外的任何方式(就是使用iterator迭代的时候除了迭代器自己的remove或add之外的改变数组大小的行为均会跑错)，
 * 迭代器将抛出 ConcurrentModificationException。
 * 因此，面对并发修改，迭代器快速而干净地失败(就是多线程并发时一般会抛错，这是不安全的行为也不是每次都出现)，而不是在未来不确定的时间冒任意的、非确定性的行为。
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:  <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 * 请注意，不能保证迭代器的快速失败行为，因为一般来说，在存在不同步的并发修改的情况下，不可能做出任何硬保证。
 * 快速失败的迭代器会尽最大努力抛出 ConcurrentModificationException。
 * 因此，编写一个依赖于这个异常的正确性的程序是错误的：迭代器的快速失败行为应该只用于检测错误。
 * 笔记：就是并发不同步的修改不保证一定不抛错/抛错，程序的正确与否并不能依赖于此错误作为判断，错误的迭代行为能能用于检测错误本身
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 * 此类是 Java 集合框架的成员。
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Collection
 * @see     List
 * @see     LinkedList
 * @see     Vector
 * @since   1.2
 */

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * Default initial capacity.
     * 默认的初始化容量大小
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Shared empty array instance used for empty instances.
     * 用於空實例的共享空數組實例。
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     * 用于默认大小的空实例的共享空数组实例。我们将其与 EMPTY_ELEMENTDATA 区分开来，以了解添加第一个元素时要膨胀多少。
     * 默认 new ArrayList() 时内部的数组就是它
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     * 存儲 ArrayList 元素的數組緩衝區。 ArrayList 的容量就是這個數組緩衝區的長度。當添加第一個元素時，任何具有 elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA 的空 ArrayList 都將擴展為 DEFAULT_CAPACITY。
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * The size of the ArrayList (the number of elements it contains).
     * ArrayList 的大小（它包含的元素數量）。
     * 当ArrayList实例化时这个size就是0
     * 注意: 这个size就是当前数组元素的个数，比如这个数组容量是10,当前只有两个元素，则这个size=2
     *      size记录的就是当前数组实际元素的个数，每add一个元素这个 size++
     * @serial
     */
    private int size;

    /**
     * Constructs an empty list with the specified initial capacity.
     * 构造一个给定初始容量的空数组(列表)
     *
     * @param  initialCapacity  the initial capacity of the list
     *                          初始化容量的列表
     * @throws IllegalArgumentException if the specified initial capacity is negative
     *                      如果初始容量是负数（则抛错）
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     * 構造一個初始容量為 10 的空列表。
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public ArrayList(Collection<? extends E> c) {
        // 这个其实很有趣
        // a.他是将c中所有元素拷贝到一个新数组中，这个数组按 min(数组容量,数组元素个数) 作为新数组大小
        // b.拷贝后的list与原list没有任何关系，也没有引用关系
        // c.这个拷贝是通过底层(native) System.arraycopy(...) 来实现的
        // d.
        Object[] a = c.toArray();
        // 下面做了两个操作是
        // 1. 将当前list维护的数组元素个数赋值为a的(实际)元素的个数(大小)
        // 2. 如果c的类型与当前类型(ArrayList.class)不一致时会进行一次拷贝
        //   2.1 这是一个深(native)拷贝 ，调用的是System.arraycopy(...)
        //   2.2 深拷贝返回的数组大小为实际a的元素的个数，也就是size(size被赋值为a.length)
        //   2.3 这个拷贝还有一个意义是将数组a的类型转换为Object[].class
        //   2.4 顺带说一下: 在ArrayList或者Arrays类中调用的 Arrays.copyOf() 函数始终返回的是实际元素个数大小的数组
        //       也是前两个元素最小的的大小 min(a.length,size) ,因为实际传入的一个是原始数组即大小以及数组实际元素个数的大小
        //       当然这也仅仅是个人猜测哈（^。^) 哈哈哈
        if ((size = a.length) != 0) {
            if (c.getClass() == ArrayList.class) {
                elementData = a;
            } else {
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            // replace with empty array. 替换为空数组
            // 走到这里的必要条件是：当前list的数组与a这个数组大小一样并且其大小为0（就是两个空数组）
            elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * Trims the capacity of this <tt>ArrayList</tt> instance to be the
     * list's current size.  An application can use this operation to minimize
     * the storage of an <tt>ArrayList</tt> instance.
     * 将此 ArrayList 实例的容量修剪为列表的当前大小。应用程序可以使用此操作来最小化 ArrayList 实例的存储。
     */
    public void trimToSize() {
        // 记录一下修改的次数，数组内元素增减都会记录
        modCount++;
        // size代表的是当前实际元素的个数
        // elementData.length 其实也就是 capacity
        // 这里的意思应该是压缩数组的大小，将数组的大小压缩为实际元素个数的大小 也就是使 size=capacity
        // 而且是使用底层拷贝(深拷贝)的方式
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    /**
     * Increases the capacity of this <tt>ArrayList</tt> instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     * 如有必要，增加此 ArrayList 实例的容量，以确保它至少可以容纳最小容量参数指定的元素数量。
     *
     * @param   minCapacity   the desired minimum capacity
     * 参数：  minCapacity – 所需的最小容量
     */
    public void ensureCapacity(int minCapacity) {
        // 最小展开
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                // any size if not default element table
                ? 0
                // larger than default for default empty table. It's already
                // supposed to be at default size.
                : DEFAULT_CAPACITY;
        // 这是当前数组有元素的时候扩，或者当前数组为默认数组且>容量(空数组容量为10) 的时候扩
        // 也就是空数组(他的默认容量就是10) 且需扩容的大小 < 10 的时候 不用扩，空数组扩无意义
        if (minCapacity > minExpand) {
            // 确保显式容量
            ensureExplicitCapacity(minCapacity);
        }
    }

    // 计算容量
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        // 如果当前数组(elementData)为完全的空(无元素且容量为0) 计算出来的元素最小为10
        // 当然如果走的是addAll，minCapacity=size+size(addAll(..))
        // 如果走的是add函数，minCapacity=size+1
        // 总之就是 当前数组为空的时候 要一次性保证容量一次性能容纳add进来的元素，主要保证了 空数组+addAll的场景下 一次就能放得下
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    // 确保容纳的能力
    private void ensureCapacityInternal(int minCapacity) {
        // calculateCapacity计算出来的容量大小最低为10
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    // 確保顯式容量
    private void ensureExplicitCapacity(int minCapacity) {
        // 这个变量记录的是当前活动数组被修改的次数
        // 每添加一个(准确的说是一次)元素修改次数+1，如果是addAll也算做是+1
        modCount++;
        // overflow-conscious code 判断是否溢出
        // 可以简单的理解 minCapacity 为当前需要保证的最小容量(具体大小为当前容量+1:这是对于当前add元素的个数而定的)，elementData.length则为当前活动数组的容量
        // minCapacity 也为添加元素后所需数组容量大小，如果(所需容量)大于当前(添加前)数组容量即需要<b>扩容</b>
        if (minCapacity - elementData.length > 0)
            grow(minCapacity); //增长
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     * 要分配的数组的最大大小。一些 VM 在数组中保留一些标题字。尝试分配更大的数组可能会导致 OutOfMemoryError：请求的数组大小超过 VM 限制
     * 这个地方很有意思，也让人较为费解
     *   首先当前数组最大容量其实是 Integer.MAX_VALUE ,这里再使用 MAX_ARRAY_SIZE 定义一个MAX_VALUE-8的域值实在多余
     *   仅个人理解 MAX_ARRAY_SIZE 其实是一个预警值...
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     * 增加容量以確保它至少可以容納最小容量參數指定的元素數量。
     * @param minCapacity the desired minimum capacity 所需的最小容量(也即当前需要的容量大小)
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        // 这里的增长策略是 oldCapacity=10 -> newCapacity=15 oldCapacity=9 -> newCapacity=14
        // 即 每一次增长的为上一次的一半
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // 这里个人觉得只是一个保险，对于类似addAll这样的操作 newCapacity 可能小于一次add的数量
        // 比如当前容量是10[oldCapacity:10->newCapacity:15],addAll(100)后所需的容量还是不够 这时就会出现[newCapacity:100]
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        // 这里也是一个保险，对于待扩容后的大小比数组最大(MAX_ARRAY_SIZE)还要大的时候启用hugeCapacity(minCapacity)
        // 这里调用 hugeCapacity 后顶多扩容8个大小 MAX_ARRAY_SIZE=2147483639(Integer.MAX_VALUE-8)
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        // 翻译： minCapacity 通常接近 size，所以這是一個勝利
        // Arrays.copyOf:
        //  複製指定的數組，截斷或填充空值（如有必要），使副本具有指定的長度。對於在原始數組和副本中都有效的所有索引，
        //  這兩個數組將包含相同的值。對於在副本中有效但在原始副本中無效的任何索引，副本將包含 null。
        //  當且僅當指定長度大於原始數組的長度時，此類索引才會存在。結果數組與原始數組的類完全相同。
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    // 大容量
    private static int hugeCapacity(int minCapacity) {
        // 这个新增的大小不能为负,防止向负索引，其实应该是保证内存安全而存在的
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        // Integer.MAX_VALUE    = 2147483647
        // MAX_ARRAY_SIZE       = 2147483639(Integer.MAX_VALUE-8)
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    /**
     * Returns the number of elements in this list.
     * 返回此列表中的元素数。
     * @return the number of elements in this list
     */
    public int size() {
        // 这个size就是当前数组（elementData）内真实元素的个数,一般这个数<=capacity
        return size;
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *  如果此列表不包含任何元素，则返回 true。
     * @return <tt>true</tt> if this list contains no elements
     */
    public boolean isEmpty() {
        // 就是当前数组实际元素个数是否为零，这跟capacity是不一样的
        return size == 0;
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     *
     * 如果此列表包含指定元素，则返回 true。更正式地说，当且仅当此列表包含至少一个元素 e 满足 (o==null ? e==null : o.equals(e)) 时，才返回 true。参数： o - 要测试此列表中是否存在的元素返回：如果此列表包含指定元素，则为 true
     */
    public boolean contains(Object o) {
        // 列表中是否存在o 的任意条件是
        // 1. 数组中存在null元素，并且o==null
        // 2. null!=o && 存在元素.equal(o) == true,需要注意的是 null!=o时一定要用equal比较是否相同
        return indexOf(o) >= 0;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *  返回此列表中指定元素第一次出现的索引，如果此列表不包含该元素，则返回 -1。
     *  更正式地说，返回满足 (o==null ? get(i)==null : o.equals(get(i))) 的最低索引 i，如果没有这样的索引，则返回 -1。
     */
    public int indexOf(Object o) {
        // 看到没 当前数组内是否存在o其实是用循环遍历寻找的
        // 如果寻找的o是个null 则只需判断数据元素是否==null，这样
        // String a = null;
        // a.equals(null);
        // 的(以上代码)会抛错
        // 如果是 null!=o 时候，o需要跟当前数组每个元素做equal比较，且equal左元素必须是非null，这点很重要...

        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i; // 这里返回的是元素所在数组的索引
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i; // 这里也一样哈
        }
        return -1;// 我记得String里面的contains返回的也是-1，如果元素确实不存在的话。
    }

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *  返回此列表中指定元素最后一次出现的索引，如果此列表不包含该元素，则返回 -1。
     *  更正式地说，返回满足 (o==null ? get(i)==null : o.equals(get(i))) 的最高索引 i，如果没有这样的索引，则返回 -1。
     */
    public int lastIndexOf(Object o) {
        // 如果看过 indexOf(.) 这个函数的实现，以下逻辑就很容易明白了
        // indexOf是从左往右找，lastIndexOf则是从右往左找 思路很简单～
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * Returns a shallow copy of this <tt>ArrayList</tt> instance.  (The
     * elements themselves are not copied.)
     *
     * @return a clone of this <tt>ArrayList</tt> instance
     * 返回此 ArrayList 实例的浅表副本。 （不复制元素本身。） 返回：此 ArrayList 实例的克隆
     */
    public Object clone() {
        // TODO 官方注释其实说的比较模糊，这里我给一个例子以证清白
        /*
            StringJoiner sj  =new StringJoiner(":");
            sj.add("a");
            ArrayList<Object> lst1 = new ArrayList<Object>();
            lst1.add("aaa");
            lst1.add(sj);
            ArrayList lst2 = (ArrayList) lst1.clone();
            sj.add("b");
            lst1.add("!!!");
            sj.add("c");
            lst2.add("ccc");
            System.out.println(lst1);
            // [aaa, a:b:c, !!!]
            System.out.println(lst2);
            // [aaa, a:b:c, ccc]
         */
        // 注意上面代码里面的sj这个对象，些许有理解了吧
        // 好了，现在我浅义的理解是当前这个clone是重写了Object的clone方法(当然这是一个native的方法，内部实现在jvm，用c++写的哈)
        // 内部使用 Arrays.copyOf -> System.arraycopy 这个函数还是调用的底层的数组深拷贝函数，额。。。
        // 还没有说完的是 底层的这个 System.arraycopy  只是拷贝了ArrayList这个对象到一个新地址(没有代码，我猜的)，好了重点儿来了，
        // 如果拷贝后的新ArrayList不是跟之前是用一个实例，但是它内部维护的元素如果也是一个个对象的话，这时候这个System.arraycopy深拷贝并不负责把原ArrayList维护的元素也一并拷贝到新ArrayList中
        // 这就印证了官方注释中说的：The elements themselves are not copied. （不复制元素本身。)
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);// 内部元素建立的是引用关系
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            // 这不应该发生，因为我们是可克隆的
            throw new InternalError(e);
        }
    }

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list in
     *         proper sequence
     *
     *  以正确的顺序（从第一个元素到最后一个元素）返回包含此列表中所有元素的数组。返回的数组将是“安全的”，因为此列表不维护对它的引用。
     *  （换句话说，这个方法必须分配一个新数组）。因此，调用者可以自由修改返回的数组。此方法充当基于数组和基于集合的 API 之间的桥梁。
     *  返回： 一个包含此列表中所有元素的数组，并按正确的顺序排列
     */
    public Object[] toArray() {
        // 对于这个，我个人理解是拷贝后的数组(返回的)是一个深拷贝，这个深拷贝仅限于这个数组本身
        // TODO？？？：以及内部基本类型的元素是拷贝的操作，
        // 如果是一个引用类型(例StringJoiner)只是拷贝了这个对象的引用而已(可以试试放一个自定义对象)
        // https://www.cnblogs.com/wk-missQ1/p/12809658.html
        // 可以确定的是对象还有包装类型copyOf后内部元素仍然是引用类型
        return Arrays.copyOf(elementData, size);
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element); the runtime type of the returned
     * array is that of the specified array.  If the list fits in the
     * specified array, it is returned therein.  Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of
     * this list.
     * 以正确的顺序（从第一个元素到最后一个元素）返回一个包含此列表中所有元素的数组；返回数组的运行时类型是指定数组的运行时类型。
     * 如果列表适合指定的数组，则在其中返回。否则，将使用指定数组的运行时类型和此列表的大小分配一个新数组。
     *
     * <p>If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list), the element in
     * the array immediately following the end of the collection is set to
     * <tt>null</tt>.  (This is useful in determining the length of the
     * list <i>only</i> if the caller knows that the list does not contain
     * any null elements.)
     * 如果列表适合指定的数组并有剩余空间（即，数组的元素多于列表），则数组中紧跟集合末尾的元素设置为 null。
     * （仅当调用者知道列表不包含任何空元素时，这对确定列表的长度很有用。）
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     * 参数：a——列表元素要存储到的数组，如果它足够大的话；否则，将为此目的分配相同运行时类型的新数组。
     * 返回：包含列表元素的数组抛出：ArrayStoreException – 如果指定数组的运行时类型不是此列表中每个元素的运行时类型的超类型
     * NullPointerException – 如果指定数组为 null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // 如果没有看懂当前功能可以试着调试下下面的代码
        /*
        ArrayList<String> srcList = new ArrayList<>(8);
        srcList.add("aa");
        srcList.add("gg");
        srcList.add("oo");
        srcList.add("hh");
        String[] desArr = new String[2];
        System.out.println(System.identityHashCode(desArr));
        String[] strings = srcList.toArray(desArr);
        System.out.println(System.identityHashCode(strings));
        System.out.println(strings);
         */
        // 额，总结下，其实就是这个a是一个容器，这个容器是放当前数组里的所有元素，那这时就出现问题来
        // 如果a的容量(a.length)小于当前数组大小(size),这时候就会创建一个新的数组，新数组的大小就是size，通过 System.identityHashCode 可窥见一隙
        // 如果a的容量(a.length)大于等于当前数组大小(size),这时就把当前数组(elementData)内的实际元素'拷贝'到a中，注意这里的拷贝的只是元素的引用
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            // 创建一个 a 的运行时类型的新数组，但我的内容：
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        // TODO 官方注释说的是在集合末尾跟一个null有助于确定a的实际元素的长度...如果 a[size-1] 的元素也是null呢，这似乎出乎作者的本意了...
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // Positional Access Operations
    // 位置访问操作
    @SuppressWarnings("unchecked")
    E elementData(int index) {
        // 注意:当前这个方法是个 protected(默认) 方法，仅供ArrayList内部使用
        // 主要功能不过是索引当前数组内指定位置(index)元素并返回(E)
        return (E) elementData[index];
    }

    /**
     * Returns the element at the specified position in this list.
     * 返回此列表中指定位置的元素。
     *
     * @param  index index of the element to return
     *         index 要返回的元素的索引
     * @return the element at the specified position in this list
     *         此列表中指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc} 异常
     */
    public E get(int index) {
        // 检查下这个索引的位置是否越界::index >= size)
        // 同时这个检查其实很粗糙，它只检查正越界的，对于index=-8这样的会检查通过(不会抛出错误)
        rangeCheck(index);
        // 这里索引当前数组，index<0 的依然会抛错,例： (java.lang.ArrayIndexOutOfBoundsException: -1)
        return elementData(index);
    }

    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *  将此列表中指定位置的元素替换为指定元素。
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        // 跟以上get方法一样，它只保证 index < size
        rangeCheck(index);
        // 由于检查不彻底，这里同样有索引越界的问题
        E oldValue = elementData(index);
        // 替换完指定位置的元素 再返回老元素
        elementData[index] = element;
        return oldValue;
    }

    /**
     * Appends the specified element to the end of this list.
     * 將指定元素附加到此列表的末尾。
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        // 这个顾名思义，由于当前数组的容量不可能无限大，只能递增的增长，所以需要再每次添加元素的时候检查一下数组是否能容纳下，容量不够先扩容...
        ensureCapacityInternal(size + 1);  // Increments modCount!!   递增 modCount
        elementData[size++] = e; // 索引自增一，把元素放到这个位置
        return true; // 默认添加成功后返回true，如果添加不成功则会抛出错误而不是返回false
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     * 在此列表中的指定位置插入指定元素。将当前位于该位置的元素（如果有）和任何后续元素向右移动（将其索引加一）。
     *
     * @param index index at which the specified element is to be inserted
     *              要插入指定元素的索引
     * @param element element to be inserted
     *                要插入的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        // 指定位置插入元素时只能这个位置(index)只能 (0~size) ，请注意这个范围只在当前数组有效元素的索引范围内
        rangeCheckForAdd(index);
        // 可以看到这个函数在add(.)方法内也有出现，
        ensureCapacityInternal(size + 1);  // Increments modCount!! 递增 modCount
        // 简单的解释一下 这个 arraycopy 方法是将第一个参数的第index位置的元素开始 及之后的所有元素复制到 index+1 这个开始位置，复制元素的个数是 size-index
        // 这里不太好理解的是第三个参数，这个参数是目标数组，因为元素只在当前数组内挪动，所以数组是没变(引用)
        // 举个栗子：复制之前的数组是 [a,b,c,d,-,-,-],现在需要在b和c之间插入一个元素x，这里调用 arraycopy 方法之后 数组会编程这个样子
        //          之后：[a,b,-,c,d,-,-]，注意，栗子中的数组容量大小没有变化，我用"-"代表有效元素之后的位置
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        // 这里是将空出来的位置补上当前插入的元素，如果对于上面呢个栗子的话，这步操作之后它的元素应该就是：[a,b,h,c,d,-,-]
        // 注意栗子哈，由于栗子的数组容量为7，插入前 size=4，这样容量能保证新插入元素，所以插入后数组容量依然没变
        elementData[index] = element;
        // 这里得更新下实际有效元素的索引
        size++;
        /*
            ArrayList<String> srcList = new ArrayList<>(8);
            srcList.add("aa");
            srcList.add("gg");
            srcList.add("oo");
            srcList.add("hh");
            System.out.println(System.identityHashCode(srcList));
            srcList.add(2,"XX");
            System.out.println(System.identityHashCode(srcList));
            System.out.println(srcList);
            // [aa, gg, XX, oo, hh]
         */
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     * 移除此列表中指定位置的元素。将任何后续元素向左移动（从它们的索引中减去 1）。
     *
     * @param index the index of the element to be removed
     *          要删除的元素的索引
     * @return the element that was removed from the list
     *          从列表中删除的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        // 检查索引位置是否>=size，如果这里index<0 也一样检查通过
        rangeCheck(index);
        // 统计一下修改次数
        modCount++;
        // 先取出这个位置的老位置
        E oldValue = elementData(index);
        // 这里是计算出需要移动元素的个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 这里还是很清楚：是将elementData index+1这个位置开始的元素移动到 index 这个位置开始并且元素长度为 numMoved 的位置
            // 说明白点儿就是 将索引(index+1) 开始的元素往左移动一个位置，这里自然要计算出numMoved这个长度
            // 当然 如果elementData里面只有一个元素，自然不会执行下面这行代码啦
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        // 如果elementData本身只有一个元素的话那这个elementData相当于是清空了，移除了一个元素也就相当于是将数组缩小一个
        elementData[--size] = null; // clear to let GC do its work  明确让GC做它的工作（也就是缩一个位置并置为null，切断引用了自然会被gc掉）
        // 这个oldValue是提前索引保存至变量的，到这里自然是要返回啦～
        return oldValue;
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If the list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * (if such an element exists).  Returns <tt>true</tt> if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     * 从此列表中删除第一次出现的指定元素（如果存在）。如果列表不包含该元素，则它不变。
     * 更正式地说，删除具有最低索引 i 的元素，使得 (o==null ? get(i)==null : o.equals(get(i)))
     * （如果存在这样的元素）。如果此列表包含指定的元素（或等效地，如果此列表因调用而更改），则返回 true。
     *
     * @param o element to be removed from this list, if present
     *          要从此列表中删除的元素（如果存在）
     * @return <tt>true</tt> if this list contained the specified element
     *          如果此列表包含指定的元素 (其实也就是返回是否删除这个状态)
     */
    public boolean remove(Object o) {
        // 从elementData中 移除 o 这个元素，因为如果 elementData 中存在null元素，则不能使用 null.equals(null) (会抛错)
        // 故由此对null与非null的o元素采用不同的处理方式～，这个是重点
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    // 这里奇怪的是并没有直接调用已经封装好了的 remove(int index) 这个函数，而是才使用 fastRemove(index) 的方式删除
                    // 想要一探究竟 remove(index) 与 fastRemove(index) 的区别，下面函数一并奉上～
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                // 这里可能需要注意的是 o作为非null参数自然要放在 equals 左边，不然会抛错～
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    /*
     * Private remove method that skips bounds checking and does not
     *  跳过边界检查且不执行的私有删除方法
     * return the value removed.
     *  返回删除的值。
     */
    private void fastRemove(int index) {
        // 由于这个函数是内部调用，相比 remove(index) 是少了很多安全检查
        // 其实也就两点
        //  1.少了对index做边界检查，因为是内部操作所以基本不存在越界对可能
        //  2.少保存索引位置老元素的获取及返回
        // 因为少了以上两步操作，所以相比较 remove(index) 这个方法效率会有些许提升～，所以封装fastRemove(index)是有意义的～
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns.
     * 从此列表中删除所有元素。此调用返回后，列表将为空。
     */
    public void clear() {
        // 清空当前数组(elementData)中所有元素
        // 一句话可以概括同时代码也很简单的其实 里面包含了一个重大的秘密
        // 1.当前数组每个元素位置被置为null使之能够gc掉
        // 2.由于只是清空元素，所以这里没有明确点名的是当前数组的容量是没有变化滴（Capacity）
        modCount++;
        // clear to let GC do its work
        // 切断引用，使gc能够适时候清理掉其中的元素
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     * 按照指定集合的迭代器返回的顺序，将指定集合中的所有元素附加到此列表的末尾。
     * 如果在操作正在进行时修改了指定的集合，则此操作的行为是未定义的。
     * （这意味着如果指定的集合是这个列表，并且这个列表是非空的，那么这个调用的行为是未定义的。）
     *
     * @param c collection containing elements to be added to this list
     *          包含要添加到此列表的元素的集合
     * @return <tt>true</tt> if this list changed as a result of the call
     *          如果此列表因调用而更改
     * @throws NullPointerException if the specified collection is null
     *          如果指定的集合为null
     */
    public boolean addAll(Collection<? extends E> c) {
        // 功能即为合并两个集合
        // 首先需要说的是传入的c集合不可以是null，否则下面这行就会抛出 NullPointerException 异常，这也是官方注释里面说了的
        Object[] a = c.toArray();
        int numNew = a.length;
        // 当前数组需要与传入的c(也即是a)这个数组合并，首先要确保当前数组的容量是够的，所以可能涉及到扩容
        // 这个最小容量就是 size + numNew ，下面这个方法即确保容量大小，如果容量够那就直接追加到当前数组末尾
        // 如果容量不够自然就是按 size+size/2 扩容一次，这个在add(E)方法内有过探讨～
        ensureCapacityInternal(size + numNew);  // Increments modCount 递增 modCount
        // 上面只是试探性的去做扩容操作(腾出位置),腾出位置之后自然要吧传入的c追加到当前数组末尾哈
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew; // 翻译过来就是 size=size+numNew
        // 这可能是为了统一api返参，add(E)这个方法也是一样，其实如果c是空或者null就没必要再调用当前方法做合并。。。
        return numNew != 0;
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     * 将指定集合中的所有元素插入此列表，从指定位置开始。将当前位于该位置的元素（如果有）和任何后续元素向右移动（增加它们的索引）。
     * 新元素将按照指定集合的迭代器返回的顺序出现在列表中。
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        // 功能：是将c中的所有元素插入到当前数组 index 开始的位置，index开始的原元素则统一往右移动
        // 简单说这个对index的检查还是到位的：index > size || index < 0
        rangeCheckForAdd(index);
        // 获取c内部维护的数组(这个数组是实际元素个数的数组)，不然numNew的长度是有问题的
        // 同时需要说明的是上面一步并没有对c是否为null做判断，所以下面一行也存在抛错的可能～
        Object[] a = c.toArray();
        int numNew = a.length;
        // 确保当前数组的容量大小(内部也会记录一次修改::modCount)
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 下面的代码跟remove方法的逻辑基本是相似的
        // 先计算当前数组内index开始需要挪动的长度numMoved
        // 在执行底层数组(引用)拷贝
        // 更新当前数组大小并返回是否插入
        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
     * (If {@code toIndex==fromIndex}, this operation has no effect.)
     * 从此列表中删除索引在 fromIndex（包括）和 toIndex（不包括）之间的所有元素。将任何后续元素向左移动（减少它们的索引）。
     * 此调用通过 (toIndex - fromIndex) 元素缩短列表。 （如果toIndex==fromIndex，则此操作无效。）
     *
     * @throws IndexOutOfBoundsException if {@code fromIndex} or
     *         {@code toIndex} is out of range
     *         ({@code fromIndex < 0 ||
     *          fromIndex >= size() ||
     *          toIndex > size() ||
     *          toIndex < fromIndex})
     */
    protected void removeRange(int fromIndex, int toIndex) {
        // 移除指定范围的元素 ，这个范围为： fromIndex<=被移除元素所在索引位置<toIndex
        // 因为其他函数一般会在 ensureCapacityInternal 内做一次更改次数 modCount，这里直接挪出来
        // 本人以为这样做其实是不妥的：如果能先检查下 fromIndex toIndex的范围才是，同时，当前方法是protected修饰的
        // 故，方法使用方需要对 fromIndex、toIndex做预检查～
        modCount++;
        // 先计算当前数组toIndex开始的之后元素的个数
        // 然后将尾部元素移动到 fromIndex 这个位置开始
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                numMoved);
        // 下面这四行代码如果没有其实也是没问题的，他只是让GC工作，让当前数组瘦身
        // clear to let GC do its work 明确让GC做它的工作
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        // 要更新当前数组大小，不过也需要注意的是当前方法并没有改变当前数组的容量，这一点也很重要～
        size = newSize;
    }

    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     *  检查给定的索引是否在范围内。如果不是，则引发适当的运行时异常。
     *  此方法不检查索引是否为负：它总是在数组访问之前立即使用，如果索引为负，则抛出 ArrayIndexOutOfBoundsException。
     */
    private void rangeCheck(int index) {
        // 我记得在get方法下说明过这个rangeCheck的问题(index为负时)，这里显而易见哈～
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * A version of rangeCheck used by add and addAll.
     *  add 和 addAll 使用的 rangeCheck 版本。
     */
    private void rangeCheckForAdd(int index) {
        // 这是为add或addAll方法使用的索引位置检查～
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     *  构造 IndexOutOfBoundsException 详细消息。
     *  在错误处理代码的许多可能重构中，这种“大纲”在服务器和客户端 VM 上表现最好。
     */
    private String outOfBoundsMsg(int index) {
        // 这只是个错误消息构造方法，一般在索引位置检查时会用到，可以看看上面两个方法
        return "Index: "+index+", Size: "+size;
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection.
     * 从此列表中删除包含在指定集合中的所有元素。
     *
     * @param c collection containing elements to be removed from this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *         is incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     *         specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see Collection#contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        // 从当前数组内移除所有包含c中数组元素的元素
        // 首先第一步要检查这个c是否是null哈～
        Objects.requireNonNull(c);
        // 这里有单独的方法来执行当前方法的具体操作，batchRemove是一个private修饰的方法～
        // 具体实现我们往后具体看～
        return batchRemove(c, false);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection.  In other words, removes from this list all
     * of its elements that are not contained in the specified collection.
     * 仅保留此列表中包含在指定集合中的元素。换句话说，从这个列表中删除所有不包含在指定集合中的元素。
     *
     * @param c collection containing elements to be retained in this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *         is incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     *         specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see Collection#contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        // 保留当前数组内所有元素与c内元素一样的元素，换言之也就是从当前数组移除与c内不一样的元素
        // 一样的需要检查c是否是null
        Objects.requireNonNull(c);
        // 同样的调用 batchRemove 方法执行具体操作
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                // 首先 在removeAll方法内这个 complement=false，在retainAll内调用则是complement=true
                // 再者，我们需要把w这个参数理解为需要保留元素的位置的索引(最右的那个)
                // 故此，我们先将 complement 理解为是否保留元素的的一个标志，这样下面的代码就好理解了
                // 循环当前数组内的数据 将每个需要保留的元素从左开始依次写入，最终 w 即为当前数组的 size
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            // Preserve behavioral compatibility with AbstractCollection,
            // 保持与 AbstractCollection 的行为兼容性，
            // even if c.contains() throws.
            // TODO 这一段我其实很迷糊，实际上这个 r != size 总会返回false并不会执行这个if下的语句
            if (r != size) {
                System.arraycopy(elementData, r,
                        elementData, w,
                        size - r);
                w += size - r;
            }
            // 这一段反而很清晰，就是将保留的元素段之后的(非保留的)元素的位置置为null，就是给当前数组瘦身
            // 瘦身之后的当前数组容量并没有缩小。。。
            if (w != size) {
                // clear to let GC do its work
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
                // 记住似乎是少了一步： this.elementData=elementData
                // 是因为this.elementData与elementData 两个引用同时指向的是同一个引用对象
            }
        }
        return modified;
    }

    /**
     * Save the state of the <tt>ArrayList</tt> instance to a stream (that
     * is, serialize it).
     * 将 ArrayList 实例的状态保存到流中（即序列化它）。
     *
     * @serialData The length of the array backing the <tt>ArrayList</tt>
     *             instance is emitted (int), followed by all of its elements
     *             (each an <tt>Object</tt>) in the proper order.
     */
    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
        // 本方法其实就是将当前数组序列化到一个输出流中，方便后期存储及计算
        // 由于当前方法是private签名，目前也没调用的地方，这里不做详细分析
        // Write out element count, and any hidden stuff
        // 写出元素计数和任何隐藏的东西
        int expectedModCount = modCount;
        s.defaultWriteObject();

        // Write out size as capacity for behavioural compatibility with clone()
        // 将 size 写为与 clone() 行为兼容的容量
        s.writeInt(size);

        // Write out all elements in the proper order.
        // 以正确的顺序写出所有元素。
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }
        // 多线程下写存在不一致的情况，所以要抛出错误
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * Reconstitute the <tt>ArrayList</tt> instance from a stream (that is,
     * deserialize it).
     *  从流中重构 ArrayList 实例（即反序列化它）。
     */
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        // 这是以上writeObject方法的反向操作，从输入流反序列化到当前数组
        elementData = EMPTY_ELEMENTDATA;

        // Read in size, and any hidden stuff
        // 读取大小和任何隐藏的东西
        s.defaultReadObject();

        // Read in capacity
        // 读取容量
        s.readInt(); // ignored

        if (size > 0) {
            // be like clone(), allocate array based upon size not capacity
            // 就像 clone()，根据大小而不是容量分配数组
            // 计算一个容量（这个容量最低为10）
            int capacity = calculateCapacity(elementData, size);
            // 检查这个流以及数组是否有读写权限 获取 Java OIS 访问权限
            SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
            ensureCapacityInternal(size);// 确保容量
            // 循环将流中的每个元素写入到当前数组中即可
            Object[] a = elementData;
            // Read in all elements in the proper order.  以正确的顺序读入所有元素。
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     * 返回此列表中元素的列表迭代器（以正确的顺序），从列表中的指定位置开始。
     * 指定的索引指示初始调用 next 将返回的第一个元素。对 previous 的初始调用将返回具有指定索引减一的元素。
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ListIterator<E> listIterator(int index) {
        // 我们常用listIterator()来构建一个迭代器，这里则可以指定开始位置构建一个迭代器
        // 这个位置必须在当前数组有效的索引范围内（有效元素的索引范围）
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        // 构建并返回一个迭代器，从指定位置开始
        return new ListItr(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     * 返回此列表中元素的列表迭代器（以正确的顺序）。
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @see #listIterator(int)
     */
    public ListIterator<E> listIterator() {
        // 当前方法同以上，只不过是直接从0开始索引并返回一个迭代器 ,具体代码方法内会有说明
        return new ListItr(0);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     * 以正确的顺序返回此列表中元素的迭代器。
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     *         以正确顺序遍历此列表中的元素的迭代器
     */
    public Iterator<E> iterator() {
        // 虽然与都会返回一个迭代器，但是iterator只能单向循环，且不能实现增删改查
        // 详见： https://www.cnblogs.com/tjudzj/p/4459443.html
        return new Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     *  AbstractList.Itr 的优化版本
     */
    private class Itr implements Iterator<E> {
        // 这个其实默认就是 i=0;
        int cursor;       // index of next element to return :下一个将要返回的元素位置的索引,其实也就是个游标
        int lastRet = -1; // index of last element returned; -1 if no such :返回的最后一个元素的索引； -1 如果没有
        int expectedModCount = modCount;

        Itr() {}

        public boolean hasNext() {
            // 是否进行下一次循环的条件是：游标不等于size
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            // 安全检查
            checkForComodification();
            // 这个将游标赋予i，然后检查是否i是否超出当前数组索引位置（size)
            // 我暂时没看出以下三行跟 hasNext() 有多少区别。。。，而且checkForComodification内也是做了安全检查了的
            // 总结就是：十分没必要啊...
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            // 不大明白为啥要再整个引用 ，通过这个新引用索引返回数组值
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            // 因为迭代器每次循环前都会调用 hasNext ，故此推测这里也应该将游标+1
            cursor = i + 1;
            // 需要说明的是这个 lastRet 是个成员变量，而i只是个方法内临时变量而已
            // 所以每循环一次这个 lastRet 需要记录为当前返值前的当前索引位置
            return (E) elementData[lastRet = i];
        }

        // 这是个很重要的函数.
        public void remove() {
            // 我在next方法内讲过，这个 lastRet 在返值时被赋予当前索引的位置
            // 不过你要点 lastRet 这个发现他的默认值是 -1 。。。，oh，对了
            // 很明显这个remove()方法是要在循环内使用的，如果不是在循环内使用的话，起码也是要先调一次 next() 方法才可使用
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        // 这个个人理解，应该是防止多线程并发而做的一个安全措施
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.  (If
     * {@code fromIndex} and {@code toIndex} are equal, the returned list is
     * empty.)  The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations.
     *
     * <p>This method eliminates the need for explicit range operations (of
     * the sort that commonly exist for arrays).  Any operation that expects
     * a list can be used as a range operation by passing a subList view
     * instead of a whole list.  For example, the following idiom
     * removes a range of elements from a list:
     * <pre>
     *      list.subList(from, to).clear();
     * </pre>
     * Similar idioms may be constructed for {@link #indexOf(Object)} and
     * {@link #lastIndexOf(Object)}, and all of the algorithms in the
     * {@link Collections} class can be applied to a subList.
     *
     * <p>The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of this list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
    }

    private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                    parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(ArrayList.this, offset,
                    offset + this.size, this.modCount);
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, and {@link Spliterator#ORDERED}.
     * Overriding implementations should document the reporting of additional
     * characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator<>(this, 0, -1, 0);
    }

    /** Index-based split-by-two, lazily initialized Spliterator */
    static final class ArrayListSpliterator<E> implements Spliterator<E> {

        /*
         * If ArrayLists were immutable, or structurally immutable (no
         * adds, removes, etc), we could implement their spliterators
         * with Arrays.spliterator. Instead we detect as much
         * interference during traversal as practical without
         * sacrificing much performance. We rely primarily on
         * modCounts. These are not guaranteed to detect concurrency
         * violations, and are sometimes overly conservative about
         * within-thread interference, but detect enough problems to
         * be worthwhile in practice. To carry this out, we (1) lazily
         * initialize fence and expectedModCount until the latest
         * point that we need to commit to the state we are checking
         * against; thus improving precision.  (This doesn't apply to
         * SubLists, that create spliterators with current non-lazy
         * values).  (2) We perform only a single
         * ConcurrentModificationException check at the end of forEach
         * (the most performance-sensitive method). When using forEach
         * (as opposed to iterators), we can normally only detect
         * interference after actions, not before. Further
         * CME-triggering checks apply to all other possible
         * violations of assumptions for example null or too-small
         * elementData array given its size(), that could only have
         * occurred due to interference.  This allows the inner loop
         * of forEach to run without any further checks, and
         * simplifies lambda-resolution. While this does entail a
         * number of checks, note that in the common case of
         * list.stream().forEach(a), no checks or other computation
         * occur anywhere other than inside forEach itself.  The other
         * less-often-used methods cannot take advantage of most of
         * these streamlinings.
         */

        private final ArrayList<E> list;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index
        private int expectedModCount; // initialized when fence set

        /** Create new spliterator covering the given  range */
        ArrayListSpliterator(ArrayList<E> list, int origin, int fence,
                             int expectedModCount) {
            this.list = list; // OK if null unless traversed
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize fence to size on first use
            int hi; // (a specialized variant appears in method forEach)
            ArrayList<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = list) == null)
                    hi = fence = 0;
                else {
                    expectedModCount = lst.modCount;
                    hi = fence = lst.size;
                }
            }
            return hi;
        }

        public ArrayListSpliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                    new ArrayListSpliterator<E>(list, lo, index = mid,
                            expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                @SuppressWarnings("unchecked") E e = (E)list.elementData[i];
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc; // hoist accesses and checks from loop
            ArrayList<E> lst; Object[] a;
            if (action == null)
                throw new NullPointerException();
            if ((lst = list) != null && (a = lst.elementData) != null) {
                if ((hi = fence) < 0) {
                    mc = lst.modCount;
                    hi = lst.size;
                }
                else
                    mc = expectedModCount;
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (; i < hi; ++i) {
                        @SuppressWarnings("unchecked") E e = (E) a[i];
                        action.accept(e);
                    }
                    if (lst.modCount == mc)
                        return;
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // figure out which elements are to be removed
        // any exception thrown from the filter predicate at this stage
        // will leave the collection unmodified
        int removeCount = 0;
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                removeSet.set(i);
                removeCount++;
            }
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

        // shift surviving elements left over the spaces left by removed elements
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            final int newSize = size - removeCount;
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            this.size = newSize;
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            elementData[i] = operator.apply((E) elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
}