package ir.rasen.myapplication.helper;

public class WorkTime {

    public int time_open_hour;
    public int time_open_minutes;
    public int time_close_hour;
    public int time_close_minutes;
    public boolean[] workDays = new boolean[7];

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
                result += String.valueOf(i+1)+",";
        }

        return result.substring(0,result.length()-1);
    }


    public void setWorkDaysFromString(String workDaysString)throws Exception{
        String[] workDaysArrays = workDaysString.split(",");
        for (String str : workDaysArrays)
            workDays[Integer.valueOf(str)] = true;
    }

    public String getTimeWorkOpenString(){
        return time_open_hour+":"+time_open_minutes;
    }

    public String getTimeWorkCloseString(){
        return time_close_hour+":"+time_close_minutes;
    }

    public String getTimeWorkOpenWebservice(){
        return String.valueOf(time_open_hour)+ String.valueOf(time_open_minutes);
    }

    public String getTimeWorkCloseWebservice(){
        return String.valueOf(time_close_hour)+String.valueOf(time_close_minutes);
    }


    public void setTimeWorkOpenFromString(String timeOpen) throws  Exception{
        time_open_hour = Integer.valueOf(timeOpen.substring(0,timeOpen.indexOf(":")));
        time_open_minutes = Integer.valueOf(timeOpen.substring(timeOpen.indexOf(":")+1,timeOpen.length()));
    }

    public void setTimeWorkCloseFromString(String timeClose)throws Exception{
        time_open_hour = Integer.valueOf(timeClose.substring(0,timeClose.indexOf(":")));
        time_open_minutes = Integer.valueOf(timeClose.substring(timeClose.indexOf(":")+1,timeClose.length()));
    }


}
