
public class Demo {
	public static void main(String[] args) {
		String ty="CD";
		String string="";
		for(int i=1;i<=13;i++){
			if(i<10){
			string+="{\"code\":\""+ty+"-00"+i+"\"},";
			}else{
				string+="{\"code\":\""+ty+"-0"+i+"\"},";
			}
			
		}
		System.out.println(string);
	}
}