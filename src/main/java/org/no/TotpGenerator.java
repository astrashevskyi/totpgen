package org.no;

import com.bastiaanjansen.otp.TOTPGenerator;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TotpGenerator {
    private final Dotenv dotenv;

    private TotpGenerator() {
        String WORK_DIR = System.getProperty("user.dir");
        dotenv = Dotenv.configure().filename("codes.env").ignoreIfMissing().directory(WORK_DIR).load();
    }

    private void work() {
        var users = dotenv.entries().stream().map(DotenvEntry::getKey).filter(key -> key.startsWith("totp"))
                .collect(Collectors.toList());
        if(users.isEmpty()) {
            System.out.println("no totp data provided, check your codes.env file");
            System.out.println("exiting...");
            System.exit(1);
        }
        Timer time = new Timer();
        var dummyTotp = TOTPGenerator.withDefaultValues("bla-bla".getBytes());
        var task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("============ ^_^ ============");
                users.stream().sorted().forEachOrdered(user -> {
                    var totpGenerator = TOTPGenerator.withDefaultValues(dotenv.get(user).getBytes());
                    var totpCode = totpGenerator.now();
                    System.out.printf("%s: %s\n", user, totpCode);
                });
            }
        };
        task.run();
        time.schedule(task, dummyTotp.durationUntilNextTimeWindow().toMillis(), dummyTotp.getPeriod().toMillis());
    }

    public static void main(String[] args) {
        new TotpGenerator().work();
    }
}
