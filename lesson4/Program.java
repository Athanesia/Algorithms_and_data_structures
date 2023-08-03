package ru.geekbrains.lesson4;

public class Program {

    public static void main(String[] args) {

        Employee employee1 = new Employee("AAA", 30);
        System.out.println(employee1.hashCode());

        HashMap<String, String> map = new HashMap<>();

        String v = map.put("+79005551122", "Александр");
        v = map.put("+79005551123", "Сергей");
        v = map.put("+79005551123", "Алексей");
        v = map.put("+79005551124", "Александр1");
        v = map.put("+79005551125", "Александр2");
        v = map.put("+79005551126", "Александр3");
        v = map.put("+79005551127", "Александр4");
        v = map.put("+79005551128", "Александр5");

        String searchRes = map.get("+790055511221");
        System.out.println("Search Result: " + searchRes);

        v = map.remove("+79005551127");
        System.out.println("Removed Value: " + v);

        System.out.println("Iterating through HashMap:");
        for (HashMap.Entry<String, String> entry : map) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
        }
    }
}