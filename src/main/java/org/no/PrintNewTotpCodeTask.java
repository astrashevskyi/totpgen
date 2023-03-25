package org.no;

import com.bastiaanjansen.otp.TOTPGenerator;

import java.util.Objects;
import java.util.TimerTask;

public class PrintNewTotpCodeTask extends TimerTask {
    private String previousCode;
    private final TOTPGenerator totpGenerator;
    private final String name;

    public PrintNewTotpCodeTask(String name, TOTPGenerator totpGenerator) {
        this.totpGenerator = totpGenerator;
        this.name = name;
    }

    @Override
    public void run() {
        String newCode = totpGenerator.now();
        if(Objects.equals(newCode, previousCode)) {
            return;
        }
        previousCode = newCode;
        System.out.printf("%s: %s\n", name, newCode);
    }
}
