public class Test {
    public static void main() {
        String name = "Jump to line";
        int i = 4;
        for(int j=0;j<i;j++){
            name = name + ":" + name;
        }
        System.out.println(name);
    }
}
