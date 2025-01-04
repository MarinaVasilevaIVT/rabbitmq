import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        int messageCount = 1;  // Счётчик сообщений
        int delay = 4000;     // Интервал отправки сообщений в миллисекундах

        if (args.length > 0) {
            messageCount = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            delay = Integer.parseInt(args[1]);
        }

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            System.out.println(" [*] Publisher started. Press Ctrl+C to exit.");

            while (true) {
                String message = "Task " + messageCount++;

                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");

                Thread.sleep(delay);
            }
        } catch (Exception e) {
            System.err.println(" [!] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
