package IP_2375;

import integrationproject.algorithms.Algorithms;
import integrationproject.model.BlackAnt;
import integrationproject.model.RedAnt;
import integrationproject.utils.InputHandler;
import integrationproject.utils.Visualize;
import java.util.ArrayList;

/**
 * @author Eirini Mitsopoulou
 */
public class IP_2375 extends Algorithms{

    public static void main(String[] args) {
       // checkParameters(args);
        
        //create Lists of Red and Black Ants
        int flag = Integer.parseInt(args[1]);
        ArrayList<RedAnt> redAnts = new ArrayList<>();
        ArrayList<BlackAnt> blackAnts = new ArrayList<>();
        if (flag == 0) {
            InputHandler.createRandomInput(args[0], Integer.parseInt(args[2]));
        }
        InputHandler.readInput(args[0], redAnts, blackAnts);
          
        IP_2375 algs = new IP_2375();
        
        //debugging options
        boolean visualizeMST = false;
        boolean visualizeSM = false;
        boolean printCC = false;
        boolean evaluateResults = true;

        if(visualizeMST){
            int[][] mst = algs.findMST(redAnts, blackAnts);
            if (mst != null) {
                Visualize sd = new Visualize(redAnts, blackAnts, mst, null, "Minimum Spanning Tree");
                sd.drawInitialPoints();
            }
        }

        if(visualizeSM){
            int[][] matchings = algs.findStableMarriage(redAnts, blackAnts);          
            if (matchings != null) {
                Visualize sd = new Visualize(redAnts, blackAnts, null, matchings, "Stable Marriage");
                sd.drawInitialPoints();
            }
        }

        if(printCC){
            int[] coinChange = algs.coinChange(redAnts.get(0), blackAnts.get(0)); 
            System.out.println("Capacity: " + redAnts.get(0).getCapacity());
            for(int i = 0; i < blackAnts.get(0).getObjects().length; i++){
                System.out.println(blackAnts.get(0).getObjects()[i] + ": " + coinChange[i]);
            }
        }
        
        if(evaluateResults){
            System.out.println("\nEvaluation Results");
            algs.evaluateAll(redAnts, blackAnts);
        }
    }

    /**
     * We use Kruskal algorithm to find the Minimun Spanning Tree. In this tree we add edges in increasing weight, skipping those who would create a cycle (Union find method).
     * @param redAnts
     * @param blackAnts
     * @return mst
     */
    @Override
    public int[][] findMST(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts) {
        int i=0,j=0;
        int[][] mst=new int[2*redAnts.size()-1][4]; // the final array who contains all the eadges of the Minimum Spanning Tree
        double[][] weight=new double[2*redAnts.size()][2*redAnts.size()];
        int []parent = new int[2*redAnts.size()]; 
        // connect with edges each red with black ant (for each edge we know which the id of the ants and their weight) 
        for (RedAnt x: redAnts ){ //We number the red ants from 0 till redAnts.size -1
            for (BlackAnt y: blackAnts ){ //We number the black ants from redAnts.size till 2*redAnts.size-1  to separate them from the red ones            
                 weight[i][j+redAnts.size()] = Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
                j++; // the number of the black ant      
            }
           i++; //the number of the red ant
           j=0;
        }
        i=0;j=0;
        // connect with edges only the red ants eachother
        for (RedAnt x: redAnts ){
            for (RedAnt y: redAnts ){ 
                if(i!=j) //if it isn't the same red ant
                 weight[i][j] = Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
                j++;       
            }
           i++;
           j=0;
        }
        i=0;j=0;
        // connect with edges only the black ants eachother
        for (BlackAnt x: blackAnts ){
            for (BlackAnt y: blackAnts ){ 
                if(i!=j) //if it isn't the same black ant
                 weight[i+redAnts.size()][j+redAnts.size()] = Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
                j++;       
            }
           i++;
           j=0;
        }
        // Kruskal Algorithm
        int eadges =1,a=100000,b=100000,c=100000,d=100000,l=0;
        double min=100000;
        for(i=0;i<2*redAnts.size();i++)
        {
         for(j=0;j<2*redAnts.size();j++){
              if(weight[i][j]==0) // if a value in the weight array = 0 that means tha i and j are not connected with eadges. We need to change 0 with a huge value because we want to be sure that i and j won't be included in the mst  
                    weight[i][j]=100000;}
        }
           
        while(eadges<2*redAnts.size())
        {
         for(i=0,min=100000;i<2*redAnts.size();i++)
         {
          for(j=0;j<2*redAnts.size();j++)
          {
           if(weight[i][j]<min)
           {
            min=weight[i][j];
            a=c=i;
            b=d=j;
           }
          }
         }
         
         while(parent[c]!=0){
            c=parent[c];
         }
         while(parent[d]!=0){
            d=parent[d];
         } 
          // Union Find       
         int p=0;
         
           if(c!=d) //if it's not create a cycle
           {
            parent[c]=d; 
            p= 1; // add it in the tree
           }
           
         if(p==1)
         {
          
          mst[l][0] = a; // Insert the first ant
            if(mst[l][0]<redAnts.size()) // if the number of the ant is smaller than the redAnts size then is red
                mst[l][1] = 0;
            else{ // else is black
                mst[l][1] = 1;
                mst[l][0] = (a - redAnts.size());
            }
            mst[l][2] = b; // Insert the second ant which is connected with the first one
            if(mst[l][2]<redAnts.size())
                mst[l][3] = 0;
            else{
                mst[l][3] = 1;
                mst[l][2] = (b - redAnts.size());
            }            
            l++;eadges++;         
         }
         weight[a][b]=weight[b][a]=100000;
        }
       
       return mst;
    }

