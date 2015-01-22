package ir.rasen.myapplication.helper;

public class WorkTime {

    public int time_open;
    public int time_close;
    private boolean[] workDays = new boolean[7];

    public void addWorkDay(int dayOfWeek) throws Exception {
        if (dayOfWeek < 1 || dayOfWeek > 7)
            throw new Exception("Invalid work day");
        workDays[dayOfWeek-1] = true;
    }

    public boolean[] getWorkDays() {
        return this.workDays;
    }

    public String getWorkDaysString() {
        String result = "";
        for (int i = 0; i < 7; i++) {
            if (workDays[i])
                result += String.valueOf(i)+",";
        }

        return result.substring(0,result.length()-1);
    }

    public void setWorkDaysFromString(String workDaysString)throws Exception{
        String[] workDaysArrays = workDaysString.split(",");
        for (String str : workDaysArrays)
            workDays[Integer.valueOf(str)] = true;
    }

    public int getTime_open() { return this.time_open; }
    public int getTime_close() { return this.time_close; }

}
