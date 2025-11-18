package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    private class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;
    }

    private FunctionNode head;
    private int count;
    private static final long serialVersionUID = 1L;

    // --- Конструкторы ---
    public LinkedListTabulatedFunction() {
        this.count = 0;
        this.head = new FunctionNode();
        this.head.prev = head;
        this.head.next = head;
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Invalid arguments for function creation");
        }
        this.count = 0;
        this.head = new FunctionNode();
        this.head.prev = head;
        this.head.next = head;
    
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            try {
                this.addPoint(new FunctionPoint(leftX + i * step, 0));
            } catch (InappropriateFunctionPointException e) { /* Невозможно */ }
        }
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
         if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Invalid arguments for function creation");
        }
        this.count = 0;
        this.head = new FunctionNode();
        this.head.prev = head;
        this.head.next = head;
    
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
             try {
                this.addPoint(new FunctionPoint(leftX + i * step, values[i]));
             } catch (InappropriateFunctionPointException e) { /* Невозможно */ }
        }
    }
    
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Function must have at least 2 points");
        }
        this.count = 0;
        this.head = new FunctionNode();
        this.head.prev = head;
        this.head.next = head;
    
        for (int i = 0; i < points.length; ++i) {
            if (i > 0 && points[i-1].getX() >= points[i].getX()) {
                 throw new IllegalArgumentException("Points are not sorted by X");
            }
            try {
                this.addPoint(new FunctionPoint(points[i]));
            } catch (InappropriateFunctionPointException e) { /* Невозможно */ }
        }
    }


    // --- Внутренние методы ---
    
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= count) {
            throw new FunctionPointIndexOutOfBoundsException("Index " + index + " is out of bounds");
        }
        FunctionNode current;
        if (index < count / 2) {
            current = head.next;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = head.prev;
            for (int i = count - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private FunctionNode addNode(FunctionNode node) {
        FunctionNode newNode = new FunctionNode();
        newNode.prev = node.prev;
        newNode.next = node;
        node.prev.next = newNode;
        node.prev = newNode;
        count++;
        return newNode;
    }

    private FunctionNode deleteNode(FunctionNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        count--;
        return node;
    }
    
    // --- Основные методы ---
    
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    // --- ИСПРАВЛЕННЫЙ МЕТОД (Задание 2) ---
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder() || count == 0) {
            return Double.NaN;
        }

        FunctionNode current = head.next;
        while (current != head) {
            double epsilon = 1e-9; // Машинный эпсилон

            // Проверка: если x совпадает с точкой текущего узла
            if (Math.abs(current.point.getX() - x) < epsilon) {
                return current.point.getY();
            }

            // Проверяем интервал между текущим и следующим узлом
            if (current.next != head) {
                double x1 = current.point.getX();
                double x2 = current.next.point.getX();
                
                // Если x строго внутри интервала
                if (x > x1 && x < x2) {
                    // Проверяем правую границу на всякий случай, чтобы избежать интерполяции при x ~= x2
                    if (Math.abs(x2 - x) < epsilon) {
                        return current.next.point.getY();
                    }
                    
                    // Если точного совпадения нет, делаем интерполяцию
                    double y1 = current.point.getY();
                    double y2 = current.next.point.getY();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
            }
            current = current.next;
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return count;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= count) {
            throw new FunctionPointIndexOutOfBoundsException("Index " + index + " is out of bounds [0, " + (count - 1) + "]");
        }
    }

    public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndex(index);
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();
        double leftBound = (node.prev == head) ? Double.NEGATIVE_INFINITY : node.prev.point.getX();
        double rightBound = (node.next == head) ? Double.POSITIVE_INFINITY : node.next.point.getX();

        if (newX <= leftBound || newX >= rightBound) {
            throw new InappropriateFunctionPointException("X coordinate is out of the allowed interval");
        }
        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        checkIndex(index);
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndex(index);
        FunctionNode node = getNodeByIndex(index);
        double leftBound = (node.prev == head) ? Double.NEGATIVE_INFINITY : node.prev.point.getX();
        double rightBound = (node.next == head) ? Double.POSITIVE_INFINITY : node.next.point.getX();
        
        if (x <= leftBound || x >= rightBound) {
            throw new InappropriateFunctionPointException("X coordinate is out of the allowed interval");
        }
        node.point.setX(x);
    }

    public double getPointY(int index) {
        checkIndex(index);
        return getNodeByIndex(index).point.getY();
    }
    
    public void setPointY(int index, double y) {
        checkIndex(index);
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        checkIndex(index);
        if (count < 3) {
            throw new IllegalStateException("Cannot delete point: function must have at least 2 points remaining.");
        }
        deleteNode(getNodeByIndex(index));
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.next;
        while(current != head && current.point.getX() < point.getX()) {
            current = current.next;
        }
        if(current != head && current.point.getX() == point.getX()) {
            throw new InappropriateFunctionPointException("Point with X=" + point.getX() + " already exists.");
        }
        addNode(current).point = new FunctionPoint(point);
    }

    // --- Методы Externalizable ---
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(count);
        FunctionNode current = head.next;
        while(current != head) {
            out.writeObject(current.point);
            current = current.next;
        }
    }
    
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int readCount = in.readInt();
        this.head.prev = head;
        this.head.next = head;
        this.count = 0;
        
        for (int i = 0; i < readCount; i++) {
            FunctionPoint point = (FunctionPoint) in.readObject();
            try {
                this.addPoint(point);
            } catch (InappropriateFunctionPointException e) {
                 throw new IOException("Failed to deserialize: " + e.getMessage());
            }
        }
    }


    // --- НОВЫЕ МЕТОДЫ ---

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.next;
        while (current != head) {
            sb.append(current.point.toString());
            if (current.next != head) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction that = (TabulatedFunction) o;

        if (this.getPointsCount() != that.getPointsCount()) return false;

        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction thatList = (LinkedListTabulatedFunction) o;
            FunctionNode thisCurrent = this.head.next;
            FunctionNode thatCurrent = thatList.head.next;
            
            while (thisCurrent != this.head) {
                if (!thisCurrent.point.equals(thatCurrent.point)) {
                    return false;
                }
                thisCurrent = thisCurrent.next;
                thatCurrent = thatCurrent.next;
            }
        } else {
            for (int i = 0; i < this.getPointsCount(); i++) {
                if (!this.getPoint(i).equals(that.getPoint(i))) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public int hashCode() {
        int result = 1;
        FunctionNode current = head.next;
        while (current != head) {
            result = 31 * result + current.point.hashCode();
            current = current.next;
        }
        result = 31 * result + Integer.hashCode(count);
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] clonedPoints = new FunctionPoint[this.count];
        FunctionNode current = this.head.next;
        for (int i = 0; i < this.count; i++) {
            clonedPoints[i] = (FunctionPoint) current.point.clone();
            current = current.next;
        }
        return new LinkedListTabulatedFunction(clonedPoints);
    }
}
