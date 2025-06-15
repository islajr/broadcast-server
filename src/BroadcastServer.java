import java.util.List;

public class BroadcastServer {
    public static void main(String[] args) {

        String usageMessage = "Usage: broadcast-server <command>";
        String startMessage = "Usage: broadcast-server start -p <port>";
        String port = null;
        List<String> commands = List.of(new String[]{"start", "connect"});

        if (args.length == 2 || args.length == 4) {

            if (args[0].equals("broadcast-server")) {
                if (commands.contains(args[1].toLowerCase())) {
                    if (args[1].equalsIgnoreCase("start")) {
                        try {

                            if (args[2].equalsIgnoreCase("-p")) {
                                port = args[3];
                                // start the server
                                System.out.println("server started on port " + port);
                            } else {
                                System.out.println(startMessage);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println(startMessage);
                        }
                    } else if (args[1].equalsIgnoreCase("connect")) {
                        // connect to the server
                        System.out.println("connected to server on port");
                    } else {
                        System.out.println(usageMessage);
                    }
                } else {
                    System.out.println(usageMessage);
                }
            } else {
                System.out.println(usageMessage);
            }
        } else {
            System.out.println(usageMessage);
        }
    }
}