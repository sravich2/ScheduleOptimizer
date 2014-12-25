import sun.org.mozilla.javascript.internal.ast.ArrayComprehensionLoop;

import java.util.ArrayList;

/**
 * Created by Sachin on 10-12-2014.
 */
public class Schedule {

    public ArrayList<Meeting> mainSchedule;
    public ArrayList<String> CRNList = new ArrayList<String>();
    public ArrayList<ArrayList<Meeting>> twoDimensionalSchedule;

    public Schedule(ArrayList<Meeting> schedule){
        this.mainSchedule = schedule;
    }
    public Schedule(ArrayList<Meeting> schedule, ArrayList<String> crnList){
        this.CRNList = new ArrayList<String>(crnList);
        this.mainSchedule = schedule;
    }
    public Schedule(Schedule copyThis){
        this.mainSchedule = copyThis.mainSchedule;
        this.CRNList = new ArrayList<String>(copyThis.CRNList);
    }
    public String toString(){
        return mainSchedule.toString();
    }

}
