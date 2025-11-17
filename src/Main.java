import functions.*;

public class Main {
    public static void main(String[] args) {
        
        // --- Подготовка ---
        FunctionPoint[] points = {
            new FunctionPoint(0, 0),
            new FunctionPoint(1, 1),
            new FunctionPoint(2, 4),
            new FunctionPoint(3, 9)
        };
        
        FunctionPoint[] points2 = {
            new FunctionPoint(0, 0),
            new FunctionPoint(1, 1),
            new FunctionPoint(2, 4),
            new FunctionPoint(3, 9)
        };
        
        FunctionPoint[] points3 = { // Различающийся
            new FunctionPoint(0, 0),
            new FunctionPoint(1, 2), // <--- Различие
            new FunctionPoint(2, 5),
            new FunctionPoint(3, 10)
        };

        // Создаем объекты двух типов
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(points);

        System.out.println("--- Задание 5.1: Проверка toString() ---");
        System.out.println("Array: " + arrayFunc.toString());
        System.out.println("List:  " + listFunc.toString());
        
        System.out.println("\n--- Задание 5.2: Проверка equals() ---");
        TabulatedFunction arrayFuncEq = new ArrayTabulatedFunction(points2);
        TabulatedFunction listFuncEq = new LinkedListTabulatedFunction(points2);
        TabulatedFunction arrayFuncDiff = new ArrayTabulatedFunction(points3);

        System.out.println("arrayFunc.equals(arrayFuncEq): " + arrayFunc.equals(arrayFuncEq)); // true
        System.out.println("listFunc.equals(listFuncEq):   " + listFunc.equals(listFuncEq));   // true
        System.out.println("arrayFunc.equals(listFuncEq):  " + arrayFunc.equals(listFuncEq));  // true
        System.out.println("listFunc.equals(arrayFuncEq):  " + listFunc.equals(arrayFuncEq));  // true
        System.out.println("arrayFunc.equals(arrayFuncDiff): " + arrayFunc.equals(arrayFuncDiff)); // false
        System.out.println("arrayFunc.equals(null):        " + arrayFunc.equals(null));        // false

        System.out.println("\n--- Задание 5.3: Проверка hashCode() ---");
        System.out.println("arrayFunc.hashCode(): " + arrayFunc.hashCode());
        System.out.println("arrayFuncEq.hashCode(): " + arrayFuncEq.hashCode());
        System.out.println("listFunc.hashCode(): " + listFunc.hashCode());
        System.out.println("listFuncEq.hashCode(): " + listFuncEq.hashCode());
        System.out.println("arrayFuncDiff.hashCode(): " + arrayFuncDiff.hashCode());

        try {
            // Изменяем одну точку (тут setPointX может выбросить исключение, поэтому catch нужен)
            arrayFunc.setPointX(1, 1.0001);
        } catch (InappropriateFunctionPointException e) {
            e.printStackTrace();
        }
        System.out.println("arrayFunc (изменённый) hashCode(): " + arrayFunc.hashCode());
        
        // Восстановим arrayFunc для теста клонирования
        try {
            arrayFunc.setPointX(1, 1.0);
        } catch (InappropriateFunctionPointException e) { e.printStackTrace(); }


        System.out.println("\n--- Задание 5.4: Проверка clone() (Глубокое клонирование) ---");
        try {
            // Клонируем arrayFunc
            ArrayTabulatedFunction arrayClone = (ArrayTabulatedFunction) arrayFunc.clone();
            
            // 1. Изменяем ОРИГИНАЛ
            arrayFunc.setPointY(1, 100.0);
            
            // 2. Печатаем ОБА
            System.out.println("Оригинал Array (изменён): " + arrayFunc.toString());
            System.out.println("Клон Array (не изменён):  " + arrayClone.toString());

            // Клонируем listFunc
            LinkedListTabulatedFunction listClone = (LinkedListTabulatedFunction) listFunc.clone();

            // 1. Изменяем ОРИГИНАЛ
            listFunc.setPointY(1, 100.0);

            // 2. Печатаем ОБА
            System.out.println("Оригинал List (изменён): " + listFunc.toString());
            System.out.println("Клон List (не изменён):  " + listClone.toString());
            
        } catch (CloneNotSupportedException e) { 
            e.printStackTrace();
        }
    }
}
