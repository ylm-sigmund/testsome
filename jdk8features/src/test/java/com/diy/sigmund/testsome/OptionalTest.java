package com.diy.sigmund.testsome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.diy.sigmund.entity.Acthiactinst;

/**
 * @author yao liming
 * @since 2020/7/7 21:32
 */
public class OptionalTest {

    @Test
    public void optional() {
        Acthiactinst acthiactinst = new Acthiactinst();
        acthiactinst.setActId("1");
        String ss = Optional.ofNullable(acthiactinst).map(Acthiactinst::getActName).orElse("222");
        System.out.println(ss);
        Acthiactinst acthiactinst1 = null;
        String aa = Optional.ofNullable(acthiactinst1).map(Acthiactinst::getActName).orElse("222");
        System.out.println(aa);
    }

    @Test
    public void ofNullable() {
        // return new Optional<>(value);
        Optional<Integer> integer = Optional.of(1);
        // return value == null ? empty() : of(value);
        // empty{return (Optional<T>) EMPTY;}
        // private static final Optional<?> EMPTY = new Optional<>();
        Optional<Object> optional = Optional.ofNullable(null);
        Optional<List> optional1 = Optional.ofNullable(new ArrayList<>());
        // 打印結果
        // Optional[1]
        System.out.println(integer);
        // Optional.empty
        System.out.println(optional);
        // Optional[[]]
        System.out.println(optional1);
    }

    @Test
    public void empty() {
        // empty{return (Optional<T>) EMPTY;}
        // private static final Optional<?> EMPTY = new Optional<>();
        System.out.println(Optional.ofNullable(null) == Optional.empty());// true
        Object o1 = Optional.<Integer>empty();// Optional.empty
        Object o2 = Optional.<String>empty();// Optional.empty
        Optional<Integer> empty = Optional.empty();// Optional.empty
        System.out.println(o1 == o2);// true
    }

    /**
     * 是否存在
     */
    @Test
    public void isPresent() {
        Optional<Integer> optional = Optional.ofNullable(1);// Optional[1]
        Optional<Object> optional1 = Optional.ofNullable(null);// Optional.empty
        // isPresent() { return value != null;}
        System.out.println(optional.isPresent());// true
        System.out.println(optional1.isPresent());// false
    }

    /**
     * public public void ifPresent(Consumer<? super T> consumer) { if (value != null) consumer.accept(value); }
     */
    @Test
    public void ifPresent() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Object> optional2 = Optional.ofNullable(null);// Optional.empty
        // value is 1
        optional1.ifPresent(integer -> {
            System.out.println("value is " + integer);
        });
        // 不会输出任何内容
        optional2.ifPresent(aa -> {
            System.out.println("value is " + aa);
        });
    }

    /**
     * public T orElse(T other) { return value != null ? value : other; }
     */
    @Test
    public void orElse() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Object> optional2 = Optional.ofNullable(null);// Optional.empty
        // 1
        System.out.println(optional1.orElse(222));
        // 222
        System.out.println(optional2.orElse("222"));
    }

    /**
     * public T orElseGet(Supplier<? extends T> other) { return value != null ? value : other.get(); }
     */
    @Test
    public void orElseGet() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Object> optional2 = Optional.ofNullable(null);// Optional.empty
        // 1
        System.out.println(optional1.orElseGet(() -> 222));
        // 222
        System.out.println(optional2.orElseGet(() -> 222));
    }

    /**
     * public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X { if (value != null)
     * { return value; } else { throw exceptionSupplier.get(); } }
     */
    @Test
    public void orElseThrow() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Integer> optional2 = Optional.ofNullable(null);// Optional.empty
        try {
            // 不会抛出异常
            optional1.orElseThrow(() -> {
                throw new ArithmeticException("this is ArithmeticException");
            });
            // 抛出异常
            optional2.orElseThrow(() -> {
                throw new IllegalStateException("this is IllegalStateException");
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void filter() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Integer> optional2 = Optional.ofNullable(null);// Optional.empty
        // Optional.empty
        System.out.println(optional1.filter(a -> a == null));
        // 1
        optional1.filter(integer -> integer == 1).ifPresent(System.out::println);
        // undefined 什么也不会输出
        optional1.filter(integer -> integer == 2).ifPresent(System.out::println);
    }

    @Test
    public void map() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Integer> optional2 = Optional.ofNullable(null);// Optional.empty
        // Optional[key1]
        System.out.println(optional1.map(a -> "key" + a));
        // Optional.empty
        System.out.println(optional2.map(a -> "key" + a));
    }

    @Test
    public void flatMap() {
        Optional<Integer> optional1 = Optional.ofNullable(1);// Optional[1]
        Optional<Integer> optional2 = Optional.ofNullable(null);// Optional.empty
        // Optional[key1]
        System.out.println(optional1.flatMap(a -> Optional.of("key" + a)));
        // Optional.empty
        System.out.println(optional2.flatMap(a -> Optional.of("key" + a)));
    }
}
