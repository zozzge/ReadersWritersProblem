package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


public class Test {
	public static void main(String [] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		ReadWriteLock RW = new ReadWriteLock();
		
		
		for (int i = 0; i < 4; i++) {
            executorService.execute(new Writer(RW));
            executorService.execute(new Reader(RW));
        }
                executorService.shutdown();
		
	}
}


class ReadWriteLock{
	private final Semaphore mutex=new Semaphore(1);
        private final Semaphore writeMutex = new Semaphore(1);
        private int readerCount = 0;
	
	public void readLock() {
             try {
            mutex.acquire(); 
            readerCount++;
            if (readerCount == 1) {
                writeMutex.acquire(); 
            }
            mutex.release(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
		
		
	}
	public void writeLock() {
             try {
            writeMutex.acquire(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
		
	}
	public void readUnLock() {
            try {
            mutex.acquire(); 
            readerCount--;
            if (readerCount == 0) {
                writeMutex.release(); 
            }
            mutex.release(); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
		
	}
	public void writeUnLock() {
            writeMutex.release();
		
	}

}




class Writer implements Runnable
{
   private final ReadWriteLock RW_lock;
   

    public Writer(ReadWriteLock rw) {
    	RW_lock = rw;
   }

    public void run() {
      while (true){
    	  RW_lock.writeLock();
    	
    	  RW_lock.writeUnLock();
       
      }
   }
}

class Reader implements Runnable
{
   private final ReadWriteLock RW_lock;

   public Reader(ReadWriteLock rw) {
    	RW_lock = rw;
   }
    public void run() {
      while (true){ 	    	  
    	  RW_lock.readLock();
    	  RW_lock.readUnLock();
       
      }
   }


}