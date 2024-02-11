import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;

public class FileFilter {
    public static void main(String[] args) {
        boolean intExist = false;
        boolean floatExist = false;
        boolean stringExist = false;
        PrintWriter pwInt = null;
        PrintWriter pwFloat = null;
        PrintWriter pwString = null;
        Statistics intStat = new Statistics(0, Double.MIN_VALUE, Double.MAX_VALUE, 0, "integers.txt");
        Statistics floatStat = new Statistics(0, Double.MIN_VALUE, Double.MAX_VALUE, 0, "floats.txt");
        Statistics stringStat = new Statistics(0, Integer.MAX_VALUE, Integer.MIN_VALUE, "strings.txt");
        boolean pathMode = false;
        boolean pathGet = false;
        boolean prefMode = false;
        boolean prefGet = false;
        boolean appMode = false;
        boolean fullStatMode = false;
        boolean briefStatMode = false;
        boolean isTxt = false;
        String path = "";
        String pref = "";
        for (String fileName : args) {
            if (fileName.endsWith(".txt") & !isTxt) {
                isTxt = true;
            }
            if (fileName.equals("-o") & !pathMode & !isTxt) {
                pathMode = true;
                continue;
            }
            if (pathMode & !pathGet & !isTxt) {
                path = fileName;
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                File directory = new File(path);
                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        System.out.println("Создана новая директория.");
                    }
                }
                pathGet = true;
                continue;
            }
            if (fileName.equals("-p") & !prefMode & !isTxt) {
                prefMode = true;
                continue;
            }
            if (prefMode & !prefGet & !isTxt) {
                pref = fileName;
                prefGet = true;
                continue;
            }
            if (fileName.equals("-a") & !isTxt) {
                appMode = true;
                continue;
            }
            if (fileName.equals("-s") & !isTxt) {
                briefStatMode = !fullStatMode;
                continue;
            }
            if (fileName.equals("-f")  & !isTxt) {
                fullStatMode = true;
                if (briefStatMode) {
                    briefStatMode = false;
                }
                continue;
            }
            if (!fileName.endsWith(".txt"))
            {
                System.out.println("Некорректный ввод. Ожидалось имя файла. Все опции вводятся перед файлами.");
                switch (fileName) {
                    case "-o":
                        System.out.println("Опция -o не может быть использована либо использована повторно.");
                        break;
                    case "-p":
                        System.out.println("Опция -p не может быть использована либо использована повторно.");
                        break;
                }
            }
            try {
                File fileInput = new File(fileName);
                Scanner scanner = new Scanner(fileInput);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!intExist & isLong(line)) {
                        intExist = true;
                        if (pathGet | prefGet) {
                            pwInt = new PrintWriter(new FileWriter(path + pref + "integers.txt", appMode));
                        } else {
                            pwInt = new PrintWriter(new FileWriter("integers.txt", appMode));
                        }
                    }
                    if (!floatExist & !isLong(line) & isDouble(line)) {
                        floatExist = true;
                        if (pathGet | prefGet) {
                            pwFloat = new PrintWriter(new FileWriter(path + pref + "floats.txt", appMode));
                        } else {
                            pwFloat = new PrintWriter(new FileWriter("floats.txt", appMode));
                        }

                    }
                    if (!stringExist & !isLong(line) & !isDouble(line)) {
                        stringExist = true;
                        if (pathGet | prefGet) {
                            pwString = new PrintWriter(new FileWriter(path + pref + "strings.txt", appMode));
                        } else {
                            pwString = new PrintWriter(new FileWriter("strings.txt", appMode));
                        }
                    }
                    if(intExist & isLong(line)) {
                        pwInt.println(line);
                        if (briefStatMode) {
                            intStat.setCounter(intStat.getCounter()+1);
                        }
                        if (fullStatMode) {
                            intStat.setCounter(intStat.getCounter()+1);
                            if (Long.parseLong(line) > intStat.getMaxValue()) {
                                intStat.setMaxValue(Long.parseLong(line));
                            }
                            if (Long.parseLong(line) < intStat.getMinValue()) {
                                intStat.setMinValue(Long.parseLong(line));
                            }
                            intStat.setSum(intStat.getSum()+Long.parseLong(line));
                        }
                    }
                    if (floatExist & !isLong(line) & isDouble(line)) {
                        pwFloat.println(line);
                        if (briefStatMode) {
                            floatStat.setCounter(floatStat.getCounter()+1);
                        }
                        if (fullStatMode) {
                            floatStat.setCounter(floatStat.getCounter()+1);
                            if (Double.parseDouble(line) > floatStat.getMaxValue()) {
                                floatStat.setMaxValue(Double.parseDouble(line));
                            }
                            if (Double.parseDouble(line) < floatStat.getMinValue()) {
                                floatStat.setMinValue(Double.parseDouble(line));
                            }
                            floatStat.setSum(floatStat.getSum()+Double.parseDouble(line));
                        }
                    }
                    if (stringExist & !isLong(line) & !isDouble(line)) {
                        pwString.println(line);
                        if (briefStatMode) {
                            stringStat.setCounter(stringStat.getCounter() + 1);
                        }
                        if (fullStatMode) {
                            stringStat.setCounter(stringStat.getCounter() + 1);
                            String[] words = line.split(" ");
                            int stringLength = words.length-1;
                            for (String word: words) {
                                stringLength += word.length();
                            }
                            if (stringLength > stringStat.getMaxString()) {
                                stringStat.setMaxString(stringLength);
                            }
                            if (stringLength < stringStat.getMinString()) {
                                stringStat.setMinString(stringLength);
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Файл " + fileName + " не найден!");
            } catch (IOException e) {
                System.out.println("Вы ввели неправильно путь.");
            }

        }
        if (!isTxt) {
            System.out.println("Ввод должен содержать хотя бы один текстовый файл.");
            if (pwInt != null) {
                pwInt.close();
            }
            if (pwFloat != null) {
                pwFloat.close();
            }
            if (pwString != null) {
                pwString.close();
            }
            System.exit(0);
        }
        if (pwInt != null) {
            pwInt.close();
        }
        if (pwFloat != null) {
            pwFloat.close();
        }
        if (pwString != null) {
            pwString.close();
        }
        if (briefStatMode) {
            System.out.println("Краткая статистика по записанным элементам:\n");
            System.out.println(intStat.getBriefStat());
            System.out.println(floatStat.getBriefStat());
            System.out.println(stringStat.getBriefStat());
        }
        if (fullStatMode) {
            System.out.println("Полная статистика по записанным элементам:\n");
            System.out.println(intStat.getFullStat());
            System.out.println(floatStat.getFullStat());
            System.out.println(stringStat.getFullStat());
        }
    }

