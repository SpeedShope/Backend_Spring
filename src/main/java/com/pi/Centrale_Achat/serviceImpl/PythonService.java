package com.pi.Centrale_Achat.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class PythonService {

    public Double predictAcceptance(String productData) throws Exception {
        // Call the Python script to predict the acceptance
        ProcessBuilder pb = new ProcessBuilder("python3", "C:\\Users\\User\\Desktop\\spring-update\\Centrale_AchatPi\\src\\main\\java\\com\\pi\\Centrale_Achat\\dataMaining\\linear_classifier.py", productData);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Read the output of the Python script
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        Double result = Double.parseDouble(line);

        // Wait for the process to finish and get the exit code
        boolean finished = process.waitFor(5, TimeUnit.SECONDS);
        int exitCode = process.exitValue();

        if (finished && exitCode == 0) {
            return result;
        } else {
            throw new Exception("Python script execution failed");
        }
    }
}
