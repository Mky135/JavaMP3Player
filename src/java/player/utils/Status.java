package player.utils;

public  enum Status
{
    playing("Playing"),
    stopped("Stopped"),
    paused("Paused");

    String Status;

    Status(String state)
    {
        this.Status = state;
    }
}
