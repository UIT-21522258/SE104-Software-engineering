package model;


public class revenue {
    private double[] data;
    revenue(){
        data = new double[12];
        for(int i=0;i<12;i++){
            data[i]=0;
        }
    }
    public void setData(double[] data){
        for(int i=0;i<12;i++){
            this.data[i]=data[i];
        }
    }
    public double[] getData(){
        return data;
    }
}
