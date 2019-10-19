package com.kalaari.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newrelic.api.agent.NewRelic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
@EnableAutoConfiguration
public class SpringAsyncConfig implements AsyncConfigurer {

    @Value("${custom.async.core-pool-size}")
    private Integer asyncCorePoolSize;

    @Value("${custom.async.max-pool-size}")
    private Integer asyncMaxPoolSize;

    @Value("${custom.async.keep-alive-seconds}")
    private Integer keepAliveSeconds;

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncCorePoolSize);
        executor.setMaxPoolSize(asyncMaxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            String paramsToString = Arrays.toString(params);
            log.error("Uncaught exception in async: " + method.getClass() + ":" + method.getName() + " with params: "
                    + paramsToString + "error: " + ex.getMessage(), ex);

            HashMap<String, String> newrelicAtributes = new HashMap<>();
            newrelicAtributes.put("method", method.getName());
            newrelicAtributes.put("class", method.getClass().getName());
            newrelicAtributes.put("declaringClass", method.getDeclaringClass().getName());
            newrelicAtributes.put("params", paramsToString);
            NewRelic.noticeError(ex, newrelicAtributes);
        };
    }

    class MdcTaskDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            // Right now: Web thread context !
            // (Grab the current thread MDC data)
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    // Right now: @Async thread context !
                    // (Restore the Web thread context's MDC data)
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } catch (Exception e) {
                    log.error("Exception in async decorate: ", e);
                    NewRelic.noticeError(e);
                } finally {
                    MDC.clear();
                }
            };
        }
    }

}
