package org.no;

import com.bastiaanjansen.otp.TOTPGenerator;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.Timer;
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
        users.forEach(user -> {
                var secretCode = dotenv.get(user);
                var task = new PrintNewTotpCodeTask(user, TOTPGenerator.withDefaultValues(secretCode.getBytes()));
                time.schedule(task, 0, 1000);
            }
        );
    }

    public static void main(String[] args) {
        new TotpGenerator().work();
    }
}