    private static boolean isLong(String input){
        try {
            Long.valueOf(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static boolean isDouble(String input){
        try {
            Double.valueOf(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
class Statistics {
    private int counter;
    private int minString;
    private int maxString;
    private double maxValue;
    private double minValue;
    private double sum;
    private final String file;
    public Statistics(int counter, double maxValue, double minValue, double sum, String file) {
        this.counter = counter;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.sum = sum;
        this.file = file;
    }
    public Statistics(int counter, int minString, int maxString, String file) {
        this.counter = counter;
        this.minString = minString;
        this.maxString = maxString;
        this.file = file;
    }
    public int getMinString() {
        return minString;
    }

    public void setMinString(int minString) {
        this.minString = minString;
    }

    public int getMaxString() {
        return maxString;
    }

    public void setMaxString(int maxString) {
        this.maxString = maxString;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getMean() {
        return sum / counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }


    public String getBriefStat() {
        return switch (file) {
            case "integers.txt":
                yield "Количество целых чисел: " + counter;
            case "floats.txt":
                yield "Количество вещественных чисел: " + counter;
            case "strings.txt":
                yield "Количество строк: " + counter;
            default:
                yield "Wrong filename";
        };
    }

    public String getFullStat() {
        return switch (file) {
            case "integers.txt":
                if (counter != 0) {
                    yield " Статистика по целым числам:\n" + getBriefStat() + ", Минимальный элемент: " + (long) minValue +
                            ", Максимальный элемент: " +
                            (long) maxValue + "\nСумма чисел: " + (long) sum + ", Среднее значение: " + getMean() + ".\n";
                } else {
                    yield "Файл с целыми числами не создан.";
                }
            case "floats.txt":
                if (counter != 0) {
                    yield " Статистика по вещественным числам:\n" + getBriefStat() + ", Минимальный элемент: " + minValue +
                            ", Максимальный элемент: " +
                            maxValue + "\nСумма чисел: " + sum + ", Среднее значение: " + getMean()+ ".\n";
                } else {
                    yield "Файл с вещественными числами не создан.";
                }
            case "strings.txt":
                if (counter != 0) {
                    yield " Статистика по строкам:\n" + getBriefStat() + ", Размер самой короткой строки: " + minString +
                            ", Размер самой длинной строки: " + maxString+ ".\n";
                } else {
                    yield "Файл со строками не создан.";
                }
            default:
                yield "Wrong filename.";
        };
    }
}
