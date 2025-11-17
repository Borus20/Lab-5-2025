package functions;

// 1. Добавляем extends Cloneable
public interface TabulatedFunction extends Function, java.io.Serializable, Cloneable {
    
    // ... (все старые методы)
    int getPointsCount();
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    double getPointX(int index);
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    double getPointY(int index);
    void setPointY(int index, double y);
    void deletePoint(int index);
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    // 2. Добавляем объявление метода clone()
    Object clone() throws CloneNotSupportedException;
}
