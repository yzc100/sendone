
public class Regex {
	public static void main(String[] args) {
		String demo = "select * from stock";
		System.out.println(demo.replaceAll("[*]", "count(*)"));
	}
}
