import java.util.*;
public class RaceCond2{
    static boolean keepGoing = true;
    static int k1, k2, t1, t2, data, runs = -1, next_in = 0, next_out = 0;
    public static void main(String args[]){
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter n (array size): ");
        int n = kb.nextInt();
        System.out.print("Enter k (max burst length): ");
        int k = kb.nextInt();
        System.out.print("Enter t (max sleep time): ");
        int t = kb.nextInt();
        System.out.print("Enter max number of consumer runs: ");
        int finish = kb.nextInt();
        kb.close();
        int buffer[] = new int[n];//Array of n, value of 0 by default
        
        Random rand = new Random();
        Thread producer = new Thread(new Runnable(){
            public void run()
            {
                while(keepGoing){
                    k1 = rand.nextInt((k + 1)); //random int 0 to k
                    for(int i = 0; i<k1; i++){
                        if(keepGoing){
                            P(buffer[(next_in + i)%n]);
                        }else{
                            break;
                        }
                    }
                    next_in = (next_in + k1)%n;
                    t1 = rand.nextInt((t + 1)); //random int 0 to t
                    try{
                        Thread.sleep(t1);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread consumer = new Thread(new Runnable(){
            public void run()
            {
                
                while(keepGoing){
                    t2 = rand.nextInt((t + 1)); //random int 0 to t
                    try{
                        Thread.sleep(t2);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    k2 = rand.nextInt((k + 1)); //random int 0 to k
                    for(int i = 0; i<k2; i++){
                        data = buffer[(next_out + i)%n];
                        if((next_out + i)%n == 0){
                            runs++;
                        }
                        if(data > 1){
                            System.out.println("Race condition found at index " + i + " of value " + buffer[(next_out + i)%n]
                                                + " after " + runs + " full run(s). The consumer was not fast enough." );
                            keepGoing = false;
                            break;
                        }if(runs > finish){
                            System.out.println("Reached maximum number of runs without a race condition." );
                            keepGoing = false;
                            break;
                        }else{
                            buffer[(next_out + i)%n] = 0;
                        }
                        next_out = (next_out + k2)%n;
                    }
                }
            }
        });
        producer.start();
        consumer.start();
    }
    public static void P(int s){
        while((s == 0) && !keepGoing);
        s = s-1;
        return;
    }
}
