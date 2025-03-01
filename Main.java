public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello from Jenkins!");
        // Konteynerin kapanmasını önlemek için sonsuz döngü
        while (true) {
            Thread.sleep(9000); // 1 saniye bekle
        }
    }
}
