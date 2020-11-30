package com.diy.sigmund.testsome;

/**
 * 1.lambda表达式的基础语法： Lambda是一个匿名函数 参数列表 方法体
 *
 * ():小括号用来描述参数列表，形参 {}：大括号用来描述方法体 ->:Lambda运算符，读作goes to
 *
 * 1.无参数无返回 ()->{sout("hello world");} 2.单个参数，无返回 (int a)->{} 3.多个参数，无返回 (int a,int b)->{} 4.无参数，有返回 ()->{return 100;}
 * 5.单个参数，又返回 (int a)->{return a*2;} 6.多个参数，有返回 (int a,int b)->{return a+b;}
 *
 * 2.Lambda语法精简 ①参数： 由于在接口的抽象方法中，已经定义了参数的数量和类型。所以在Lambda表达式中， 参数的类型可以省略。 备注：如果需要省略类型，每个参数类型都要省略。不能只省略部分 ②参数小括号：
 * 如果参数列表中，参数的数量只有一个，此时小括号可以省略 ③方法大括号 如果方法体中只有一行代码，那么大括号可以省略 ④方法大括号 如果方法体中唯一的语句是一个返回语句，则{}省略的同时，也必须省略到return
 *
 *
 *
 * 3.Lambda方法引用 可以快速的将一个Lambda表达式的实现指向一个已经实现的方法 语法：方法的隶属者（类--静态方法，对象--非静态方法）：：方法名
 *
 * 引用条件： ①：参数数量和类型一定要和接口中定义的方法一样 ②：返回值的类型一定要和接口中定义的方法一样 eg: Interface1 face1 = ClassName1 :: change;
 *
 * Lambda构造方法的引用 eg:PersonInterface inter = Person :: new;
 *
 * 4.综合案例 1.集合排序 list.sort((o1,o2)->o2.age-o1.age); Collection.sort(); Arrays.sort();
 *
 * 2.TreeSet TreeSet<Object> set = new TreeSet<>((o1,02)->{if.....});//返回0，treeSet认为相同元素，所以返回1和-1，
 *
 * 3.集合的遍历 forEach list.forEach(System.out::println); list.forEach((ele)->{ if(ele%2==0){ sout(ele); } });
 *
 * 4.集合的删除 removeId list.removeIf(ele->ele.age>10);
 *
 * 5.线程的实例化 Thread t = new Thread(()->{ //run方法里面的逻辑 }); t.start();
 *
 * 5.系统内置函数式接口 import java.util.function.* Predicate<T> 参数T ,返回值boolean IntPredicate<T> 参数int,返回值boolean
 * LongPredicate;DoublePredicate Consumer<T> 参数T， 返回值void IntConsumer;LongConsumer;DoubleConsumer Function<T,R> 参数T,
 * 返回值R IntFunction<R> 参数int，返回值R LongFunction<R>;DoubleFunction<R> IntToLongFunction 参数int,返回Long
 * IntToDoubleFunction;LongToIntFunction;LongToDoubleFunction;DoubleToIntFunction;DoubleToLongFunction; Supplier<T>
 * 参数无，返回值T UnaryOperator<T> 参数T， 返回值T BinaryOperator<T>参数T,T,返回值T BiFunction<T,U,R>参数T,U,返回值R BiPredicate<T,U>
 * 参数T,U,返回值boolean BiConsumer<T,U> 参数T,U,返回值void
 *
 * 最常用的：Predicate<T> Consumer<T> Function<T,R> Supplier<T>
 *
 * 6.闭包问题 1.闭包提升局部变量的生命周期 2.闭包中引用的是常量
 *
 * @author ylm-sigmund
 * @since 2020/10/11 21:57
 */
public class LambdaIntroduction {

}
