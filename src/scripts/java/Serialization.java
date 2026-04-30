import java.io.*;

public class Serialization {
    public static void main(String[] args) {
        User user = new User("Alice", 30, "secret123");
        String filename = "src/scripts/user.ser";
        
        // 1. Serialization: Writing the object to a file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(user);
            System.out.println("Object serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 2. Deserialization: Reading the object back
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            User restoredUser = (User) in.readObject();
            System.out.println("Object deserialized: " + restoredUser);
            // Note: password will be null because it was marked 'transient'
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static class User implements Serializable {
        // Explicitly declaring a serialVersionUID is recommended for version control
        private static final long serialVersionUID = 1L;
        
        private String name;
        private int age;
        
        // The 'transient' keyword prevents specific fields from being serialized
        private transient String password;
        
        public User(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }
        
        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + ", password='" + password + "'}";
        }
    }
    
}
