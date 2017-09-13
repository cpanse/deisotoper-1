package ch.fgcz.proteomics.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Convert {
    public static ch.fgcz.proteomics.mspy.Peaklist msdToPeaklist(String file) {
        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("peak mz")) {
                    String[] parts = line.split("\"");
                    if (parts.length <= 7) {
                        charge.add(-1);
                        isotope.add(-1.0);
                    }
                    for (int i = 0; i < parts.length; i++) {
                        if (i == 1) {
                            mz.add(Double.parseDouble(parts[i]));
                        } else if (i == 3) {
                            intensity.add(Double.parseDouble(parts[i]));
                        } else if (i == 7) {
                            charge.add(Integer.parseInt(parts[i]));
                        } else if (i == 9) {
                            isotope.add(Double.parseDouble(parts[i]));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ch.fgcz.proteomics.mspy.Peaklist(mz, intensity, charge, isotope);
    }

    public static ch.fgcz.proteomics.mspy.Peaklist mgfToPeaklist(String file) {
        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    if (i == 0) {
                        mz.add(Double.parseDouble(parts[i]));
                    } else if (i == 1) {
                        intensity.add(Double.parseDouble(parts[i]));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ch.fgcz.proteomics.mspy.Peaklist(mz, intensity);
    }

    public static void main(String[] args) {
        ch.fgcz.proteomics.mspy.Peaklist plist2 = msdToPeaklist("/srv/lucas1/Downloads/output1.msd");

        ch.fgcz.proteomics.mspy.Peaklist plist = mgfToPeaklist("/srv/lucas1/Downloads/mgffromhelafiltered");

        for (ch.fgcz.proteomics.mspy.Peak e : plist.getPeaklist()) {
            System.out.println("MZ: " + e.getMz() + ", INTENSITY:" + e.getIntensity() + ", CHARGE:" + e.getCharge() + ", ISOTOPE:" + e.getIsotope());
        }
    }
}