    /**
     * It is known that every instance of the Stable Marriage problem admits at least one stable matching using an efficient algorithm known as the Gale/Shapley algorithm.
     * According to this algorithm every free red ant goes to all black ants in its preference list according to the distances. For every black ant that red makes a proposal, red checks if the black is free. if it is, they both become engaged. 
     * If the black is not free, either says no to red or leave its current engagement according to the preference list. So an engagement can broke if a black ant gets better option.
     * @param redAnts
     * @param blackAnts
     * @return marriage
     */
    @Override
    public int[][] findStableMarriage(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts) {
        double min = 1000000;
        int idy = -1,i=0;
        int[] usedIds = new int[redAnts.size()];
        for(int k=0; k<redAnts.size(); k++)
            usedIds[k] = -1;
        int[][]  marriage = new int[redAnts.size()][2];
        
        double[][] redAntDist = new double[redAnts.size()][redAnts.size()];
        int count1=0,count2=0;
        for (RedAnt x: redAnts ){ //we add at the redAntDist array each red ant with black ant distance where count1=redantID and count2=blackantID
            for (BlackAnt y: blackAnts ){
                redAntDist[count1][count2]= Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
                 count2++;       
            }
            count2=0;
            count1++;
        }
        double[][] blackAntDist = new double[redAnts.size()][redAnts.size()];
        count1=0;count2=0;
        for (BlackAnt x: blackAnts ){
            for (RedAnt y: redAnts ){//we add at the blackAntDist array each black ant with red ant distance where count1=blackantID and count2=redantID
                blackAntDist[count1][count2]= Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));          
                count2++;       
            }
            count2=0;
            count1++;
        }
        int[] redA = new int[redAnts.size()];
        int[] blackA = new int[redAnts.size()];
        int[] blackPartner = new int[redAnts.size()];
        int[][] redPreferences = new int[redAnts.size()][redAnts.size()];
        int[][] blackPreferences = new int[redAnts.size()][redAnts.size()];
        for( int k=0; k<redAnts.size(); k++)  {    
            for(  i=0; i < redAnts.size(); i++){
                redPreferences[k][i] = i; //we create the  array redPreferences where we keep all the blackIDs to keep the row preference of each red ant when we sort the RedAntDist array
                blackPreferences[k][i] = i; //the same for the black ants
            }
            redA[k] = k; //save the red ants ids 
            blackA[k] = k; //save the black ants ids 
            blackPartner[k] = -1; //black ants have no partners at first
        }
        
        redPreferences = bubbleSort(redAntDist,redPreferences,redAnts.size()); //we sort from min to max redAntDist array and redPreferences array at the same time
        blackPreferences = bubbleSort(blackAntDist,blackPreferences,redAnts.size()); //  we sort from min to max blackAntDist array and blackPreferences array at the same time       
              
        boolean[] redEngaged = new boolean[redAnts.size()]; // this array contains if the red ants  are engaged or not
        int count = 0; //count the engagements
        while (count < redAnts.size())
        {   
            int u =0;
            for (u = 0; u < redAnts.size(); u++)
                if (!redEngaged[u]) // if the red ant is not engaged
                    break;
 
            for ( i = 0; i < redAnts.size() && !redEngaged[u]; i++)
            {
                int v=0,k=0;
                for (int j = 0; j < redAnts.size(); j++)
                    if (blackA[j] ==redPreferences[u][i])
                        v= j; k++;
                if(k==0)
                    v = -1;
                
                if (blackPartner[v] == -1) //if black is not engaged
                {
                    blackPartner[v] = redA[u]; //the black engaged with the red ant
                    redEngaged[u] = true;
                    count++;
                }
                else
                {
                    int currentPartner = blackPartner[v];
                    int preference=0;
                    for (int j = 0; j < redAnts.size(); j++) //find the best preference of the black ant
                    {
                        if (blackPreferences[v][j]==redA[u]){ //if the black ant prefers better this red ant they get engaged
                            preference = 1; break;} 
                        if (blackPreferences[v][j]==currentPartner){ //if black ant prefers his current partner
                            preference = 0; break;}
                    }
                                          
                    if (preference == 1) //if black ant prefers a new red partner
                    {
                        int bool =0;k=0;
                        blackPartner[v] = redA[u]; //tha black being engaged with the new red partner
                        redEngaged[u] = true; //the red become engaged
                        for ( i = 0; i < redAnts.size(); i++)
                            if (redA[i]== currentPartner ) //we find the id of the red ant that the black left behind
                                bool=i; k++;
                        if(k==0)
                            bool = -1;
                        redEngaged[bool] = false; // this red ant becomes single again
                    }
                }
            }
            int j = 0;          
            for ( i = 0; i < redAnts.size(); i++)
            {            //save the final couples 
                marriage[i][j] = blackPartner[i]; j++; //the red ant
                marriage[i][j] = blackA[i]; j=0;        // the black ant       
            }
        }
             
        return marriage;
    }

    /**
     * We find with dynamic programming the minimum number of seeds that are needed to full the bucket 
     * @param redAnt
     * @param blackAnt
     * @return seeds
     */
    @Override
    public int[] coinChange(RedAnt redAnt, BlackAnt blackAnt) {      
       int i=0;
       int[] s = new int [5]; //we keep all the weights of the seeds    
       int[] seeds = new int [5]; // how much of each seed we need to full the bucket
       
       for (int ant: blackAnt.getObjects()){
                s[i] = ant;             
                i++;
       }
     
       int [ ] numberOfSeeds = new int[ redAnt.getCapacity() + 1 ]; //we keep the number of seeds we used, to full the bucket
       int [ ] seedsUsed = new int[ redAnt.getCapacity() + 1 ];
       seedsUsed[ 0 ] = 1; // we keep 1 kilo in case we need only 1 kilo seed
        for( i  = 1; i <= redAnt.getCapacity() ; i++ )
        {
            int minSeeds = i;
            for( int j = 0; j < 5; j++ ) // 5 is the different seeds
            {
                if( s[ j ] > i )   // if this seed has bigger capacity than the avaliable bucket capicity, go to the next seed
                    continue;
                if( numberOfSeeds[ i - s[ j ] ] + 1 < minSeeds ) //if we increase(+1) the number of seeds we use and is smaller than the minSeeds we put this seed in the bucket
                {
                    minSeeds = numberOfSeeds[ i - s[ j ] ] + 1; //increase the number of seeds each time we use one seed to full the bucket (at the end numberOfSeeds[redAnts.size] will give us the number, because that will mean that we fulled the bucket)
                    seedsUsed[ i ]  = s[ j ]; // we count that we used this seed
                }
            }
            numberOfSeeds[ i ] = minSeeds;           
       }
        
       for(  i = redAnt.getCapacity(); i > 0; )
        {
            for(int j = 0; j < 5; j++){
                if(s[j] == seedsUsed[ i ]) //if we used this seed
                    seeds[j]++; // count how much times we used each seed, started from 0
            }    
            i = i - seedsUsed[ i ]; //unlit the bucket is empty
        }      
       return seeds;
    }
    
    private int[][] bubbleSort(double[][] weight,int[][] preferences,int size){
        double temp;
        int temp1;
        for(int k=0; k<size; k++)  {    
            for( int i=0; i < size; i++){
                     for(int j=1; j < (size-i); j++){                              
                                if(weight[k][j-1] > weight[k][j]){
                                        //swap the elements!
                                        temp = weight[k][j-1];
                                        weight[k][j-1] = weight[k][j];
                                        weight[k][j] = temp;
                                        temp1 = preferences[k][j-1];
                                        preferences[k][j-1] = preferences[k][j];
                                        preferences[k][j] = temp1;
                                }                            
                        }
               }
        }
        return preferences;
    }
         
    
    private static void checkParameters(String[] args) {
        if (args.length == 0 || args.length < 2 || (args[1].equals("0") && args.length < 3)) {
            if (args.length > 0 && args[1].equals("0") && args.length < 3) {
                System.out.println("3rd argument is mandatory. Represents the population of the Ants");
            }
            System.out.println("Usage:");
            System.out.println("1st argument: name of filename");
            System.out.println("2nd argument: 0 create random file, 1 input file is given as input");
            System.out.println("3rd argument: number of ants to create (optional if 1 is given in the 2nd argument)");
            System.exit(-1);
        }
    }   
}
