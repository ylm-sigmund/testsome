package com.diy.sigmund.testsome;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.diy.sigmund.entity.User;

/**
 * @author ylm-sigmund
 * @since 2021/1/16 12:22
 */
public class DuplicateRemovalTest {

    /**
     * 基本数据类型，普通方法去重
     */
    @Test
    public void basicDataTypeForCommon() {
        final List<String> stringList = Stream.of("a", "b", "c", "a", "b").collect(Collectors.toList());
        final Set<String> stringLinkedHashSet = new LinkedHashSet<>(stringList);
        stringList.clear();
        stringList.addAll(stringLinkedHashSet);
        stringList.forEach(System.out::println);
    }

    /**
     * 基本数据类型，lambda表达式去重
     */
    @Test
    public void basicDataTypeForLambda() {
        final List<String> stringList = Stream.of("a", "b", "c", "a", "b").collect(Collectors.toList());
        // 去重后的集合，对原集合无影响
        final List<String> distinctList = stringList.stream().distinct().collect(Collectors.toList());
        stringList.forEach(System.out::println);
        System.out.println("--------");
        distinctList.forEach(System.out::println);
    }

    /**
     * 对象去重，需要重写hashcode和equals
     *
     * 根据对象的属性，普通方法去重
     * 
     * public class User {
     *
     * private Integer id; private String name; private Date date;
     */
    @Test
    public void objectTypeForCommon() {
        final List<User> userList = getUserList();

        // Function<? super T, ? extends U> keyExtractor 函数式接口入参，形参User，出参user.getName
        final TreeSet<User> userTreeSet = new TreeSet<User>(Comparator.comparing(User::getName));

        userTreeSet.addAll(userList);
        userList.clear();
        userList.addAll(userTreeSet);

        userList.forEach(System.out::println);
    }

    /**
     * 根据对象的属性，lambda表达式去重
     */
    @Test
    public void objectTypeForLambda() {
        final List<User> userList = getUserList();
        final List<User> distinctList = userList.stream().collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<User>(Comparator.comparing(User::getName))), ArrayList::new));

        distinctList.forEach(System.out::println);
    }

    @Test
    public void compare() {
        final List<User> userList = getUserList();
        System.out.println("-------------中文排序，按字母升序");
        // Comparator<? super U> keyComparator 创建一个自定义的比较器。
        userList.sort(Comparator.comparing(User::getName, (o1, o2) -> {
            Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
            CollationKey c1 = cmp.getCollationKey(o1);
            CollationKey c2 = cmp.getCollationKey(o2);
            return cmp.compare(c1.getSourceString(), c2.getSourceString());
        }));
        // userList.sort(Comparator.comparing(User::getName, Comparator.naturalOrder()));
        // userList.sort(Comparator.comparing(User::getName, Comparator.reverseOrder()));
        userList.forEach(System.out::println);

        System.out.println("-------------含null元素排序");
        userList.add(null);
        // 当集合中存在null元素时，可以使用针对null友好的比较器，null元素排在集合的最前面
        userList.sort(Comparator.nullsFirst(Comparator.comparing(User::getName)));
        userList.forEach(System.out::println);

        System.out.println("-------------首先使用 id 排序，紧接着在使用 name 排序");
        userList.add(new User(1004, "小亮"));
        userList.sort(Comparator
            .nullsFirst(Comparator.comparing(User::getId).thenComparing(Comparator.comparing(User::getName))));
        userList.forEach(System.out::println);
    }

    private List<User> getUserList() {
        final User user1 = new User(1001, "小华");
        final User user2 = new User(1002, "小红");
        final User user3 = new User(1003, "小刚");
        final User user4 = new User(1004, "小华");
        return Stream.of(user1, user2, user3, user4).collect(Collectors.toList());
    }
}
