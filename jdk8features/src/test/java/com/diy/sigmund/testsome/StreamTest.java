package com.diy.sigmund.testsome;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * @author yao liming
 * @since 2020/8/11 22:54
 */
public class StreamTest {
    /**
     * 1、传入可变长度数组调用静态方法创建stream
     */
    @Test
    public void StreamOf() {
        // 没实质性用途，可做测试用
        Stream<String> stream = Stream.of("A", "B", "C", "D");
        stream.forEach(System.out::println);
        // 一个 Stream 只可以使用一次,下面的语句会报错
        // java.lang.IllegalStateException: stream has already been operated upon or closed
        stream.forEach(a -> System.out.println(a));
    }

    /**
     * 2、基于数组或集合创建stream
     */
    @Test
    public void basedOnArray() {
        Stream<String> stream1 = Arrays.stream(new String[] {"A", "B", "C", "D"});
        List<String> list = new ArrayList<>();
        list.add("X");
        list.add("Y");
        Stream<String> stream2 = list.stream();
        stream1.forEach(System.out::println);
        stream2.forEach(System.out::println);
    }

    /**
     * 基于Supplier创建的Stream会不断调用Supplier.get()方法来不断产生下一个元素，
     * 这种Stream保存的不是元素，而是算法，它可以用来表示无限序列。
     *
     * 对于无限序列，如果直接调用forEach()或者count()这些最终求值操作，会进入死循环，
     * 因为永远无法计算完这个序列，所以正确的方法是先把无限序列变成有限序列
     */
    @Test
    public void generate() {
        Stream<Integer> natual = Stream.generate(new NatualSupplier());
        // 注意：无限序列必须先变成有限序列再打印:
        natual.limit(7).forEach(System.out::println);
    }

    class NatualSupplier implements Supplier<Integer> {
        int n = 0;

        @Override
        public Integer get() {
            return ++n;
        }
    }

