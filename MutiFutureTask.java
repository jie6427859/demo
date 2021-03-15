package com.kyexpress.vms.vehicle.provider.utils;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * * @title: MutiFutureTask
 * * @author 159930
 * * @date 2020/12/7 19:41
 */
public class MutiFutureTask {
    /**
     * 带有回调机制的线程池
     */
    private static final ListeningExecutorService service = MoreExecutors.listeningDecorator(new ThreadPoolExecutor(50,
            50,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(50),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()));

    public static <T, V> List<V> batchExec(List<T> params, BatchFuture<T, V> batchFuture) {
        if(CollectionUtils.isEmpty(params)){
            return null;
        }
        final List<V> value = Collections.synchronizedList(new ArrayList<V>());
        try{
            List<ListenableFuture<V>> futureList = new ArrayList<ListenableFuture<V>>();
            for(T t : params){
                // 将实现了Callable的任务提交到线程池中，得到一个带有回调机制的ListenableFuture实例
                ListenableFuture<V> future = service.submit(new SingleTask<T, V>(t, batchFuture));
                Futures.addCallback(future, new FutureCallback<V>() {
                    @Override
                    public void onSuccess(V result) {
                        value.add(result);
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
                futureList.add(future);
            }
            ListenableFuture<List<V>> allAsList = Futures.allAsList(futureList);
            allAsList.get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
            e.printStackTrace();
        }

        return value;

    }

    /**
     *业务实现类
     * @param <T>
     * @param <V>
     */
    private static class SingleTask<T, V> implements Callable<V> {
        private T param;
        private BatchFuture<T, V> batchFuture;
        public SingleTask(T param, BatchFuture<T, V> batchFuture){
            this.param = param;
            this.batchFuture = batchFuture;
        }

        @Override
        public V call() throws Exception {
            return batchFuture.callback(param);
        }
    }
    public interface BatchFuture<T,V>{
        V callback(T param);
    }

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList();
        for (int i = 1; i <= 11; i ++){
            list.add(i);
        }
        for (int i = 0; i < 10; i ++){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<List<Integer>> subList = ListUtils.getSubList(10,list);
                    List<List<String>> result = MutiFutureTask.batchExec(subList, new BatchFuture<List<Integer>,List<String>>() {
                        @Override
                        public List<String> callback(List<Integer> param) {
                            List<String> result = Lists.newArrayList();
                            for (Integer i : param){
                                result.add(String.valueOf(i)+ "a");
                            }
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return result;
                        }
                    });
                    List<String> allList = Lists.newArrayList();
                    for (List<String> s : result){
                        allList.addAll(s);
                    }
                    System.out.println(subList+"=====["+Thread.currentThread()+"]======" + allList);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


}
