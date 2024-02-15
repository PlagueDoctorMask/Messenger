import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Server extends JFrame {
    private static final int WIDTH = 455;
    private static final int HEIGHT = 407;
    private boolean inWork;
    public static final String LOG_PATH = "src/log.txt";
    JTextArea log;
    JButton btnStart, btnExit;

    List<Client> clientList;

    Server(){
        clientList = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setTitle("Server");
        setResizable(false);
        log = new JTextArea();
        add(log);


        btnStart = new JButton("Start");
        btnExit = new JButton("Stop");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inWork = false;
                while (!clientList.isEmpty()){
                    disconnectUser(clientList.get(clientList.size()-1));
                }
                appendLog("Сервер остановлен!");
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                inWork = true;
                appendLog("Сервер запущен!");

            }
        });

        JPanel panBottom = new JPanel(new GridLayout(1, 2));
        panBottom.add(btnStart);
        panBottom.add(btnExit);

        add(panBottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void appendLog(String text){

        log.append(text + "\n");
    }

    public boolean connectUser(Client client){
        if (!inWork){
            return false;
        }
        clientList.add(client);
        return true;
    }

    public void message(String text){
        if (!inWork){
            return;
        }
        text += "";
        appendLog(text);
        answerAll(text);
        saveInLog(text);
    }

    private void answerAll(String text){
        for (Client client: clientList){
            client.answer(text);
        }
    }

    public void disconnectUser(Client client){
        clientList.remove(client);
        if (client != null){
            client.disconnectFromServer();
        }
    }

    private void saveInLog(String text){
        try (FileWriter writer = new FileWriter(LOG_PATH, true)){
            writer.write(text);
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    String readLog(){
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
