package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-24
 * @url https://github.com/google/gson and https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.1
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MassSpectrometryMeasurementSerializer {
    public static String serializeToJson(String fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        Gson gson = new Gson();

        String data = gson.toJson(massSpectrometryMeasurement);

        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static MassSpectrometryMeasurement deserializeFromJson(String fileName) {
        Gson gson = new Gson();

        String data = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringbBuilder = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                stringbBuilder.append(line);
                stringbBuilder.append(System.lineSeparator());
                line = br.readLine();
            }
            data = stringbBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        java.lang.reflect.Type type = new TypeToken<MassSpectrometryMeasurement>() {
        }.getType();

        return gson.fromJson(data, type);
    }
}