package ir.rasen.myapplication.helper;

public class FriendshipRelation {

        public enum Status {

            FRIEND,
            NOT_FRIEND,
            REQUEST_SENT,
            REQUEST_REJECTED,
            ME
        }

    public static Status getFromCode(int code){
        switch (code){
            case 0:
                return Status.ME;
            case 1:
                return Status.FRIEND;
            case 2:
                return Status.NOT_FRIEND;
            case 3:
                return Status.REQUEST_SENT;
            case 4:
                return Status.REQUEST_REJECTED;
        }
        return Status.NOT_FRIEND;
    }

}
