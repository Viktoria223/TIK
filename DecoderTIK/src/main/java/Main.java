import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String code = sc.nextLine();

        TreeMap<Character, Integer> alphabetData = getDataAlphabet(new File("C:\\Users\\huawei\\Downloads\\huffman_alphabet.txt"));

        //генерируем список листов
        ArrayList<NodeTree> nodes = new ArrayList<>();
        for (Character c : alphabetData.keySet()) {
            nodes.add(new NodeTree(c, alphabetData.get(c)));
        }

        //строим дерево с помощью алгоритма Хаффмана
        NodeTree tree = getHuffman(nodes);

        //строим таблицу префиксных кодов
        TreeMap<Character, String> codes = new TreeMap<>();
        for (Character c : alphabetData.keySet()) {
            codes.put(c, tree.getCodeForCharacter(c, ""));
        }

        for (Map.Entry<Character, String> m : codes.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
        }

        assert code != null;
        System.out.println(decode(code, tree));
    }

    //алгоритм Хаффмана
    private static NodeTree getHuffman(ArrayList<NodeTree> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            codeTreeNodes.sort(Collections.reverseOrder());
            NodeTree left = codeTreeNodes.remove(0);
            NodeTree right = codeTreeNodes.remove(0);

            //складываем два листа с наименьшими вероятностями
            NodeTree parent = new NodeTree(null, right.probability + left.probability, left, right);
            codeTreeNodes.add(parent);
        }
        return codeTreeNodes.get(0);
    }

    private static String decode(String encoded, NodeTree tree) {
        StringBuilder decoded = new StringBuilder();

        NodeTree node = tree;
        for (int i = 0; i < encoded.length(); i++) {
            if (encoded.charAt(i) == '0') {
                node = node.left;
            } else {
                node = node.right;
            }
            if (node.character != null) {
                decoded.append(node.character);
                node = tree;
            }
        }
        return decoded.toString();
    }

    private static TreeMap<Character, Integer> getDataAlphabet(File file) {
        TreeMap<Character, Integer> map = new TreeMap<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            List<String> alphabet = Arrays.asList(reader.readLine().split(" "));
            List<String> probabilities = Arrays.asList(reader.readLine().split(" "));

            for (int i = 0; i < alphabet.size(); i++) {
                int pr;
                if (probabilities.get(i).contains(",") || probabilities.get(i).contains(".")) {
                    pr = (int) (Float.parseFloat(probabilities.get(i).replace(",", ".")) * 1000000);
                } else {
                    pr = Integer.parseInt(probabilities.get(i));
                }
                map.put(alphabet.get(i).toCharArray()[0], pr);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!!!");
        } catch (IOException e) {
            System.out.println("Некорректный файл!!!");
        }
        return map;
    }

    //узел дерева
    public static class NodeTree implements Comparable<NodeTree> {
        Character character;
        int probability;
        NodeTree left;
        NodeTree right;

        public NodeTree(Character character, int probability) {
            this.character = character;
            this.probability = probability;
        }

        public NodeTree(Character character, int probability, NodeTree left, NodeTree right) {
            this.character = character;
            this.probability = probability;
            this.left = left;
            this.right = right;
        }

        //сортируем
        @Override
        public int compareTo(NodeTree o) {
            return o.probability - probability;
        }

        public String getCodeForCharacter(Character ch, String parentPath) {
            if (character == ch) {
                return parentPath;
            } else {
                if (left != null) {
                    String path = left.getCodeForCharacter(ch, parentPath + "0");
                    if (path != null) {
                        return path;
                    }
                }
                if (right != null) {
                    String path = right.getCodeForCharacter(ch, parentPath + "1");
                    if (path != null) {
                        return path;
                    }
                }
            }
            return null;
        }
    }
}

