package functions;

public class FunctionPoint implements java.io.Serializable, Cloneable {
    private double x;
    private double y;
    
    private static final long serialVersionUID = 1L;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    // --- ИСПРАВЛЕННЫЙ МЕТОД ---
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FunctionPoint that = (FunctionPoint) o;
        
        // Задаем точность (машинный эпсилон)
        double epsilon = 1e-9; 

        // Проверяем x и y с учетом погрешности
        // Если разница между числами меньше epsilon, считаем их равными
        boolean xEquals = Math.abs(this.x - that.x) < epsilon;
        boolean yEquals = Math.abs(this.y - that.y) < epsilon;

        return xEquals && yEquals;
    }

    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        int xHash = (int) (xBits ^ (xBits >>> 32));
        int yHash = (int) (yBits ^ (yBits >>> 32));
        
        return 31 * xHash + yHash;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
