public class Main {
    public static void main(String[] args) {

        Server server = new Server();
        Client c1 = new Client(server,"Shisui");
        Client c2 = new Client(server,"Itachi");
    }
}