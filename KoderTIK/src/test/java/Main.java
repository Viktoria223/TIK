import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String text;
        Scanner sc = new Scanner(System.in);
        text = sc.nextLine();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\huawei\\Desktop\\LZW.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String alph = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        List<String> algBW = getAlgBW(text);
        String word = algBW.get(0);
        Integer indBW = Integer.valueOf(algBW.get(1));
        String result = getLZW(word, alph);
        System.out.println(result);
        System.out.println(indBW);
        System.out.println(word);
    }

    public static List<String> getAlgBW(String text) {
        List<String> shiftList = new ArrayList<>();
        int size = text.length();
        List<String> sortedStrings;
        int n = -1;
        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String shift = text.substring(i, size) + text.substring(0, i);
            shiftList.add(shift);
        }

        sortedStrings = shiftList.stream().sorted().collect(Collectors.toList());

        for (int i = 0; i < size; i++) {
            sb.append(sortedStrings.get(i).charAt(size - 1));
            if (sortedStrings.get(i).equals(text)) {
                n = i;
            }
        }

        result.add(sb.toString());
        result.add(String.valueOf(n+1));

        return result;
    }

    public static String getLZW(String text, String alph) {

        Map<String, Integer> combinations = new HashMap<>();
        StringBuilder modAlph = new StringBuilder();
        int sizeAlph = alph.length();
        int sizeText = text.length();

        List<Integer> result = new ArrayList<>();
        int ind = -1;

        //убираем пробелы в строке алфавита
        for (int i = 0; i < alph.length(); i++) {
            if (i % 2 == 0) {
                modAlph.append(alph.charAt(i));
            }
        }

        sizeAlph = modAlph.length();
        int numberCode = sizeAlph;

        //создаем комбинации и их коды из алфавита
        for (int i = 0; i < sizeAlph; i++) {
            combinations.put(String.valueOf(modAlph.charAt(i)), i);
        }

        for (int j = 0; j < sizeText; j++) {
            String st = String.valueOf(text.charAt(0));
            StringBuilder words = new StringBuilder();

            //ищем наибольшее слово
            for (int i = j; i < sizeText; i++) {
                words.append(text.charAt(i));
                for (String s : combinations.keySet()) {
                    if (words.toString().equals(s)) {
                        st = words.toString();
                        ind = i;
                    }
                }
            }

            //кладем код
            for (Map.Entry<String, Integer> r : combinations.entrySet()) {
                if (st.equals(r.getKey())) {
                    result.add(r.getValue());
                }
            }

            if ((ind + 1) < sizeText) {
                //ищем новое слово для комбинаций
                StringBuilder newWord = new StringBuilder();
                newWord.append(st);
                newWord.append(text.charAt(ind+1));

                //добавляем новое слово в комбинации
                combinations.put(newWord.toString(), numberCode);
                numberCode++;


            }

            j += st.length() - 1;
        }

        //двоичное представление
        List<String> strResult = new ArrayList<>();
        for (Integer i : result) {
            strResult.add(Integer.toBinaryString(i));
        }

        StringBuilder res = new StringBuilder();
        for (String i : strResult) {
            if (i.length() < 8) {
                StringBuilder r = new StringBuilder();
                int t = 8 - i.length();
                for (int j = 0; j < t; j++) {
                    r.append("0");
                }
                r.append(i);
                res.append(r);
            }
        }

        return res.toString();
    }
}

