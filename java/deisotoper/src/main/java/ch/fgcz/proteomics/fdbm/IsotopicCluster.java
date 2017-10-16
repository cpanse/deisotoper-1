package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicCluster {
    private final static double H_MASS = 1.008;
    private List<Peak> isotopiccluster = new ArrayList<>();
    private int charge;
    private String status;
    private int clusterID;

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterid) {
        this.clusterID = clusterid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public List<Peak> getIsotopicCluster() {
        return isotopiccluster;
    }

    public void setIsotopicCluster(List<Peak> isotopicCluster) {
        this.isotopiccluster = isotopicCluster;
    }

    public IsotopicCluster(List<Peak> isotopiccluster) {
        this.isotopiccluster = isotopiccluster;
        this.charge = 0;
        this.clusterID = 0;
    }

    public IsotopicCluster(List<Peak> isotopiccluster, int charge) {
        this.isotopiccluster = isotopiccluster;
        this.charge = charge;
    }

    public IsotopicCluster(String status) {
        this.isotopiccluster = null;
        this.charge = 0;
        this.status = status;
    }

    public IsotopicCluster simpleAggregateFirst() {
        double intensitysum = this.sumIntensity();

        this.isotopiccluster.get(0).setIntensity(intensitysum);
        if (this.isotopiccluster.size() == 2) {
            this.isotopiccluster.remove(1);
        } else if (this.isotopiccluster.size() == 3) {
            this.isotopiccluster.remove(2);
            this.isotopiccluster.remove(1);
        }

        this.isotopiccluster.get(0).setMz(this.isotopiccluster.get(0).getMz() * this.charge - (this.charge - 1) * H_MASS);
        return this;
    }

    public IsotopicCluster simpleAggregateLast() {
        double intensitysum = this.sumIntensity();

        this.isotopiccluster.remove(0);
        if (this.isotopiccluster.size() == 1) {
            this.isotopiccluster.get(0).setIntensity(intensitysum);
        } else if (this.isotopiccluster.size() == 2) {
            this.isotopiccluster.remove(0);
            this.isotopiccluster.get(0).setIntensity(intensitysum);
        }

        this.isotopiccluster.get(0).setMz((this.isotopiccluster.get(0).getMz() * this.charge) - (this.charge - 1) * H_MASS);
        return this;
    }

    public IsotopicCluster simpleAggregateMean() {
        double intensitysum = this.sumIntensity();

        double mzmean = 0;
        for (Peak p : this.isotopiccluster) {
            mzmean += p.getMz();
        }

        mzmean = mzmean / this.isotopiccluster.size();

        this.isotopiccluster.get(0).setIntensity(intensitysum);
        this.isotopiccluster.get(0).setMz(mzmean);
        if (this.isotopiccluster.size() == 2) {
            this.isotopiccluster.remove(1);
        } else if (this.isotopiccluster.size() == 3) {
            this.isotopiccluster.remove(2);
            this.isotopiccluster.remove(1);
        }

        this.isotopiccluster.get(0).setMz((this.isotopiccluster.get(0).getMz() * this.charge) - (this.charge - 1) * H_MASS);
        return this;
    }

    public IsotopicCluster advancedAggregateHighest() {
        double intensitysum = this.sumIntensity();
        double minint = 0;
        double minmz = 0;

        for (Peak p : this.isotopiccluster) {
            if (p.getIntensity() > minint) {
                minint = p.getIntensity();
                minmz = p.getMz();
            }
        }

        this.isotopiccluster.get(0).setIntensity(intensitysum);
        this.isotopiccluster.get(0).setMz(minmz);
        if (this.isotopiccluster.size() == 2) {
            this.isotopiccluster.remove(1);
        } else if (this.isotopiccluster.size() == 3) {
            this.isotopiccluster.remove(2);
            this.isotopiccluster.remove(1);
        }

        return this;
    }

    private double sumIntensity() {
        double intensitysum = 0;
        for (Peak p : this.isotopiccluster) {
            intensitysum += p.getIntensity();
        }

        return intensitysum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(" + this.clusterID + ") [ ");
        if (this.isotopiccluster == null) {
            return this.status;
        }
        for (Peak p : this.isotopiccluster) {
            sb.append(p.getMz() + " ");
        }
        sb.append("]");
        return sb.toString();
    }
}
