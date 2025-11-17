package functions;

// Добавляем implements Cloneable для Задания 4
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

    // --- НОВЫЕ МЕТОДЫ (Задание 1) ---

    public String toString() {
        // Возвращает строку в формате "(x; y)"
        return "(" + x + "; " + y + ")";
    }

    public boolean equals(Object o) {
        // 1. Проверка на тот же самый объект
        if (this == o) return true;
        // 2. Проверка, что o - это вообще FunctionPoint
        if (o == null || getClass() != o.getClass()) return false;
        
        // 3. Приведение типа и сравнение полей
        FunctionPoint that = (FunctionPoint) o;
        
        // Используем Double.compare для точного сравнения битов double
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    public int hashCode() {
        // Используем XOR, как предложено в задании
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        int xHash = (int) (xBits ^ (xBits >>> 32)); // Хэш для x
        int yHash = (int) (yBits ^ (yBits >>> 32)); // Хэш для y
        
        // Комбинируем хэши
        return 31 * xHash + yHash;
    }

    public Object clone() throws CloneNotSupportedException {
        // Вызываем clone() родительского класса Object
        return super.clone();
    }
}
