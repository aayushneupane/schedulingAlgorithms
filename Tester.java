import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Tester
{
    public static final float WANTED_TIME = 100;
    public static final int MAX_ITERATION = 5;
    public static final int MAX_FCFS = 40;
    public static final int MAX_SJF = 30;
    public static final int MAX_SRT = 30;
    public static final int MAX_RR = 40;
    public static final int MAX_HPFNP = 40;
    public static final int MAX_HPFP = 50;
    public static final String NL = System.getProperty("line.separator");
    
    public static void main(String[] args) throws FileNotFoundException
    {
        System.out.println("Choose the algorithm you want to use: ");
        System.out.println("FCFS, SJF, SRT, RR, HPR-NPA, HPF-PA");
    	Scanner in = new Scanner(System.in);
    	
    	String alg = in.nextLine();
    	
    	switch (alg){
    	
    	case "FCFS":
    		testFCFS();
    		break;
    	case "SJF":
    		testSJF();
    		break;
    	case "SRT":
    		testSRT();
    		break;
    	case "RR":
    		testRR();
    		break;
    	case "HPF-NPA":
    		testHPF_NPA();
    		break;
    	case "HPF-PA":
    		testHPF_PA();
    		break;
    	default: 
    		System.out.println("Choose an algorithm");
    	}
    	
        /**
        testFCFS();
        testSJF();
        testSRT();
        testRR();
        testHPF_NPNA();
        testHPF_NPA();
        testHPF_PNA();
        testHPF_PA();
        */
        System.out.println("Check local folder");
    }
    
    private static void testFCFS() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("FCFS_OUT.txt"))
        {
        	out.println("FirstComeFirstServe");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nFCFS processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testSJF() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("SJF_OUT.txt"))
        {
            out.println("ShortestJobFirst");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nSJF processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testSRT() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("SRT_OUT.txt"))
        {
        	out.println("ShortestRemainingTime");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nSRT processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testRR() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("RR_OUT.txt"))
        {
        	out.println("RoundRobin");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nRR processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testHPF_NPNA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_NPNA_OUT.txt"))
        {
        	out.println("HPF_NPNA");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nHPF_NPNA processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testHPF_NPA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_NPA.txt"))
        {
        	out.println("NPF_NPA");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nNPF_NPA processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testHPF_PNA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_PNA.txt"))
        {
        	out.println("HPF_PNA");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nHPF_PNA processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }
    
    private static void testHPF_PA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_PA.txt"))
        {
        	out.println("NPF_PA");
            for(int i = 1; i <= MAX_ITERATION; i++)
            {
                out.println("\n_________________________\n");
                out.println("\nHPF_PA processor #" + i);
  
                out.printf(ShortestJobFirst.processor(SimulatedProcess.GenMultiple(MAX_SJF), WANTED_TIME));
            }
        }
    }

}
