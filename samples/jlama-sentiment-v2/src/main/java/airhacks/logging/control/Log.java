package airhacks.logging.control;

public interface Log {

    public static void info(String message){
        System.out.println(message);
    }

    public static void info(Object message){
        info(message.toString());
    }
}
