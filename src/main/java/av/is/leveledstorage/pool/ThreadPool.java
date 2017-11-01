package av.is.leveledstorage.pool;

import av.is.leveledstorage.settings.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Avis Network on 2017-11-01.
 */
public class ThreadPool {
    
    private final Configuration configuration;
    
    private List<PoolExecutor> executors = new ArrayList<>();
    private AtomicInteger index = new AtomicInteger(0);
    
    public ThreadPool(Configuration configuration) {
        this.configuration = configuration;
        
        for(int i = 0; i < configuration.threadPoolCount; i++) {
            executors.add(new PoolExecutorImpl(configuration));
        }
    }
    
    /**
     * 스레드 풀 방식을 이용한 실행 또는 구 버전의 실행 방식을 지원하는 메소드입니다.
     *
     * @param async 으로 비동기/동기 실행 설정이 가능합니다.
     * @param runnable 이 모든 작업이 끝난 후 실행됩니다.
     */
    public void execute(boolean async, Runnable runnable) {
        if(!configuration.useThreadPool) {
            executors.get(0).execute(async, runnable);
        } else {
            executors.get(index.get()).execute(async, runnable);
            index.set((index.get() + 1) % executors.size() - 1);
        }
    }
    
    /**
     * 스레드 풀에 저장된 모든 큐를 비웁니다.
     * 데이터 초기화에 사용됩니다.
     *
     * 사용중인 메소드는 아닙니다.
     */
    public void clearAllQueue() {
        executors.forEach(PoolExecutor::processQueue);
    }
}