    @Test
    public void testAnother() {
        // Files类的lines()方法可以把一个文件变成一个Stream，每个元素代表文件的一行内容
        try {
            Stream<String> lines = Files.lines(Paths.get("/data/test.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 正则表达式的Pattern对象有一个splitAsStream()方法，可以直接把一个长字符串分割成Stream序列而不是数组
        Pattern p = Pattern.compile("\\s+");
        Stream<String> s = p.splitAsStream("The quick brown fox jumps over the lazy dog");
        s.forEach(System.out::println);
    }

    @Test
    public void basicDataTypes() {
        // 因为Java的范型不支持基本类型，所以我们无法用Stream<int>这样的类型，会发生编译错误。
        // 为了保存int，只能使用Stream<Integer>，但这样会产生频繁的装箱、拆箱操作。为了提高效率，
        // Java标准库提供了IntStream、LongStream和DoubleStream这三种使用基本类型的Stream，
        // 它们的使用方法和范型Stream没有大的区别，设计这三个Stream的目的是提高运行效率：
        IntStream is = Arrays.stream(new int[] {1, 2});
        LongStream longStream = Arrays.stream(new String[] {"3", "4"}).mapToLong(Long::parseLong);
    }

    @Test
    public void Map() {
        // Stream.map()是Stream最常用的一个转换方法，它把一个Stream转换为另一个Stream
        // 利用map()，不但能完成数学计算，对于字符串操作，以及任何Java对象都是非常有用的
        List<String> list = Arrays.asList("  Apple ", " pear ", " ORANGE", " BaNaNa ");
        /**
         * 将T类型转换为R类型
         * @FunctionalInterface
         * public interface Function<T, R> {
         *     // 将T类型转换为R:
         *     R apply(T t);
         * }
         */
        list.stream().map(String::trim).map(String::toLowerCase).forEach(System.out::println);
    }

    @Test
    public void filter() {
        // 所谓filter()操作，就是对一个Stream的所有元素一一进行测试，
        // 不满足条件的就被“滤掉”了，剩下的满足条件的元素就构成了一个新的Stream
        /**
         * filter()方法接收的对象是Predicate接口对象，它定义了一个test()方法，负责判断元素是否符合条件
         * @FunctionalInterface
         * public interface Predicate<T> {
         *     // 判断元素t是否符合条件:
         *     boolean test(T t);
         * }
         */
        // 过滤掉偶数，保留奇数集合
        IntStream.of(1, 2, 3, 4, 5).filter(number -> number % 2 != 0).forEach(System.out::println);
    }
    
    @Test
    public void streamFilterGenerate() {
        Stream.generate(new LocalDateSupplier()).limit(31).filter(ldt -> {
            return ldt.getDayOfWeek() == DayOfWeek.SATURDAY || ldt.getDayOfWeek() == DayOfWeek.SUNDAY;
        }).forEach(System.out::println);
    }
    
    class LocalDateSupplier implements Supplier<LocalDate>{
        LocalDate start = LocalDate.of(2020,8,1);
        int n= -1;
        @Override
        public LocalDate get() {
            n++;
            return start.plusDays(n);
        }
    }

    @Test
    public void reduce(){
        Integer sum = Stream.of(1, 2, 3, 4, 5, 6).reduce(0, (acc, n) -> acc + n);
        System.out.println(sum);
        /**
         * @FunctionalInterface
         * public interface BinaryOperator<T> {
         *     // Bi操作：两个输入，一个输出
         *     T apply(T t, T u);
         * }
         */
        //上述代码看上去不好理解，但我们用for循环改写一下，就容易理解了：
        /**
         * Stream<Integer> stream = ...
         *         int sum = 0;
         *         for (n : stream) {
         *             sum = (sum, n) -> sum + n;
         *         }
         */
        // 可见，reduce()操作首先初始化结果为指定值（这里是0），紧接着，
        // reduce()对每个元素依次调用(acc, n) -> acc + n，其中，acc是上次计算的结果：
        /**
         * // 计算过程:
         * acc = 0 // 初始化为指定值
         * acc = acc + n = 0 + 1 = 1 // n = 1
         * acc = acc + n = 1 + 2 = 3 // n = 2
         * acc = acc + n = 3 + 3 = 6 // n = 3
         * acc = acc + n = 6 + 4 = 10 // n = 4
         * acc = acc + n = 10 + 5 = 15 // n = 5
         * acc = acc + n = 15 + 6 = 21 // n = 6
         */
        // 因此，实际上这个reduce()操作是一个求和。

        //如果去掉初始值，我们会得到一个Optional<Integer>
        Optional<Integer> opt = Stream.of(1, 2, 3, 4, 5, 6).reduce((acc, n) -> acc + n);
        System.out.println(Optional.ofNullable(opt.get()).orElse(1));

        //把求和改为乘积，但是必须注意初始乘数必须为1
        Integer product = Stream.of(1, 2, 3, 4, 5, 6).reduce(1, (acc, n) -> acc * n);
        System.out.println(product);
    }

    /**
     * 对Stream来说可以分为两类，
     * 一类是转换操作，即把一个Stream转换为另一个Stream，例如map()和filter()，
     * 另一类是聚合操作，即对Stream的每个元素进行计算，得到一个确定的结果，例如reduce()
     */
    @Test
    public void toList() {
        /**
         * 把Stream的每个元素收集到List的方法是调用collect()并传入Collectors.toList()对象，
         * 它实际上是一个Collector实例，通过类似reduce()的操作，把每个元素添加到一个收集器中（实际上是ArrayList）。
         *
         * 类似的，collect(Collectors.toSet())可以把Stream的每个元素收集到Set中。
         */
        List<String> list = Stream.of("Apple", "", null, "Pear", "  ", "Orange")
            .filter(str -> Objects.nonNull(str) && !str.isEmpty()).collect(Collectors.toList());
        // [Apple, Pear, , Orange]
        System.out.println(list);
    }

    @Test
    public void toArray(){
        /**
         * 注意到传入的“构造方法”是String[]::new，
         * 它的签名实际上是IntFunction<String[]>定义的String[] apply(int)，即传入int参数，获得String[]数组的返回值。
         */
        String[] array = Stream.of("Apple", "", null, "Pear", "  ", "Orange").toArray(String[]::new);
    }

    @Test
    public void toMap(){
        Map<String, String> map = Stream.of("APPL:Apple", "MSFT:Microsoft")
            .collect(Collectors.toMap(s -> s.substring(0, s.indexOf(":")), s -> s.substring(s.indexOf(":") + 1)));
        System.out.println(map);
    }

    /**
     * 分组输出使用Collectors.groupingBy()，它需要提供两个函数：
     * 一个是分组的key，这里使用s -> s.substring(0, 1)，表示只要首字母相同的String分到一组，
     * 第二个是分组的value，这里直接使用Collectors.toList()，表示输出为List
     */
    @Test
    public void groupingBy(){
        Map<String, List<String>> collect =
            Stream.of("Apple", "Banana", "Blackberry", "Coconut", "Avocado", "Cherry", "Apricots")
                .collect(Collectors.groupingBy(s -> s.substring(0, 1), Collectors.toList()));
        //{A=[Apple, Avocado, Apricots], B=[Banana, Blackberry], C=[Coconut, Cherry]}
        System.out.println(collect);
    }

    @Test
    public void sorted() {
        // [Banana, Orange, apple]
        // 此方法要求Stream的每个元素必须实现Comparable接口。如果要自定义排序，传入指定的Comparator
        System.out.println(Stream.of("Orange", "apple", "Banana").sorted().collect(Collectors.toList()));

        // [apple, Banana, Orange]
        System.out.println(
            Stream.of("Orange", "apple", "Banana").sorted(String::compareToIgnoreCase).collect(Collectors.toList()));
        System.out.println(
            Stream.of("Orange", "apple", "Banana").sorted(String::compareToIgnoreCase).collect(Collectors.toList()));
    }

    @Test
    public void distinct() {
        // [A, B, C, D]
        System.out.println(Stream.of("A", "B", "A", "C", "B", "D").distinct().collect(Collectors.toList()));
    }

    @Test
    public void Intercept() {
        // 截取操作常用于把一个无限的Stream转换成有限的Stream，
        // skip()用于跳过当前Stream的前N个元素，limit()用于截取当前Stream最多前N个元素
        // [C, D, E]
        System.out.println(Stream.of("A", "B", "C", "D", "E", "F").skip(2).limit(3).collect(Collectors.toList()));
        // [C]
        System.out.println(Stream.of("A", "B", "C", "D", "E", "F").limit(3).skip(2).collect(Collectors.toList()));
    }

    /**
     * 将两个Stream合并为一个Stream可以使用Stream的静态方法concat()
     */
    @Test
    public void concat(){
        Stream<String> a = Stream.of("A", "B", "C");
        Stream<Integer> b = Stream.of(1, 2);
        Stream<? extends Serializable> concat = Stream.concat(a, b);
        System.out.println(concat.collect(Collectors.toList()));
    }
    
    @Test
    public void flatMap(){
        //所谓flatMap()，是指把Stream的每个元素（这里是List）映射为Stream，然后合并成一个新的Stream
        Stream<Integer> integerStream =
            Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4), Arrays.asList(5, 6, 7)).flatMap(list -> list.stream());
        System.out.println(integerStream.collect(Collectors.toList()));
    }

    /**
     * 通常情况下，对Stream的元素进行处理是单线程的，即一个一个元素进行处理。
     * 但是很多时候，我们希望可以并行处理Stream的元素，因为在元素数量非常大的情况，并行处理可以大大加快处理速度。
     *
     * 把一个普通Stream转换为可以并行处理的Stream非常简单，只需要用parallel()进行转换
     *
     * 经过parallel()转换后的Stream只要可能，就会对后续操作进行并行处理。
     * 我们不需要编写任何多线程代码就可以享受到并行处理带来的执行效率的提升
     */
    @Test
    public void parallel(){
        Stream<String> s = Stream.of("a","b");
        String[] result = s.parallel() // 变成一个可以并行处理的Stream
                .sorted() // 可以进行并行排序
                .toArray(String[]::new);
    }
}
