package edu.wpi.off.by.one.errors.code.model;

/**
 * Created by roboman2444 on 11/23/15.
 * uses Darkplaces matrixlib.c and .h as reference.
 */
public class Matrix {
    private double[][] m;

    public double[][] getM(){ return m;}

    public Matrix(){
        //identity matrix constructor
        m = new double[4][4];
        m[0][0] = 1.0;
        m[1][1] = 1.0;
        m[2][2] = 1.0;
        m[3][3] = 1.0;
    }
    public Matrix(double[][] in){
        //identity matrix constructor
        m = in;
    }
    //create a translation matrix
    public Matrix(Coordinate coord){
        m = new double[4][4];
        m[0][0]=1.0;
        m[1][0]=0.0;
        m[2][0]=0.0;
        m[3][0]=coord.getX();
        m[0][1]=0.0;
        m[1][1]=1.0;
        m[2][1]=0.0;
        m[3][1]=coord.getY();
        m[0][2]=0.0;
        m[1][2]=0.0;
        m[2][2]=1.0;
        m[3][2]=coord.getZ();
        m[0][3]=0.0;
        m[1][3]=0.0;
        m[2][3]=0.0;
        m[3][3]=1.0;
    }
    //same as concatinating a translation matrix
    public Matrix translate(Coordinate coord){
        Matrix in2 = new Matrix(coord);
        return this.concat(in2);
    }
    //create a rotation matrix
    public Matrix(double angle, double x, double y, double z){
        m = new double[4][4];
        double len, c, s;

        len = x*x+y*y+z*z;
        if (len != 0.0)
            len = 1.0 / (double)Math.sqrt(len);
        x *= len;
        y *= len;
        z *= len;

        angle *= (-Math.PI / 180.0);
        c = (double)Math.cos(angle);
        s = (double)Math.sin(angle);
        
        m[0][0]=x * x + c * (1.0 - x * x);
        m[1][0]=x * y * (1.0 - c) + z * s;
        m[2][0]=z * x * (1.0 - c) - y * s;
        m[3][0]=0.0;
        m[0][1]=x * y * (1.0 - c) - z * s;
        m[1][1]=y * y + c * (1.0 - y * y);
        m[2][1]=y * z * (1.0 - c) + x * s;
        m[3][1]=0.0;
        m[0][2]=z * x * (1.0 - c) + y * s;
        m[1][2]=y * z * (1.0 - c) - x * s;
        m[2][2]=z * z + c * (1.0 - z * z);
        m[3][2]=0.0;
        m[0][3]=0.0;
        m[1][3]=0.0;
        m[2][3]=0.0;
        m[3][3]=1.0;
    }
    //same as concatinating a rotation matrix on, for ease
    public Matrix rotate(double angle, double x, double y, double z){
        Matrix in2 = new Matrix(angle, x, y, z);
        return this.concat(in2);
    }
    //create a scale matrix
    public Matrix(double scale){
        m = new double[4][4];
        m[0][0]=scale;
        m[0][1]=0.0;
        m[0][2]=0.0;
        m[0][3]=0.0;
        m[1][0]=0.0;
        m[1][1]=scale;
        m[1][2]=0.0;
        m[1][3]=0.0;
        m[2][0]=0.0;
        m[2][1]=0.0;
        m[2][2]=scale;
        m[2][3]=0.0;
        m[3][0]=0.0;
        m[3][1]=0.0;
        m[3][2]=0.0;
        m[3][3]=1.0;
    }
    //same as concatinating a scale matrix on, for ease
    public Matrix scale(double scale){
        Matrix in2 = new Matrix(scale);
        return this.concat(in2);
    }
    //create a scale3 matrix
    public Matrix(double scalex, double scaley, double scalez){
        m = new double[4][4];
        m[0][0]=scalex;
        m[0][1]=0.0;
        m[0][2]=0.0;
        m[0][3]=0.0;
        m[1][0]=0.0;
        m[1][1]=scaley;
        m[1][2]=0.0;
        m[1][3]=0.0;
        m[2][0]=0.0;
        m[2][1]=0.0;
        m[2][2]=scalez;
        m[2][3]=0.0;
        m[3][0]=0.0;
        m[3][1]=0.0;
        m[3][2]=0.0;
        m[3][3]=1.0;
    }
    //same as concatinating a scale3 matrix on, for ease
    public Matrix scale3(double scalex, double scaley, double scalez){
        Matrix in2 = new Matrix(scalex, scaley, scalez);
        return this.concat(in2);
    }
    public Matrix getRotateOnly(){
        double[][] out = new double[4][4];
        out[0][0] = m[0][0];
        out[0][1] = m[0][1];
        out[0][2] = m[0][2];
        out[0][3] = 0.0;
        out[1][0] = m[1][0];
        out[1][1] = m[1][1];
        out[1][2] = m[1][2];
        out[1][3] = 0.0;
        out[2][0] = m[2][0];
        out[2][1] = m[2][1];
        out[2][2] = m[2][2];
        out[2][3] = 0.0;
        out[3][0] = 0.0;
        out[3][1] = 0.0;
        out[3][2] = 0.0;
        out[3][3] = 1.0;

        return new Matrix(out);
    }
    public Matrix getTranslateOnly(){
        double[][] out = new double[4][4];
        out[0][0] = 1.0;
        out[1][0] = 0.0;
        out[2][0] = 0.0;
        out[3][0] = m[0][3];
        out[0][1] = 0.0;
        out[1][1] = 1.0;
        out[2][1] = 0.0;
        out[3][1] = m[1][3];
        out[0][2] = 0.0;
        out[1][2] = 0.0;
        out[2][2] = 1.0;
        out[3][2] = m[2][3];
        out[0][3] = 0.0;
        out[1][3] = 0.0;
        out[2][3] = 0.0;
        out[3][3] = 1.0;
        return new Matrix(out);
    }
    public Matrix concat(Matrix in){
        double[][] out = new double[4][4];
        double[][] in2 = in.getM();
        out[0][0] = m[0][0] * in2[0][0] + m[1][0] * in2[0][1] + m[2][0] * in2[0][2] + m[3][0] * in2[0][3];
        out[1][0] = m[0][0] * in2[1][0] + m[1][0] * in2[1][1] + m[2][0] * in2[1][2] + m[3][0] * in2[1][3];
        out[2][0] = m[0][0] * in2[2][0] + m[1][0] * in2[2][1] + m[2][0] * in2[2][2] + m[3][0] * in2[2][3];
        out[3][0] = m[0][0] * in2[3][0] + m[1][0] * in2[3][1] + m[2][0] * in2[3][2] + m[3][0] * in2[3][3];
        out[0][1] = m[0][1] * in2[0][0] + m[1][1] * in2[0][1] + m[2][1] * in2[0][2] + m[3][1] * in2[0][3];
        out[1][1] = m[0][1] * in2[1][0] + m[1][1] * in2[1][1] + m[2][1] * in2[1][2] + m[3][1] * in2[1][3];
        out[2][1] = m[0][1] * in2[2][0] + m[1][1] * in2[2][1] + m[2][1] * in2[2][2] + m[3][1] * in2[2][3];
        out[3][1] = m[0][1] * in2[3][0] + m[1][1] * in2[3][1] + m[2][1] * in2[3][2] + m[3][1] * in2[3][3];
        out[0][2] = m[0][2] * in2[0][0] + m[1][2] * in2[0][1] + m[2][2] * in2[0][2] + m[3][2] * in2[0][3];
        out[1][2] = m[0][2] * in2[1][0] + m[1][2] * in2[1][1] + m[2][2] * in2[1][2] + m[3][2] * in2[1][3];
        out[2][2] = m[0][2] * in2[2][0] + m[1][2] * in2[2][1] + m[2][2] * in2[2][2] + m[3][2] * in2[2][3];
        out[3][2] = m[0][2] * in2[3][0] + m[1][2] * in2[3][1] + m[2][2] * in2[3][2] + m[3][2] * in2[3][3];
        out[0][3] = m[0][3] * in2[0][0] + m[1][3] * in2[0][1] + m[2][3] * in2[0][2] + m[3][3] * in2[0][3];
        out[1][3] = m[0][3] * in2[1][0] + m[1][3] * in2[1][1] + m[2][3] * in2[1][2] + m[3][3] * in2[1][3];
        out[2][3] = m[0][3] * in2[2][0] + m[1][3] * in2[2][1] + m[2][3] * in2[2][2] + m[3][3] * in2[2][3];
        out[3][3] = m[0][3] * in2[3][0] + m[1][3] * in2[3][1] + m[2][3] * in2[3][2] + m[3][3] * in2[3][3];
        return new Matrix(out);
    }

    public Coordinate transform(Coordinate in){
        double inx = in.getX();
        double iny = in.getY();
        double inz = in.getZ();
        double x = inx * m[0][0] + iny * m[1][0] + inz * m[2][0] + m[3][0];
        double y = inx * m[0][1] + iny * m[1][1] + inz * m[2][1] + m[3][1];
        double z = inx * m[0][2] + iny * m[1][2] + inz * m[2][2] + m[3][2];
        return new Coordinate((float)x, (float)y, (float)z);
    }
}
