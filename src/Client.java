import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Client extends JFrame {
    private static final int WIDTH = 455;
    private static final int HEIGHT = 407;

    JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    JPasswordField password;
    JButton btnLogin, btnSend;
    JPanel headerPanel;
    JTextArea log;
    private final Server server;
    private String name;
    private boolean isConnected;





    Client(Server server, String client){
        this.server = server;
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setTitle(client);
        setResizable(false);
        setLocation(server.getX() + 500, server.getY());
        //Верхняя панель
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField(client);
        password = new JPasswordField("123456");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                connectToServer();

            }
        });
        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);
        add(headerPanel, BorderLayout.NORTH);
        // Сюда писать
        log = new JTextArea();
        add(log);
        // Сюда вводить
        JPanel footerpanel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    if (isConnected){
                        String text = tfMessage.getText();
                        if (!text.equals("")){
                            server.message(name + ": " + text);
                            tfMessage.setText("");
                        }
                    }
                    else {
                        appendLog("Нет подключения к серверу");
                    }
                }
            }
        });
        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnected){
                    String text = tfMessage.getText();
                    if (!text.equals("")){
                        server.message(name + ": " + text);
                        tfMessage.setText("");
                    }
                }else{
                appendLog("Нет подключения к серверу");
                }
            }
        });
        footerpanel.add(tfMessage);
        footerpanel.add(btnSend, BorderLayout.EAST);
        add(footerpanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void appendLog(String text){

        log.append(text + "\n");
    }
    private void connectToServer(){
        if (server.connectUser(this)){
            isConnected = true;
            appendLog("Вы успешно подключились");
            headerPanel.setVisible(false);
            name = tfLogin.getText();
            String log = server.readLog();
            if (log != null){
                appendLog(log);
            }
        }
        else{
            appendLog("Не удалось подключиться");
        }
    }

    public void answer(String text){

        appendLog(text);
    }

    public void disconnectFromServer() {
        if (isConnected) {
            headerPanel.setVisible(true);
            isConnected = false;
            server.disconnectUser(this);
            appendLog("Вы были отключены от сервера!");
        }
    }



}
