package com.sxzhongf.mscx.passbook.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RedisTemplateTest for How to write UT
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @since 2019/6/5
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    // 1. 注入Redis 客户端
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 2. 创建Test Function
     */
    @Test
    public void testRedisClient() {
        // 3. Redis#Flushall 清空所有redis是一个危险动作，在生产环境禁用！
        //    因为危险，所有Redis没有提供静态方法，必须先获取连接connection

        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return null;
        });

        // 4. 断言：当前redis为空, 如果失败，抛出异常
        /**
         * {code}
         * java.lang.AssertionError
         * 	at com.sxzhongf.mscx.passbook.service.RedisTemplateTest.testRedisClient(RedisTemplateTest.java:43)
         * 	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
         * 	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
         * 	at java.lang.reflect.Method.invoke(Method.java:498)
         * 	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
         * 	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
         * 	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
         * 	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
         * 	at org.springframework.test.context.junit4.statements.RunBeforeTestExecutionCallbacks.evaluate(RunBeforeTestExecutionCallbacks.java:73)
         * 	at org.springframework.test.context.junit4.statements.RunAfterTestExecutionCallbacks.evaluate(RunAfterTestExecutionCallbacks.java:83)
         * 	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
         * 	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
         * 	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
         * 	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
         * 	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:251)
         * 	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
         * 	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
         * 	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
         * 	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
         * 	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
         * 	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
         * 	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
         * 	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
         * 	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
         * 	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
         * 	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
         * 	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
         * 	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
         * 	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
         * 	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
         *    {code}
         */
        assert redisTemplate.opsForValue().get("name") == null;

        redisTemplate.opsForValue().set("name", "My name is Isaac");

        assert redisTemplate.opsForValue().get("name") != null;

        System.out.println("redisTemplate.opsForValue().get(\"name\") = " + redisTemplate.opsForValue().get("name"));

    }

}
