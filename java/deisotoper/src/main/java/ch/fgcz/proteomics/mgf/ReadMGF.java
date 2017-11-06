package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class ReadMGF {
    // FILE
    private static String readHeader(String file) {
        try (BufferedReader bufferedreader = new BufferedReader(new FileReader(file))) {
            String line = bufferedreader.readLine();
            String[] partequal = line.split("=");

            while (line != null) {
                if (line.equals("BEGIN IONS")) {
                    break;
                } else if (line.contains("COM")) {
                    return partequal[1];
                }

                line = bufferedreader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // FILE
    private static MassSpectrometryMeasurement readLocal(String file, MassSpectrometryMeasurement MSM) {
        try (BufferedReader bufferedreader = new BufferedReader(new FileReader(file))) {
            String line = bufferedreader.readLine();
            int chargestate = 0;
            int id = 0;
            String searchengine = null;
            String typ = null;
            double peptidmass = 0;
            double rt = 0;
            List<Double> mz = new ArrayList<>();
            List<Double> intensity = new ArrayList<>();

            while (line != null) {
                String[] partequal = line.split("=");
                String[] partspace = line.split(" ");

                if (line.equals("BEGIN IONS")) {
                    chargestate = 0;
                    id = MSM.getMSlist().size();
                    searchengine = null;
                    typ = null;
                    peptidmass = 0;
                    rt = 0;
                    mz = new ArrayList<>();
                    intensity = new ArrayList<>();
                } else if (line.equals("END IONS")) {
                    MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
                } else if (line.contains("CHARGE")) {
                    chargestate = Integer.parseInt(partequal[1].substring(0, 1));
                } else if (line.contains("TITLE")) {
                    typ = partequal[1];
                } else if (line.contains("RTINSECONDS")) {
                    rt = Double.parseDouble(partequal[1]);
                } else if (line.contains("PEPMASS")) {
                    String[] pepmasssplit = partequal[1].split(" ");
                    peptidmass = Double.parseDouble(pepmasssplit[0]);
                } else if (isDouble(partspace[0]) && isDouble(partspace[1])) {
                    mz.add(Double.parseDouble(partspace[0]));
                    intensity.add(Double.parseDouble(partspace[1]));
                }

                line = bufferedreader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MSM;
    }

    // STDIN
    private static String readHeader() {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = bufferedreader.readLine();
            String[] partequal = line.split("=");

            while (line != null) {
                if (line.equals("BEGIN IONS")) {
                    break;
                } else if (line.contains("COM")) {
                    return partequal[1];
                }

                line = bufferedreader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // STDIN
    private static MassSpectrometryMeasurement readLocal(MassSpectrometryMeasurement MSM) {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = bufferedreader.readLine();
            int chargestate = 0;
            int id = 0;
            String searchengine = null;
            String typ = null;
            double peptidmass = 0;
            double rt = 0;
            List<Double> mz = new ArrayList<>();
            List<Double> intensity = new ArrayList<>();

            while (line != null) {
                String[] partequal = line.split("=");
                String[] partspace = line.split(" ");

                if (line.equals("BEGIN IONS")) {
                    chargestate = 0;
                    id = MSM.getMSlist().size();
                    searchengine = null;
                    typ = null;
                    peptidmass = 0;
                    rt = 0;
                    mz = new ArrayList<>();
                    intensity = new ArrayList<>();
                } else if (line.equals("END IONS")) {
                    MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
                } else if (line.contains("CHARGE")) {
                    chargestate = Integer.parseInt(partequal[1].substring(0, 1));
                } else if (line.contains("TITLE")) {
                    typ = partequal[1];
                } else if (line.contains("RTINSECONDS")) {
                    rt = Double.parseDouble(partequal[1]);
                } else if (line.contains("PEPMASS")) {
                    String[] pepmasssplit = partequal[1].split(" ");
                    peptidmass = Double.parseDouble(pepmasssplit[0]);
                } else if (isDouble(partspace[0]) && isDouble(partspace[1])) {
                    mz.add(Double.parseDouble(partspace[0]));
                    intensity.add(Double.parseDouble(partspace[1]));
                }

                line = bufferedreader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MSM;
    }

    // FILE
    public static MassSpectrometryMeasurement read(String file) {
        String source = readHeader(file);

        if (source == null) {
            source = file;
        }

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(source);

        MSM = readLocal(file, MSM);

        return MSM;
    }

    // STDIN
    public static MassSpectrometryMeasurement read() {
        String source = readHeader();

        if (source == null) {
            source = "stdin";
        }

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(source);

        MSM = readLocal(MSM);

        return MSM;
    }

    private static boolean isDouble(String str) {
        Scanner scanner = new Scanner(str);
        boolean b = scanner.hasNextDouble();
        scanner.close();

        return b;
    }
}
