package av.is.leveledstorage.pool;

import av.is.leveledstorage.settings.Configuration;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Avis Network on 2017-11-01.
 */
public interface PoolExecutor {
    
    void execute(boolean async, Runnable run);
    
    void processQueue();
    
}
class PoolExecutorImpl implements PoolExecutor {
    
    private Configuration configuration;
    private SynchronousQueue<Runnable> runnables = new SynchronousQueue<Runnable>();
    private AtomicBoolean createdPool = new AtomicBoolean(false);
    
    public PoolExecutorImpl(Configuration configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public void execute(boolean async, Runnable runnable) {
        if(!configuration.useThreadPool || !async || !createdPool.get()) {
            createPool(async, runnable);
        }
        
        if(createdPool.get()) {
            runnables.add(runnable);
        }
    }
    
    void createPool(boolean async, Runnable runnable) {
        if(async) {
            if(configuration.useThreadPool) {
                createdPool.set(true);
            }
            Thread thread = new Thread(() -> process(runnable));
            thread.setDaemon(true);
            thread.start();
        } else {
            process(runnable);
        }
    }
    
    void process(Runnable runnable) {
        if(!configuration.useThreadPool) {
            processQueue();
            runnable.run();
        } else {
            while(configuration.useThreadPool) {
                try {
                    Thread.sleep(50L);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                if(runnables.isEmpty()) {
                    continue;
                }
                processQueue();
            }
            createdPool.set(false);
        }
    }
    
    @Override
    public void processQueue() {
        while(!runnables.isEmpty()) {
            runnables.poll().run();
        }
    }
}
